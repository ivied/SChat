package ivied.p001astreamchat.Sites.Twitch;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.SendMessageService;
import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Login.LoginException;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Message;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 30.05.13.
 */
public class Twitch  extends Site {

    final String TWITCH_SMILE_ADDRESS = "https://api.twitch.tv/kraken/chat/emoticons";
    final String TWITCH_SMILE_HEADER_UPDATE = "x-api-version";
    public static IrcClient botSend;
    public static String twitchNick;
    public static String twitchPass;

    ExecutorService es;
    IrcClientShow bot;
    private static Map<String, Bitmap> smileMap = new HashMap<String, Bitmap>();
    private static Map<String, String> unrecognizedSmiles = new HashMap<String, String>() ;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("\\&lt\\;3", "<3");
        aMap.put("\\&gt\\;\\(", ">\\(");
        unrecognizedSmiles = Collections.unmodifiableMap(aMap);
    }

    @Override
    public Drawable getLogo() {
        return MyApp.getContext().getResources().getDrawable(R.drawable.twitch);
    }
    @Override
    public void readChannel(String channel) {
            int rnd=(int) (1 + Math.random() * 100);
            String random ="justinfan"+Integer.toString(rnd);
            bot = new IrcClientShow(random);

            bot.setVerbose(true);
            try {
                bot.connect("199.9.250.229", 6667, "");
            } catch (NickAlreadyInUseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }

            // Join the #pircbot channel.
            bot.joinChannel("#" + channel);


    }

    @Override
    public void startThread(ChannelRun channelRun) {

        es = Executors.newSingleThreadExecutor();
        mFuture = es.submit(channelRun);

    }


    @Override
    public void destroyLoadMessages () {
        bot.disconnect();
        mFuture.cancel(true);
    }

    @Override
    public int sendMessage(String channel, String message) {
        if (Twitch.twitchNick.equalsIgnoreCase("")){
            return SendMessageService.NEED_LOGIN;
        }else{
            try {
                botSend.reconnect();
            } catch (NickAlreadyInUseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }
            botSend.sendMessage("#" + channel, message);
            return 0;
        }
    }

    @Override
    public FactorySite.SiteName getSiteEnum() {
        return FactorySite.SiteName.TWITCH;
    }

    @Override
    protected boolean isPrivateMessage(String message) {
        return false;
    }

    @Override
    public Spannable getSmiledText(String text, String nick) {
        int length = nick.length() + 1;
        Spannable spannable = getLinkedSpan( nick , text);
        addSmiles( spannable, length, "");
        return spannable;
    }

    @Override
    public Map<String, Bitmap> getSmileMap() {
        return smileMap;
    }



    @Override
    public void setNickAndPass(String nick, String pass) {
        twitchNick =nick;
        twitchPass = pass;
    }


    public class IrcClientShow extends PircBot {
        public IrcClientShow(String name) {
            this.setName(name);

        }

        public void onMessage(String channel, String sender, String login,
                              String hostname, String text) {
            long unixTime = System.currentTimeMillis() / 1000L;
            channel = channel.substring(1);
            Message message = new Message(channel, sender, text, null, unixTime);
            insertMessage ( message );

        }
    }


    @Override
    public FragmentLoginStandard getFragment() {
        return new FragmentLoginTwitch();
    }

    @Override
    public FragmentAddChannelStandard getFragmentAddChannel() {
        return new FragmentFindChannelTwitch();
    }

    @Override
    public int getColorForAdd(String channel, int length, int color) {

        if ((length>3)){
            try {
                String text = stringToHex (channel).substring(0, 6);
                Log.d(MainActivity.LOG_TAG, "color = "+ text);
                return Color.parseColor("#" + text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return color;
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    public String stringToHex(String input) throws UnsupportedEncodingException
    {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }



    private String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    @Override
    public void getLogin() throws LoginException{

        super.getLogin();

        Twitch.botSend = new IrcClient(twitchNick);

        try {
            botSend.connect("199.9.250.229", 6667, twitchPass);
        } catch (NickAlreadyInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSmileAddress() {
        return TWITCH_SMILE_ADDRESS;
    }

    @Override
    public String getSmileModifyHeader() {
        return TWITCH_SMILE_HEADER_UPDATE;
    }

    @Override
    public void getSiteSmiles(String header) {
        JSONObject smile ;
        JSONObject image ;
        JSONArray imageList;
        HttpGet httpGet = new HttpGet(getSmileAddress());
        StringBuilder builderJson = jsonRequest(httpGet);
        JSONArray jsonArray ;
        try {
            JSONObject jsonObj = new JSONObject(builderJson.toString());

            jsonArray = jsonObj.getJSONArray("emoticons");

            for(int i = 0 ; i < jsonArray.length(); i++ ){
                smile = jsonArray.getJSONObject(i);
                imageList = smile.getJSONArray("images");
                image = imageList.getJSONObject(0);
                if(image.getString("emoticon_set").equalsIgnoreCase("null")){
                    numberOfSmiles++;

                }
            }
            for(int i = 0 ; i < jsonArray.length(); i++ ){
                smile = jsonArray.getJSONObject(i);
                imageList = smile.getJSONArray("images");
                image = imageList.getJSONObject(0);
                if(image.getString("emoticon_set").equalsIgnoreCase("null")){

                    String imageAddress = image.getString("url");
                    String width = image.getString("width");
                    String height = image.getString("height");
                    String regex = smile.getString("regex");
                    Log.i(MainActivity.LOG_TAG, regex);
                    if (unrecognizedSmiles.containsKey(regex)) regex = unrecognizedSmiles.get(regex);
                    PutSmile putSmile = new PutSmile(imageAddress,regex,width,height,this,header);
                    putSmile.run();
                    Log.i (MainActivity.LOG_TAG, "regex = " + regex + ", i = " + i + "   " + jsonArray.length());}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        /*cv.put(MyContentProvider.SMILES_SITE, getSiteName());
        for (String smile: sc2tvSmileJson){
            String [] smileArray = smile.split(": '");
            smileAddress = getSmileSubstring (smileArray, SC2TV_SMILE_FIELD_ADDRESS);

            try {
                byte[] smileByte = SmileHelper.urlToImageBLOB(SC2TV_STANDARD_SMILE_WAY + smileAddress);
                cv.put(MyContentProvider.SMILES_SMILE, smileByte );
            } catch (IOException e) {
                e.printStackTrace();
            }
            cv.put(MyContentProvider.SMILES_REGEXP, getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_REGEXP));
            cv.put(MyContentProvider.SMILES_WIDTH, getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_WIDTH));
            cv.put(MyContentProvider.SMILES_HEIGHT, getSmileSubstring(smileArray, SC2TV_SMILE_FIELD_HEIGHT));
            MyApp.getContext().getContentResolver().insert(MyContentProvider.SMILE_INSERT_URI, cv);
        }*/
    }

    @Override
    public int getMiniLogo() {
        return R.drawable.twitch_small;
    }


}
