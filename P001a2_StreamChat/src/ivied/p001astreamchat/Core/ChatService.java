package ivied.p001astreamchat.Core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.R;

/**
 * ��������� �������� �������� �����
 * 
 * @author Serv
 * 
 */

public class ChatService extends Service {



	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");

	final String LOG_TAG = "myLogs";
	NotificationManager nm;
	private final IBinder binder = new ChatBinder();

    Map<String, Site> siteCount =new HashMap<String, Site>();
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
		Collection<Site> sites = siteCount.values();
		Iterator<Site> siteIter = sites.iterator();
		Site site ;
		while (siteIter.hasNext()) {
			site = siteIter.next();
			site.destroyLoadMessages();
			Log.d(LOG_TAG, site.toString());

		}


	/*	Collection<IrcClientShow> irc = ircMap.values();
		Iterator<IrcClientShow> ircIter = irc.iterator();
		IrcClientShow ircName = null;
		while (ircIter.hasNext()) {
			ircName = ircIter.next();
			ircName.disconnect();
			Log.d(LOG_TAG, ircName.toString());

		}*/

		super.onDestroy();
		Log.d(LOG_TAG, "MyService onDestroy1");
	}

	public class ChatBinder extends Binder {
		public ChatService getService() {
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

            if (!channelCount.containsKey(fullChannelName)) {
                channelCount.put(fullChannelName,1);
                FactorySite factorySite = new FactorySite();
                Site siteClass = factorySite.getSite(site);
                siteCount.put(fullChannelName, siteClass);
                siteClass.prepareThread(siteClass, c.getString(1));

            }

        }

    }


	public void sendNotify(String channel, FactorySite.SiteName site) {
		if (MainActivity.showNotifyHeaders) {
			Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
			intent.putExtra(MainActivity.CHANNEL, channel);
            intent.putExtra(MainActivity.SITE, site);


            sendBroadcast(intent);
		}

	}

    public void sendPrivateNotify (String message, String channel, FactorySite.SiteName site) {

            if ( !isAppOnForeground(MyApp.getContext())) {

                Intent notificationIntent = new Intent(getBaseContext(), MainActivity.class); // �� ����� �� �����������
                notificationIntent.putExtra(MainActivity.CHANNEL, channel);
                notificationIntent.putExtra(MainActivity.SITE, site);
                NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplication())
                                .setSmallIcon(R.drawable.ic_launcher) //?????? ???????????
                                .setAutoCancel(true) //??????????? ????????? ?? ????? ?? ????
                                .setTicker(message) //?????, ??????? ??????????? ?????? ??????-???? ??? ???????? ???????????
                                .setContentText(message) // ???????? ????? ???????????
                                .setContentIntent(PendingIntent.getActivity(getApplication(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                                .setWhen(System.currentTimeMillis()) //???????????? ????? ???????????
                                .setContentTitle("New private message") //????????? ???????????
                                .setDefaults(Notification.DEFAULT_ALL); // ????, ????? ? ??????? ????????? ???????????? ?? ?????????

                        Notification notification = nb.getNotification(); // ??????????
                        // ???????????
                        nm.notify(0, notification);
                 }
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



}