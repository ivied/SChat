package ivied.p001astreamchat.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sc2tv.Sc2tv;

/**
 * ��������� �������� �������� �����
 * 
 * @author Serv
 * 
 */

public class ChatService extends Service {
	private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";
	final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
	final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");
	SharedPreferences sPref;
	final String LOG_TAG = "myLogs";
	ScheduledExecutorService sEs;
	ExecutorService es;
	NotificationManager nm;
	private final IBinder binder = new ChatBinder();
	Map<String,  Future> channelLink = new HashMap<String, Future>();
	Map<String, IrcClientShow> ircMap = new HashMap<String, IrcClientShow>();
	Map<String, Integer> channelCount = new HashMap <String, Integer>();

	public void onCreate() {
		super.onCreate();
		 nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		loadSavedChats();
		Log.d(LOG_TAG, "MyService onCreate");
	}

	public IBinder onBind(Intent intent) {

		
		return binder;
	}

	public void onRebind(Intent intent) {
		super.onRebind(intent);
		
	}

	public boolean onUnbind(Intent intent) {
		

		return super.onUnbind(intent);

	}

	public void onDestroy() {
		Collection<Future> chats = channelLink.values();
		Iterator<Future> chatsIter = chats.iterator();
		Future chatName = null;
		while (chatsIter.hasNext()) {
			chatName = chatsIter.next();
			chatName.cancel(true);
			Log.d(LOG_TAG, chatName.toString());
			
		}
		Collection<IrcClientShow> irc = ircMap.values();
		Iterator<IrcClientShow> ircIter = irc.iterator();
		IrcClientShow ircName = null;
		while (ircIter.hasNext()) {
			ircName = ircIter.next();
			ircName.disconnect();
			Log.d(LOG_TAG, ircName.toString());
			
		}
		/*shutdownAndAwaitTermination(es);
		shutdownAndAwaitTermination(sEs);*/
		//es.shutdown();
		//sEs.shutdown();
		super.onDestroy();
		Log.d(LOG_TAG, "MyService onDestroy1");
	}

	class ChatBinder extends Binder {
		ChatService getService() {
			return ChatService.this;
		}
	}

	void loadSavedChats() {
		
		Cursor c = getContentResolver().query(SERVICE_URI, new String[] { "chat" }, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			
			String chatName=  c.getString(0);
			startChatThread(chatName);
			
		}
	}

	/**
	 * ��������� ������ ������ �hannelRun � ���������� ����� ������� � (Map)
	 * channelCount
	 * 
	 * @param chatName
	 */
    void startChatThread(String chatName) {
        Cursor c = getContentResolver().query(ADD_URI,
                new String[]{"site", "channel"}, "chat = ?",
                new String[]{chatName}, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            FactorySite.SiteName site = FactorySite.SiteName.valueOf(c.getString(0).toUpperCase());
            String fullChannelName = c.getString(0) + " " + c.getString(1);

            if (channelCount.containsKey(fullChannelName)) {
                int i = channelCount.get(fullChannelName) + 1;
                channelCount.put(fullChannelName, i);
                Log.d(LOG_TAG, fullChannelName + " == " + i);
            } else {
                channelCount.put(fullChannelName, 1);
                channelRun channelRun = new channelRun(FactorySite.SiteName.valueOf(c.getString(0).toUpperCase()),
                        c.getString(1));
                switch (site){
                    case TWITCH:

                    es = Executors.newSingleThreadExecutor();
                    channelLink.put(fullChannelName,
                            es.submit(channelRun));
                        break;

                 case SC2TV:
                    sEs = Executors.newScheduledThreadPool(1);
                    channelLink.put(fullChannelName, sEs
                            .scheduleWithFixedDelay(channelRun, 0, 3,
                                    TimeUnit.SECONDS));
                     break;

                }
            }

        }

    }

	class channelRun implements Runnable {

		FactorySite.SiteName siteName;
		String channel;
		//	String message;

		public channelRun(FactorySite.SiteName site, String channel) {

			this.channel = channel;
			this.siteName = site;
		}

		public void run() {

            switch (siteName){
                case SC2TV:


                FactorySite factorySite = new FactorySite();
                Site site = factorySite.getSite(siteName);
                site.getMessages(channel);
                break;
                case TWITCH:

                try {
                    readTwitch(channel);
                } catch (IrcException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                break;
            }
        }

			

    }

	

