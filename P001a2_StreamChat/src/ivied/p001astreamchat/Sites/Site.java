package ivied.p001astreamchat.Sites;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.IBinder;

import java.util.concurrent.Future;

import ivied.p001astreamchat.Core.ChatService;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Login.FragmentLoginStandard;

/**
 * Created by Serv on 29.05.13.
 */
public abstract class Site {


    public ChatService chatService;
    boolean bound =false;
    public Future mFuture;
    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");

    abstract  public void readChannel(String channel);
    abstract  public void startThread (ChannelRun channelRun);
    abstract  public FragmentLoginStandard getFragment ();
    public abstract FactorySite.SiteName getSiteEnum();
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

        chatService.sendNotify(cv.getAsString("channel"), site);

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

}
