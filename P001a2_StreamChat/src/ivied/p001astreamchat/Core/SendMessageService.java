package ivied.p001astreamchat.Core;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.Twitch.Twitch;

public class SendMessageService extends Service {
	private Handler handler = new Handler();
    FactorySite factorySite = new FactorySite();
	private final IBinder binder = new SendBinder();
    public static final int NEED_LOGIN = 1;
    public static final int MESSAGE_DELIVER_OK = 0;
    public static final int TOO_MUCH_SMILES_SC2TV = 2;
    public void onCreate() {
		super.onCreate();
		login ();

		
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
	
		Twitch.botSend.disconnect();
		super.onDestroy();
		Log.d(MainActivity.LOG_TAG, "MyService onDestroy");
	}

	class SendBinder extends Binder {
		SendMessageService getService() {
			return SendMessageService.this;
		}
	}
	
	class LoadLogin extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... a) {
			// TODO Auto-generated method stub\
			for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
                factorySite.getSite(siteName).getLogin();
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
			if (c.getCount()== 0) sendToast (getResources().getString(R.string.notify_channels_not_set));

			for (c.moveToFirst(); !c.isAfterLast();c.moveToNext()){
				String site = c.getString(0);
				String channel = c.getString(1);
                FactorySite factorySite = new FactorySite();
                FactorySite.SiteName siteName = FactorySite.SiteName.valueOf(site);
                Site siteClass = factorySite.getSite(siteName);
                int sendingResult = siteClass.sendMessage(channel, a[1]);
                switch (sendingResult){
                    case NEED_LOGIN:
                        sendToast(getResources().getString(R.string.toast_need_login_to) + siteName.name());
                        break;
                    case TOO_MUCH_SMILES_SC2TV:
                        sendToast(getResources().getString(R.string.toast_sc2tv_much_smiles));
                        break;
                }
            }


            return null;
        }
	}

	public void login() {
		LoadLogin login = new LoadLogin();
		login.execute();
		
		
		
		
	}

	public void sendMessage(String text, String currentTabTag) {
		SendMessage send = new SendMessage();
		send.execute(currentTabTag, text);

	}

	private void sendToast(final String toast){
		handler.post(new Runnable() {
            public void run() {
          	  Toast.makeText(getApplicationContext(), toast , Toast.LENGTH_SHORT).show();
               
            }
	
	
	 });
	}
	

}
