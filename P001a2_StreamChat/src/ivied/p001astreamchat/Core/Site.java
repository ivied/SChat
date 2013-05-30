package ivied.p001astreamchat.Core;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Serv on 29.05.13.
 */
public abstract class Site {


    ChatService chatService;
    boolean bound =false;

    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");

    abstract  public void readChannel(String channel);
    abstract  protected String getSite();


    public void getMessages (String channel){
        Intent intent = new Intent(MyApp.getContext(), ChatService.class);
        MyApp.getContext().bindService(intent, sConn, 0);
        readChannel(channel);
        MyApp.getContext().unbindService(sConn);
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

    protected void insertMessage ( FactorySite.SiteName site, String channel, String nick, String message, String id, long time) {
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

        chatService.sendNotify(cv.getAsString("channel"), cv.getAsString("message"), "sc2tv");

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
