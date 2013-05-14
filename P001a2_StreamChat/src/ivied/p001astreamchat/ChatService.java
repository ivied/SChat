package ivied.p001astreamchat;

import ivied.p001astreamchat.MainActivity.TabInfo;

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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

/**
 * Управляет потоками парсинга чатов
 * 
 * @author Serv
 * 
 */

public class ChatService extends Service {
	private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";
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
		Iterator chatsIter = chats.iterator();
		Future chatName = null;
		while (chatsIter.hasNext()) {
			chatName = (Future) chatsIter.next();
			chatName.cancel(true);
			Log.d(LOG_TAG, chatName.toString());
			
		}
		Collection<IrcClientShow> irc = ircMap.values();
		Iterator ircIter = irc.iterator();
		IrcClientShow ircName = null;
		while (ircIter.hasNext()) {
			ircName = (IrcClientShow) ircIter.next();
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
	 * запускает потоки класса сhannelRun и записывает карту потоков в (Map)
	 * channelCount
	 * 
	 * @param chatName
	 */
	void startChatThread(String chatName) {
		Cursor c = getContentResolver().query(ADD_URI,
				new String[] { "site", "channel" }, "chat = ?",
				new String[] { chatName }, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String site = c.getString(0);
			String fullChannelName = c.getString(0) + " " + c.getString(1);

			if (channelCount.containsKey(fullChannelName)) {
				int i = channelCount.get(fullChannelName) + 1;
				channelCount.put(fullChannelName, i);
				Log.d(LOG_TAG, fullChannelName + " == " + i);
			} else {
				channelCount.put(fullChannelName, 1);
				channelRun channelRun = new channelRun(c.getString(0),
							c.getString(1));
				if (site.equals("twitch")) {
					es = Executors.newSingleThreadExecutor();
					channelLink.put(fullChannelName, 
					es.submit(channelRun));
				
				} else {

					sEs = Executors.newScheduledThreadPool(1);
					
					channelLink.put(fullChannelName, sEs
							.scheduleWithFixedDelay(channelRun, 0, 3,
									TimeUnit.SECONDS));
					
				}
			}

		}

	}

	class channelRun implements Runnable {

		String site;
		String channel;
		String message;

		public channelRun(String site, String channel) {
			this.channel = channel;
			this.site = site;
		}

		public void run() {

			if (site.equals("sc2tv")) {
				readSc2tvchannel(channel);
			}
			if (site.equals("twitch")) {
				Log.d(LOG_TAG, "here!");
				readTwitch(channel);
			}

			

		}

	
	}
	private void readTwitch(String channel) {
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
	/**
	 * считываем каналы sc2tv
	 * 
	 * @param channel
	 * @return
	 */
	void readSc2tvchannel(String channel) {
		
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

			} else {

			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		try {

			JSONObject jsonObj = new JSONObject(builder.toString());

			JSONArray jsonArray = jsonObj.getJSONArray("messages");

			int i = 0;
			insertSc2tv(jsonArray, i, channel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * метод рекурсивно парсит json строку добавляя в БД только новые сообщения
	 * 
	 * @param jsonArray
	 * @param i
	 * @param channel
	 */
	private void insertSc2tv(JSONArray jsonArray, int i, String channel) {
		try {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String[] selectionArgs = new String[] { jsonObject.getString("id") };
			Cursor c = getContentResolver().query(
					INSERT_URI, null,
					"identificator = ?", selectionArgs, null);
			if (c.getCount() == 0) {

				i++;
				insertSc2tv(jsonArray, i, channel);
				i--;
				//добавление оповещений
				
			
				
				
				ContentValues cv = new ContentValues();
				cv.put("site", "sc2tv");
				cv.put("channel", channel);
				cv.put("nick", jsonObject.getString("name"));
				cv.put("message", jsonObject.getString("message"));
				try {

					final SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					final Date d = df.parse(jsonObject.getString("date"));

					cv.put("time", d.getTime() / 1000);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}

				cv.put("identificator", jsonObject.getString("id"));
				//TODO инсерт insert ignore
				Uri newUri = getContentResolver().insert(
						INSERT_URI, cv);
				
				c = getContentResolver().query(
						INSERT_URI, null, null,
						null, null);
				Log.i(LOG_TAG, "записей стало " + c.getCount());
				sendNotif(channel, jsonObject.getString("message"));
				//notificationAdd(channel);
				/*
				 * if (c.moveToFirst()) { String str; do { str = ""; for (String
				 * cn : c.getColumnNames()) { str = str.concat(cn + " = " +
				 * c.getString(c.getColumnIndex(cn)) + "; "); } Log.d(LOG_TAG,
				 * str);
				 * 
				 * } while (c.moveToNext());}
				 */

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void sendNotif(String channel, String message) {
		Notification notif = new Notification(R.drawable.ic_launcher, "New message", 
			      System.currentTimeMillis());
		Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
	    intent.putExtra(MainActivity.CHAT_NAME, channel);
	    sendBroadcast(intent);
/*	    // 2-я часть
	    notif.setLatestEventInfo(this, "Notification's title", "Notification's text", pIntent);
	    
	    // ставим флаг, чтобы уведомление пропало после нажатия
	    notif.flags |= Notification.FLAG_AUTO_CANCEL;
	    
	    // отправляем
	    nm.notify(1, notif);
		// TODO Auto-generated method stub
		*/
	}

	void shutdownAndAwaitTermination(ExecutorService pool) {
		   pool.shutdown(); // Disable new tasks from being submitted
		   try {
		     // Wait a while for existing tasks to terminate
		     if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
		       pool.shutdownNow(); // Cancel currently executing tasks
		       // Wait a while for tasks to respond to being cancelled
		       if (!pool.awaitTermination(10, TimeUnit.SECONDS))
		           System.err.println("Pool did not terminate");
		     }
		   } catch (InterruptedException ie) {
		     // (Re-)Cancel if current thread also interrupted
		     pool.shutdownNow();
		     // Preserve interrupt status
		     Thread.currentThread().interrupt();
		   }
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
			sendNotif(channel,message);
			cv.put("channel", channel);
			cv.put("nick", sender);
			cv.put("message", message);
			long unixTime = System.currentTimeMillis() / 1000L;
				cv.put("time", unixTime);
				cv.put("identificator", "");
			//TODO инсерт insert ignore
			Uri newUri = getContentResolver().insert(
					INSERT_URI, cv);
			// TODO Auto-generated method stub
			Log.d(MainActivity.LOG_TAG, message);
			
		}
	}

}