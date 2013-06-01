package ivied.p001astreamchat.Sites.Sc2tv;

import android.database.Cursor;
import android.net.Uri;

import org.apache.http.Header;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.SendMessageService;
import ivied.p001astreamchat.Sites.Site;

/**
 * Cddreated by Serv on 29.05.13.
 */
public class Sc2tv extends Site {
    final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";
    ScheduledExecutorService sEs;

    @Override
    public void readChannel(String channel) {

        StringBuilder builderJson = jsonRequest(channel);

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObj = new JSONObject(builderJson.toString());


            jsonArray = jsonObj.getJSONArray("messages");} catch (JSONException e) {
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


    private StringBuilder jsonRequest(String channel) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(CHANNEL_MESSAGES + channel + ".json");

        try {

            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                content.close();
            } else {

            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    client.getConnectionManager().shutdown();
    return builder;
    }

    private boolean privateMessage (String message) {

        Matcher matcher = bold.matcher(message);
        if (matcher.find()) {

            String address = matcher.group(2);
            if (address.equalsIgnoreCase(SendMessageService.sc2tvNick)) return true;
        }
        return false;
    }

    @Override
    public FactorySite.SiteName getSiteEnum() {
        return FactorySite.SiteName.SC2TV;
    }


    public HttpResponse getResponseSc2tvRu(String name, String pass) {
        HttpResponse response = null;
        HttpPost post = new HttpPost("http://sc2tv.ru/");
        try {
            // post
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    4);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("pass", pass));
            nameValuePairs.add(new BasicNameValuePair("op", "¬ход"));
            nameValuePairs.add(new BasicNameValuePair("form_id",
                    "user_login_block"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpClient client = new DefaultHttpClient();
            response = client.execute(post);
            client.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {


        } catch (IOException e) {


        }

        return response;
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
