package ivied.p001astreamchat.Sites.GoodGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Message;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 24.06.13.
 */
public class GoodGame extends Site {

    public static String GGNick;
    public static String GGPassWord;
    String channel;
    String channelWithoutBreakPoints;
    int channelID;
    static String GGUserID;
    ExecutorService es;
    private final String domain = "goodgame.ru";
    final String CHAT_URL = "http://www." + domain + "/chat/";
    final String LOGIN_URL = "http://" + domain + "/ajax/login/";
    private final int maxServerNum = 0x1e3;
    private Random randomNum = new Random();
    private static Map<String, WebSocketForGG> ggChanelMap =  new HashMap<>();

    private WebSocket webSocket;
    @Override
    public Drawable getLogo() {
        return MyApp.getContext().getResources().getDrawable(R.drawable.goodgame);
    }

    @Override
    public void readChannel(String channel) {
            this.channel = channel;
            this.channelWithoutBreakPoints = channel.replace(".","");

            connectToChannel();
    }


    @Override
    public void startThread(ChannelRun channelRun) {
        webSocket  = new WebSocketConnection();
        es = Executors.newSingleThreadExecutor();
        mFuture = es.submit(channelRun);
    }

    @Override
    public FragmentLoginStandard getFragment() {
        return new FragmentLoginGoodGame();
    }

    @Override
    public FragmentAddChannelStandard getFragmentAddChannel() {
        return new FragmentFindChannelGoodGame();
    }

    @Override
    public int getColorForAdd(String channel, int length, int color) {
        return Color.CYAN;
    }

    @Override
    public int sendMessage(String channel, String message) {
        try{
        WebSocketForGG socket = ggChanelMap.get(channel);
        socket.sendMessage(message);
        }catch (RuntimeException e){
            //cant find channel
        }
        return 0;
    }

    static DefaultHttpClient httpClient;

    @Override
    public void getLogin() {
        super.getLogin();
        synchronized (WebSocketForGG.waitLoginLocking){
            httpClient = new DefaultHttpClient();
            try {
                BasicHttpContext httpContext = new BasicHttpContext();
                CookieStore cookieStore      = httpClient.getCookieStore();
                httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                httpClient.execute(new HttpGet("http://www." + domain), httpContext);
                setLoginCookies(cookieStore);
                HttpPost postLogin = new HttpPost(LOGIN_URL);
                setPostLoginData(postLogin);
                httpClient.execute(postLogin, httpContext);
                GGUserID = cookieStore.getCookies().get(7).getValue();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e){
                //user and password not set
            }
           WebSocketForGG.ready = true;
           WebSocketForGG.waitLoginLocking.notifyAll();
        }
    }

    @Override
    public String getSmileAddress() {
        return null;
    }

    @Override
    public String getSmileModifyHeader() {
        return null;
    }

    @Override
    public void getSiteSmiles(String header) {

    }

    @Override
    public int getMiniLogo() {
        return R.drawable.goodgame;
    }

    @Override
    public FactorySite.SiteName getSiteEnum() {
        return FactorySite.SiteName.GOODGAME;
    }

    @Override
    public Spannable getSmiledText(String text, String nick) {
        Spannable spannable = spannableFactory.newSpannable(nick + ": " + text);
        return spannable;
    }

    @Override
    protected Map<String, Bitmap> getSmileMapLink() {
        return null;
    }

    @Override
    public Map<String, Bitmap> getSmileMap() {
        return null;
    }

    @Override
    public void setNickAndPass(String nick, String pass) {
        GGNick = nick;
        GGPassWord = pass;
    }

    @Override
    protected void insertMessage(Message message) {
        super.insertMessage(message);
    }

    @Override
    public  void sendToast(String toast) {
        super.sendToast(toast);
    }

    @Override
    public void destroyLoadMessages() {
        webSocket.disconnect();
    }



    private void connectToChannel() {
        WebSocketForGG connection = new WebSocketForGG(this, webSocket);
        ggChanelMap.put(channel, connection);
        String connectUri = getConnectAddress();

        try {
            connection.connect(connectUri);

        } catch (WebSocketException e) {
            e.printStackTrace();
        }

    }

    private void setPostLoginData(HttpPost httpPost) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.clear();
        nameValuePairs.add(new BasicNameValuePair("login", GGNick));
        nameValuePairs.add(new BasicNameValuePair("password", GGPassWord));
        nameValuePairs.add(new BasicNameValuePair("remember", "1"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
    }

    private void setLoginCookies(CookieStore mCookieStore) {
        BasicClientCookie cookie = new BasicClientCookie("fixed", "1");
        cookie.setDomain(domain);
        mCookieStore.addCookie(cookie) ;
        cookie = new BasicClientCookie("auto_login_name", GGNick);
        cookie.setDomain(domain);
        mCookieStore.addCookie(cookie ) ;

    }


    private String getConnectAddress() {

        return String.format("ws://%s:443/chat/%s/%s/websocket", domain, getRandomNumberWithLeadingZeros(), getRandomString());
    }


    private String getRandomNumberWithLeadingZeros()
    {
        return String.format("%03d", randomNum.nextInt(maxServerNum));
    }

    private String getRandomString()
    {

        String[] chars = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++)
            builder.append(chars[random.nextInt(chars.length - 1)]);

        return builder.toString();
    }



}
