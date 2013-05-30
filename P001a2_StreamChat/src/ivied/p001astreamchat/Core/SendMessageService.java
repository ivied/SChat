package ivied.p001astreamchat.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
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
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import ivied.p001astreamchat.R;

public class SendMessageService extends Service {
	private Handler handler = new Handler();
	
	private final IBinder binder = new SendBinder();
	SharedPreferences preferences;
	public String token=null;
	HttpClient client = new DefaultHttpClient();
	IrcClient bot;
	public static String sc2tvNick;
	public static String twitchNick;
	public void onCreate() {
		super.onCreate();
		//es = Executors.newFixedThreadPool(1);
		login ();
		Log.i(MainActivity.LOG_TAG, "MyService onCreate lololololololol");
		
	}

	

	public IBinder onBind(Intent intent) {

		Log.d(MainActivity.LOG_TAG, "MyService onBind");
		return binder;
	}

	public void onRebind(Intent intent) {
		super.onRebind(intent);
		Log.d(MainActivity.LOG_TAG, "MyService onRebind");
	}

	public boolean onUnbind(Intent intent) {
		Log.d(MainActivity.LOG_TAG, "MyService onUnbind");

		return super.onUnbind(intent);

	}

	public void onDestroy() {
	
		bot.disconnect();
		super.onDestroy();
		Log.d(MainActivity.LOG_TAG, "MyService onDestroy");
	}

	class SendBinder extends Binder {
		SendMessageService getService() {
			return SendMessageService.this;
		}
	}
	
	class LoadLogins extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... a) {
			// TODO Auto-generated method stub\
			
			SharedPreferences preferences;
			final String TWITCH_SAVED_NAME = "twitch";
			final String TWITCH_SAVED_PASS = "twitchpass";
		
			final String SAVED_NAME = "sc2tv";
			final String SAVED_PASS = "sc2tvpass";
			preferences = getApplicationContext().getSharedPreferences("Login",
					0);
			
			
			String name = preferences.getString(TWITCH_SAVED_NAME, "");
			twitchNick = preferences.getString(TWITCH_SAVED_NAME, "");
			String pass = preferences.getString(TWITCH_SAVED_PASS, "");
			bot = new IrcClient(name);
	        
	        // Enable debugging output.
	        //bot.setVerbose(true);
	        
	        // Connect to the IRC server.
	        try {
				bot.connect("199.9.250.229", 6667, pass );
				Log.i(MainActivity.LOG_TAG, "connecting= " + bot.isConnected());
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
	       
	        
			
			
			 name = preferences.getString(SAVED_NAME, "");
			 sc2tvNick = preferences.getString(SAVED_NAME, "");
			 pass = preferences.getString(SAVED_PASS, "");

			Log.i(MainActivity.LOG_TAG, name + " " + pass);
			HttpPost post = new HttpPost("http://sc2tv.ru/");
			try {
				// post
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						4);
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("pass", pass));
				nameValuePairs.add(new BasicNameValuePair("op", "¬ход"));
				nameValuePairs.add(new BasicNameValuePair("form_id",
						"user_login_block"));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				client = new DefaultHttpClient();
				HttpResponse response = client.execute(post);
				Header[] headers = response.getAllHeaders();

				HttpGet httpGet = new HttpGet(
						"http://chat.sc2tv.ru/gate.php?task=GetUserInfo&ref=http://sc2tv.ru/");
				StringBuilder builder = new StringBuilder();
				response = client.execute(httpGet);

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				Log.i(MainActivity.LOG_TAG, builder.toString());

				try {
					JSONObject jsonObj = new JSONObject(builder.toString());

					token = jsonObj.getString("token");
					Log.i(MainActivity.LOG_TAG, "tokenNow = " + token);
				
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block
			} catch (IOException e) {

				// TODO Auto-generated catch block
			}
			return null;
		}
	}

	

	class SendMessage extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... a) {
			final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
			String chatName = a[0];
			String [] projection =  new String[] { "site", "channel"}; 
			String [] selectionArgs = new String [] {chatName, "true"};
			Cursor c = getContentResolver().query
					(ADD_URI, projection, "chat = ? AND flag = ?", selectionArgs, null);
			if (c.getCount()== 0) sendToast (R.string.notify_channels_not_set);
	          
	        // Toast.makeText(getApplicationContext(), "" + getResources().getString(R.string.notify_channels_not_set) , Toast.LENGTH_SHORT).show();
			for (c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
				String site = c.getString(0);
				String channel = c.getString(1);
				if (site.equalsIgnoreCase("sc2tv")){
					if (sc2tvNick.equalsIgnoreCase(""))
						sendToast(R.string.toast_login_to_sc2tv);
					else {

						int count = 0;
						for (Entry<Pattern, Integer> entry : AdapterChatCursor.emoticons
								.entrySet()) {

							Matcher matcher = entry.getKey().matcher(a[1]);
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
									"message", a[1]));
							nameValuePairs.add(new BasicNameValuePair(
									"channel_id", channel));
							nameValuePairs.add(new BasicNameValuePair("token",
									token));
							HttpPost post2 = new HttpPost(
									"http://chat.sc2tv.ru/gate.php");

							try {
								post2.setEntity(new UrlEncodedFormEntity(
										nameValuePairs, HTTP.UTF_8));
								client.execute(post2);
							} catch (ClientProtocolException e) {

								e.printStackTrace();
							} catch (IOException e) {

								e.printStackTrace();
							}
							// TODO делать рекконект если сообщение не
							// доставленно
						}else{
							sendToast(R.string.toast_sc2tv_much_smiles);
						}
					}

				}
				if (site.equalsIgnoreCase("twitch")){
					if (twitchNick.equalsIgnoreCase(""))
						sendToast(R.string.toast_login_to_twitch);else{
					Log.i(MainActivity.LOG_TAG, channel + " message " +a[1]);
					try {
						bot.reconnect();
						
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
					bot.sendMessage("#"+channel, a[1]);
						}
				}
			}
			
				

				
			
			return null;
		}
	}

	public void login() {
		LoadLogins login = new LoadLogins();
		login.execute();
		
		
		
		
	}

	public void sendMessage(String text, String currentTabTag) {
		SendMessage send = new SendMessage();
		send.execute(currentTabTag, text);

	}

	private void sendToast(final int stringId){
		handler.post(new Runnable() {
            public void run() {
          	  Toast.makeText(getApplicationContext(), "" + getResources().getString(stringId) , Toast.LENGTH_SHORT).show();
               
            }
	
	
	 });
	}
	

}
