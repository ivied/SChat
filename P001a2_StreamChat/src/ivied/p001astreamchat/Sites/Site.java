package ivied.p001astreamchat.Sites;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;

import com.actionbarsherlock.app.SherlockFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.ChatView.ActionProviderLink;
import ivied.p001astreamchat.ChatView.AdapterChatCursor;
import ivied.p001astreamchat.Core.ChatService;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.MyContentProvider;
import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.R;

/**
 * Created by Serv on 29.05.13.
 */
public abstract class Site {


    public ChatService chatService;
    boolean bound =false;
    public Future mFuture;
    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");

    abstract public Drawable getLogo();
    abstract public void readChannel(String channel);
    abstract public void startThread (ChannelRun channelRun);
    abstract public FragmentLoginStandard getFragment ();
    abstract public FragmentAddChannelStandard getFragmentAddChannel();
    abstract public int getColorForAdd (String channel, int length , int color);
    abstract public int sendMessage(String channel, String message);
    abstract public void getLogin();
    abstract public String getSmileAddress();
    abstract public String getSmileModifyHeader();
    abstract public void getSiteSmiles();
    abstract public int getMiniLogo();
    abstract public  FactorySite.SiteName getSiteEnum();
    abstract public  Spannable getSmiledText(String text, String nick);
    abstract protected  Map<String,Bitmap> getSmileMapLink();
    abstract public  Map<String,Bitmap> getSmileMap();
    protected static final Spannable.Factory spannableFactory = Spannable.Factory
            .getInstance();

    public void destroyLoadMessages(){
        mFuture.cancel(true);
    }

    public void prepareThread (Site site, String channel) {


        ChannelRun channelRun = new ChannelRun(site, channel);
        startThread(channelRun);

    }
    public  String getSiteName(){
       return  getSiteEnum().name();
    }




    protected class ChannelRun implements Runnable {

        Site siteClass;
        String channel;
        //	String message;

        public ChannelRun(Site site, String channel) {

            this.channel = channel;
            this.siteClass = site;
        }

        public void run() {
            Intent intent = new Intent(MyApp.getContext(), ChatService.class);

            MyApp.getContext().bindService(intent, sConn, 0);


            siteClass.readChannel(channel);
            MyApp.getContext().unbindService(sConn);
        }




    }



    private ServiceConnection sConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatService.ChatBinder binder = (ChatService.ChatBinder) service;
            chatService = binder.getService();

            bound = true;

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

            bound = false;
        }
    };

    protected void insertMessage ( String channel, String nick, String message, String id, long time) {
        FactorySite.SiteName site= getSiteEnum();
        ContentValues cv = new ContentValues();
        cv.put("site", site.name());
        cv.put("channel", channel);
        cv.put("nick", nick);
        cv.put("message", message);
        cv.put("time", time);

        cv.put("identificator", id);
        //TODO ?????? insert ignore
        Cursor customCursor = getCursorFromChannelDB(site.name(), channel);
        String personal = personalSet(customCursor);
        if (personal.equalsIgnoreCase("")) personal = channel;
        cv.put("personal", personal);

        cv.put("color",colorSet(customCursor));
        MyApp.getContext().getContentResolver().insert(
                INSERT_URI, cv);
        customCursor.close();
        chatService.sendNotify(cv.getAsString("channel"), site);

    }

    public HttpResponse getResponse(HttpUriRequest request) {
        HttpResponse response = null;

        try {
            HttpClient client = new DefaultHttpClient();
            response = client.execute(request);
            client.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {


        } catch (IOException e) {


        }

        return response;
    }

    public StringBuilder jsonRequest(HttpGet httpGet) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();



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

    protected Cursor getCursorFromChannelDB (String site, String channel) {
        Cursor c = MyApp.getContext().getContentResolver()
                .query(ADD_URI, new String[]{"color", "personal"}, "site = ? AND channel = ?",
                        new String[]{site, channel}, null);

        return c;

    }
    protected String personalSet (Cursor c) {

        String personal = "";

        if (c.moveToNext()){personal = c.getString(1);}

        return personal;
    }

    protected int colorSet (Cursor c) {
        int color = -111111;
        if (c.moveToFirst()){color = c.getInt(0);}

        return color;
    }

    protected void putSmile ( String smile ,String regexp, String width, String height) {
        ContentValues cv = new ContentValues();
        cv.put(MyContentProvider.SMILES_SITE, getSiteName());
        try {
            byte[] smileByte = SmileHelper.urlToImageBLOB(smile);
            cv.put(MyContentProvider.SMILES_SMILE, smileByte );
        } catch (IOException e) {
            e.printStackTrace();
        }
        cv.put(MyContentProvider.SMILES_REGEXP, regexp);
        cv.put(MyContentProvider.SMILES_WIDTH, width);
        cv.put(MyContentProvider.SMILES_HEIGHT, height);
        MyApp.getContext().getContentResolver().insert(MyContentProvider.SMILE_INSERT_URI, cv);
    }

    public void setSmileMaps() {
         Map<String, Bitmap> smileMap = getSmileMapLink();
        String selection = "site = ?";
        String [] selectionArgs = {getSiteName()};
        Cursor c = MyApp.getContext().getContentResolver().query(MyContentProvider.SMILE_INSERT_URI , null, selection, selectionArgs, null );
        if (c.getCount()!=0){
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                String regexp = c.getString(3);
                Bitmap smile = SmileHelper.getImageFromBLOB(c.getBlob(2));
                smileMap.put(regexp, smile);

            }
        }
        c.close();
    }



    protected String getLinks(String message){
        if(MainActivity.messageLinksShow){
            Matcher matcher = ActionProviderLink.URL.matcher(message);
            while (matcher.find()){

                AdapterChatCursor.linkMap.add(matcher.start());
                message = message.replace(matcher.group(), "link");
                matcher = ActionProviderLink.URL.matcher(message);
            }
        }
        return message;
    }

    protected void getLinkedSpan(Spannable spannable, int length) {
        for (Integer startOfLink : AdapterChatCursor.linkMap){
            spannable.setSpan(new UnderlineSpan(), length + 1 + startOfLink ,
                    length + 1  + startOfLink + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(MyApp.getContext().getResources()
                    .getColor(R.color.link)), length + 1 + startOfLink,
                    length + 1  + startOfLink + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

    }


    public void addSmiles( Spannable spannable,  int length, String perfix) {

        Map<String, Bitmap> smileMap = new HashMap (getSmileMapLink());
        if (MainActivity.showSmiles){

            for (Map.Entry<String, Bitmap> entry : smileMap.entrySet()) {

                Matcher matcher = Pattern.compile(perfix + entry.getKey()).matcher(spannable);
                while (matcher.find()) {



                    for (ImageSpan span : spannable.getSpans(matcher.start(),
                            matcher.end(), ImageSpan.class))
                        if (spannable.getSpanStart(span) >= matcher.start()
                                && spannable.getSpanEnd(span) <= matcher.end()){
                            spannable.removeSpan(span);
                        }



                    spannable.setSpan(new ImageSpan(MyApp.getContext(), entry.getValue()),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        }

        spannable.setSpan(new ForegroundColorSpan(MyApp.getContext().getResources()
                .getColor(R.color.nick)), 0, length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }


}