	private void readTwitch(String channel) throws IrcException {
		Log.d(LOG_TAG, "readTwitch" + channel);
		// Now start our bot up.
		 int rnd=(int) (1 + Math.random() * 100);
         
		 
		String random ="justinfan"+Integer.toString(rnd);
        IrcClientShow bot = new IrcClientShow(random);
        ircMap.put(channel, bot);
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        
        try {
			bot.connect("199.9.250.229", 6667, "");
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Join the #pircbot channel.
        bot.joinChannel("#" + channel);
       
	}


	public void sendNotify(String channel, String message, String site) {
		if (getPrefsNotifHeaders()) {
			Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
			intent.putExtra(MainActivity.CHAT_NAME, channel);
			sendBroadcast(intent);
		}
		// 2-� �����
		// notif.setLatestEventInfo(this, "Notification's title",
		// "Notification's text", pIntent);
		if (getPrefsNotifSystem()) {
			if (site.equalsIgnoreCase("sc2tv")
					&& !isAppOnForeground(MyApp.getContext())) {
				Matcher matcher = bold.matcher(message);

				if (matcher.find()) {
					message = message.replace("<b>", "").replace("</b>", "");
					String privateNick = SendMessageService.sc2tvNick;
					String adress = matcher.group(2);
					if (adress.equalsIgnoreCase(privateNick)) {
				 Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class); // �� ����� �� ����������� 
				 notificationIntent.putExtra(MainActivity.CHAT_NAME, channel);
	   
				 NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplication())
				 .setSmallIcon(R.drawable.ic_launcher) //������ �����������
				 .setAutoCancel(true) //����������� ��������� �� ����� �� ����
				 .setTicker(message) //�����, ������� ����������� ������ ������-���� ��� �������� �����������
				 .setContentText(message) // �������� ����� �����������
				 .setContentIntent(PendingIntent.getActivity(getApplication(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
				 .setWhen(System.currentTimeMillis()) //������������ ����� �����������
        		.setContentTitle("New private message") //��������� �����������
        		.setDefaults(Notification.DEFAULT_ALL); // ����, ����� � ������� ��������� ������������ �� ���������
				 
						Notification notification = nb.getNotification(); // ����������
																			// �����������
						nm.notify(0, notification);
					}
				}
			}

		}

	}
	
	private boolean getPrefsNotifHeaders() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(MyApp.getContext());
        boolean notifHeaders = prefs.getBoolean("notifHeaders", true);
         
        
        return notifHeaders;
	}
	private boolean getPrefsNotifSystem() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(MyApp.getContext());
        
         boolean notifSystem = prefs.getBoolean("notifSystem", true);
        
        return notifSystem;
	}
	
	private boolean isAppOnForeground(Context context) {
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    if (appProcesses == null) {
	      return false;
	    }
	    final String packageName = context.getPackageName();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	      if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
	        return true;
	      }
	    }
	    return false;
	  }



	public class IrcClientShow extends PircBot {
		public IrcClientShow(String name) {
			this.setName(name);

		}

	

		public void onMessage(String channel, String sender, String login,
				String hostname, String message) {
			//notificationAdd(channel);
			
			ContentValues cv = new ContentValues();
			cv.put("site", "twitch");
			channel = channel.substring(1);
			
			cv.put("channel", channel);
			cv.put("nick", sender);
			cv.put("message", message);
			long unixTime = System.currentTimeMillis() / 1000L;
				cv.put("time", unixTime);
				cv.put("identificator", "");
			//TODO ������ insert ignore
				Cursor customCursor = getCursor("twitch" , channel);
				String personal = personalSet(customCursor);
				if (personal.equalsIgnoreCase("")) personal = channel;
				cv.put("personal", personal);
				cv.put("color",colorSet(customCursor));
			getContentResolver().insert(
					INSERT_URI, cv);
			// TODO Auto-generated method stub
			Log.d(MainActivity.LOG_TAG, message);
			sendNotify(channel,message,"twitch");
		}
	}
	
	private Cursor getCursor (String site , String channel) {
		Cursor c = getContentResolver()
				.query(ADD_URI, new String [] { "color","personal"}, "site = ? AND channel = ?",
				new String [] { site,channel}, null);
		return c;
	}

	private String personalSet (Cursor c) {

		String personal = "";

		if (c.moveToNext()){personal = c.getString(1);}

		return personal;
	}

	private int colorSet (Cursor c) {
		int color = -111111;
		if (c.moveToFirst()){color = c.getInt(0);}

		return color;
	}

}