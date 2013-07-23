package ivied.p001astreamchat.Sites.Sc2tv;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.SendMessageService;
import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Message;
import ivied.p001astreamchat.Sites.Site;


/**
 * Cddreated by Serv on 29.05.13.
 */
public class Sc2tv extends Site {
    final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
    private static final String SC2TV_SMILES = "http://chat.sc2tv.ru/js/smiles.js";
    public static String sc2tvNick;
    public static String sc2tvPass;
    public static String token=null;
    static public HttpClient client = new DefaultHttpClient();
    private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";
    private static final String GET_TOKEN = "http://chat.sc2tv.ru/gate.php?task=GetUserInfo&ref=http://sc2tv.ru/";
    private static final String SC2TV_GATE =  "http://chat.sc2tv.ru/gate.php";
    private static final String SC2TV_SMILE_MODIFY_HEADER = "Last-Modified";
    final String SAVED_NAME = "SC2TV";
    final String SAVED_PASS = "SC2TVpass";
    private static final String SC2TV_STANDARD_SMILE_WAY = "http://chat.sc2tv.ru/img/";
    private static final int SC2TV_SMILE_FIELD_ADDRESS = 2;
    private static final int SC2TV_SMILE_FIELD_REGEXP = 1;
    private static final int SC2TV_SMILE_FIELD_WIDTH = 3;
    private static final int SC2TV_SMILE_FIELD_HEIGHT = 4;
    final static StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    ScheduledExecutorService sEs;
    private static Map<String, Bitmap> smileMap = new HashMap<String, Bitmap>();



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
        super.getLogin();

        try {
            client = new DefaultHttpClient();
            HttpPost post = getSc2tvPost(sc2tvNick , sc2tvPass);
            client.execute(post);
            HttpGet httpGet = new HttpGet(GET_TOKEN );

            HttpResponse response = client.execute(httpGet);

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

    @Override
    public String getSmileAddress() {
        return SC2TV_SMILES;
    }

    @Override
    public String getSmileModifyHeader() {
        return SC2TV_SMILE_MODIFY_HEADER;
    }

    @Override
    public void getSiteSmiles(String header) {

        HttpGet httpGet = new HttpGet(getSmileAddress());
        StringBuilder builderJson = jsonRequest(httpGet);
        String [] sc2tvSmileJson = parseSc2tv(builderJson.toString());
        numberOfSmiles = sc2tvSmileJson.length;

        String smileAddress;

        for (String smile: sc2tvSmileJson){
            String [] smileArray = smile.split(": '");
            smileAddress = getSmileSubstring (smileArray, SC2TV_SMILE_FIELD_ADDRESS);
            PutSmile putSmile = new PutSmile(SC2TV_STANDARD_SMILE_WAY + smileAddress, getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_REGEXP),
                    getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_WIDTH),
                    getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_HEIGHT), this, header);
            putSmile.run();

        }
    }

    @Override
    public int getMiniLogo() {
        return R.drawable.sc2tv_small;
    }


    private String getSmileSubstring(String [] smileArray, int i) {

        String substring = smileArray[i].substring(0, smileArray[i].indexOf("'"));

        return substring;
    }

    private String[] parseSc2tv(String file) {
        file = file.substring(file.indexOf("{")+1);
        String  [] fileArray = file.split("\\{");
        for (int i = 0; fileArray.length > i; i++) {

            fileArray[i]= fileArray[i].substring(0, fileArray[i].indexOf("}"));

        }

        return fileArray;
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
                c.close();
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
                Message messageForInsert = new Message( channel, nick, message, id, time);
                insertMessage( messageForInsert );
                privateMessage( messageForInsert );
            }else {
            c.close();}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected boolean isPrivateMessage(String message) {

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
    public Spannable getSmiledText(String text, String nick) {

        int addressLength = 0;
        boolean privateM = false;
        Matcher matcher = bold.matcher(text);

        if (matcher.find()) {
            text = text.replace("<b>", "").replace("</b>", "");

            String address = matcher.group(2);
            privateM = address.equalsIgnoreCase(sc2tvNick);
            addressLength = matcher.group(2).length();
        }
        int length = nick.length() + 1;

        Spannable spannable = getLinkedSpan(nick, text);
        if (addressLength > 0){
            spannable.setSpan(bss, length+1, length + 1 + addressLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            if (privateM) {	spannable.setSpan(new ForegroundColorSpan(MyApp.getContext().getResources()
                    .getColor(R.color.private_msg)), length,
                    spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        addSmiles( spannable, length, "\\:s");
        return spannable;
    }

    @Override
    protected Map<String, Bitmap> getSmileMapLink() {
        return smileMap;
    }

    @Override
    public Map<String, Bitmap> getSmileMap() {
        return smileMap;
    }

    @Override
    public void setNickAndPass(String nick, String pass) {
        sc2tvNick = nick;
        sc2tvPass = pass;
    }


    @Override
    public int sendMessage(String channel, String message) {
        if (Sc2tv.sc2tvNick.equalsIgnoreCase(""))
            return SendMessageService.NEED_LOGIN;
        else {

            int count = 0;
            for (Map.Entry<String, Bitmap> entry : smileMap.entrySet()) {

                Matcher matcher = Pattern.compile(entry.getKey()).matcher(message);
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
                   client.execute(post2);
                } catch (ClientProtocolException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                return SendMessageService.MESSAGE_DELIVER_OK;
            }else{
                return SendMessageService.TOO_MUCH_SMILES_SC2TV;
            }
        }

    }

    public HttpPost getSc2tvPost(String name, String pass) {
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

}
