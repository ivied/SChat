package ivied.p001astreamchat.Sites.Sc2tv;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.ChatView.AdapterChatCursor;
import ivied.p001astreamchat.Core.SendMessageService;
import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Login.Login;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Sites.Site;

/**
 * Cddreated by Serv on 29.05.13.
 */
public class Sc2tv extends Site {
    final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
    public static String sc2tvNick;
    public static String token=null;
    static public HttpClient client = new DefaultHttpClient();
    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";
    private static final String GET_TOKEN = "http://chat.sc2tv.ru/gate.php?task=GetUserInfo&ref=http://sc2tv.ru/";
    private static final String SC2TV_GATE =  "http://chat.sc2tv.ru/gate.php";
    final String SAVED_NAME = "SC2TV";
    final String SAVED_PASS = "SC2TVpass";
    ScheduledExecutorService sEs;

    @Override
    public Drawable getLogo() {
        return MyApp.getContext().getResources().getDrawable(R.drawable.sc2tv);
    }
    @Override
    public void readChannel(String channel) {
        HttpGet httpGet = new HttpGet(CHANNEL_MESSAGES + channel + ".json");
        StringBuilder builderJson = jsonRequest(httpGet);

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObj = new JSONObject(builderJson.toString());


            jsonArray = jsonObj.getJSONArray("messages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        insertSc2tv (jsonArray, 0, channel);


    }

    @Override
    public void startThread(ChannelRun channelRun) {

        sEs = Executors.newScheduledThreadPool(1);

        mFuture = sEs.scheduleWithFixedDelay(channelRun, 0, 3,
                TimeUnit.SECONDS);
    }

    @Override
    public FragmentLoginStandard getFragment() {
        return new FragmentLoginSC2TV();
    }

    @Override
    public FragmentAddChannelStandard getFragmentAddChannel() {
        return new FragmentFindChannelSc2tv();
    }

    @Override
    public int getColorForAdd(String channel, int length, int color) {
        if (!(length==0)){

            if (length>8) length=8;
            color = -Integer.parseInt(channel.substring(0, length));
        }
        return color;
    }

    @Override
    public void getLogin() {

        SharedPreferences preferences = MyApp.getContext().getSharedPreferences(Login.XML_LOGIN,0);
        String name = preferences.getString(SAVED_NAME, "");
        String pass = preferences.getString(SAVED_PASS, "");
        sc2tvNick = preferences.getString(SAVED_NAME, "");
        try {
            client = new DefaultHttpClient();
            HttpPost post = getSc2tvPost(name , pass);
            HttpResponse response = client.execute(post);
            HttpGet httpGet = new HttpGet(GET_TOKEN );

            response = Sc2tv.client.execute(httpGet);

            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }



            JSONObject jsonObj = new JSONObject(builder.toString());

            token = jsonObj.getString("token");
            content.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertSc2tv(JSONArray jsonArray, int i, String channel) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String[] selectionArgs = new String[] { jsonObject.getString("id") };
            Cursor c = MyApp.getContext().getContentResolver().query(
                    INSERT_URI, null,
                    "identificator = ?", selectionArgs, null);
            if (c.getCount() == 0) {

                i++;
                insertSc2tv(jsonArray, i, channel);
                i--;
                String nick = jsonObject.getString("name");
                String message = jsonObject.getString("message");
                String id = jsonObject.getString("id");
                long time = 0;
                try {

                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    final Date d = df.parse(jsonObject.getString("date"));

                    time = d.getTime() / 1000;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                insertMessage(  channel, nick, message, id, time);
                if (MainActivity.showNotifySystem){
                    if (privateMessage(message)){
                        message = message.replace("<b>", "").replace("</b>", "");
                        chatService.sendPrivateNotify(message,channel,  getSiteEnum());
                    }

                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private boolean privateMessage (String message) {

        Matcher matcher = bold.matcher(message);
        if (matcher.find()) {

            String address = matcher.group(2);
            if (address.equalsIgnoreCase(sc2tvNick)) return true;
        }
        return false;
    }

    @Override
    public FactorySite.SiteName getSiteEnum() {
        return FactorySite.SiteName.SC2TV;
    }

    @Override
    public int sendMessage(String channel, String message) {
        if (Sc2tv.sc2tvNick.equalsIgnoreCase(""))
            return SendMessageService.NEED_LOGIN;
        else {

            int count = 0;
            for (Map.Entry<Pattern, Integer> entry : AdapterChatCursor.emoticons
                    .entrySet()) {

                Matcher matcher = entry.getKey().matcher(message);
                while (matcher.find()) {
                    count++;
                }
            }
            if (count < 3) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        4);

                nameValuePairs.clear();
                nameValuePairs.add(new BasicNameValuePair("task",
                        "WriteMessage"));
                nameValuePairs.add(new BasicNameValuePair(
                        "message", message));
                nameValuePairs.add(new BasicNameValuePair(
                        "channel_id", channel));
                nameValuePairs.add(new BasicNameValuePair("token",
                        Sc2tv.token));
                HttpPost post2 = new HttpPost( SC2TV_GATE  );

                try {
                       post2.setEntity(new UrlEncodedFormEntity(
                       nameValuePairs, HTTP.UTF_8));
                   Sc2tv.client.execute(post2);
                } catch (ClientProtocolException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                client.getConnectionManager().shutdown();
                return SendMessageService.MESSAGE_DELIVER_OK;
            }else{
                return SendMessageService.TOO_MUCH_SMILES_SC2TV;
            }
        }

    }


    public HttpResponse getResponseSc2tvRu(String name, String pass) {
        HttpResponse response = null;
        HttpPost post = getSc2tvPost(name, pass);
        try {
            HttpClient client = new DefaultHttpClient();
            response = client.execute(post);
            client.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {


        } catch (IOException e) {


        }

        return response;
    }


    private HttpPost getSc2tvPost (String name, String pass) {
        HttpPost post = new HttpPost("http://sc2tv.ru/");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                4);
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("pass", pass));
        nameValuePairs.add(new BasicNameValuePair("op", "Вход"));
        nameValuePairs.add(new BasicNameValuePair("form_id","user_login_block"));
        try {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return post;
    }



      /*private int getCountOfNew(JSONArray jsonArray) {

        int i = 0 ;

        Cursor c  ;
        JSONObject jsonObj = new JSONObject();
        try {


            do{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String[] selectionArgs = new String[] { jsonObject.getString("id") };

                c = MyApp.getContext().getContentResolver().query(
                        INSERT_URI, null,
                        "identificator = ?", selectionArgs, null);
                if (c.getCount() == 0) i++;
            }while ((c.getCount() == 0) && (jsonArray.length() != i));
            c.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } return i;
    }
*/
}
