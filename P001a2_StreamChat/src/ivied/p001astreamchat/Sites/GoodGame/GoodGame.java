package ivied.p001astreamchat.Sites.GoodGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    String channel;
    ExecutorService es;
    private final String domain = "goodgame.ru";
    private final String CHAT_URL = "http://www." + domain + "/chat/";
    private final int maxServerNum = 0x1e3;
    private Random randomNum = new Random();

    private String channelWithoutBreakPoints;
    private List<BasicNameValuePair> headersList = new ArrayList<BasicNameValuePair>();
    private WebSocketForGG connection = new WebSocketForGG(this);

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
        es = Executors.newSingleThreadExecutor();
        mFuture = es.submit(channelRun);
    }

    @Override
    public FragmentLoginStandard getFragment() {
        return null;
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
        return 0;
    }

    @Override
    public void getLogin() {

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
        return null;
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
    protected void insertMessage(Message message) {
        super.insertMessage(message);
    }

    private void connectToChannel() {
        String connectUri = getConnectAddress();
        getHeadersList();
        try {
            connection.connect(connectUri);

        } catch (WebSocketException e) {
            e.printStackTrace();
        }

    }

    private List<BasicNameValuePair> getHeadersList() {
        headersList.clear();
        Header[] headers = getGoodGameChannelHeaders();
        BasicNameValuePair headerNameValuePair;
        for( Header header: headers) {
            headerNameValuePair = new BasicNameValuePair(header.getName(), header.getValue());

            headersList.add(headerNameValuePair);

        }
        return headersList;
    }

    private Header[] getGoodGameChannelHeaders() {

        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        HttpGet request = new HttpGet(CHAT_URL + channelWithoutBreakPoints);
        HttpClient client = new DefaultHttpClient() ;
        try {
            HttpResponse response = client.execute(request, localContext);

            return response.getAllHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
