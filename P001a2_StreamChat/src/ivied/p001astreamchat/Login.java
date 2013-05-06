package ivied.p001astreamchat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * @author Serv
 * Класс сохраняет логин на сайте если он правельный
 */
public class Login extends Activity implements OnClickListener {

	SharedPreferences sPref;
	final int SC2DIALOG = 1;
	ImageButton loginSc2,loginTwitch;
	EditText nameSc2tv, nameTwitch;
	EditText passSc2tv, passTwitch;
	Button clearSc2tvLogin,clearTwitchLogin;
	Button okay;
	int flag = 0;
	 static final int LOGIN_WRONG = 2;
	 static final int LOGIN_COMPLETE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		okay = (Button) findViewById(R.id.okLog);
		okay.setOnClickListener(this);
		loginSc2 = (ImageButton) findViewById(R.id.btnSc2tvAddLogin);
		loginSc2.setOnClickListener(this);
		nameSc2tv = (EditText) findViewById(R.id.sc2tvName);
		passSc2tv = (EditText) findViewById(R.id.sc2tvPass);
		clearSc2tvLogin = (Button) findViewById(R.id.btnCleanSc2tvLogin);
		clearSc2tvLogin.setOnClickListener(this);
		loginTwitch = (ImageButton) findViewById(R.id.btnTwitchLogin);
		loginTwitch.setOnClickListener(this);
		nameTwitch = (EditText) findViewById(R.id.twitchName);
		passTwitch = (EditText) findViewById(R.id.twitchPass);
		clearTwitchLogin = (Button) findViewById(R.id.btnCleanTwitchLogin);
		clearTwitchLogin.setOnClickListener(this);
		loadLogins();

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.okLog:
			setResult(RESULT_OK, null);
			finish();
			break;
		case R.id.btnSc2tvAddLogin:
			LogInSc2tv login = new LogInSc2tv();
			login.execute();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(passSc2tv.getWindowToken(), 0);
			break;
		case R.id.btnCleanSc2tvLogin:
			deletePass("sc2tv");
			nameSc2tv.setText("");
			passSc2tv.setText("");
			break;
		case R.id.btnTwitchLogin:
			LoginIrc loginIrc = new LoginIrc();
			loginIrc.execute();
	        
	      
			break;
		case R.id.btnCleanTwitchLogin:
			deletePass("twitch");
			nameTwitch.setText("");
			passTwitch.setText("");
			break;
			
		default:
			break;

		}
	}

	class LoginIrc extends AsyncTask<Void, Integer, Void> {
			String name = nameTwitch.getText().toString();
			String pass = passTwitch.getText().toString();
		protected Void doInBackground(Void... Params) {
		
			IrcClient bot = new IrcClient(name);

			// Enable debugging output.
			//bot.setVerbose(true);

			// Connect to the IRC server.
			try {
				bot.connect("199.9.250.229", 6667, pass);
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
			if (bot.isConnected()) {

				publishProgress(LOGIN_COMPLETE);
				bot.disconnect();
			} else {
				publishProgress(LOGIN_WRONG);
			
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			switch (values[0]) {

			case LOGIN_COMPLETE:
				savePass("twitch", name, pass);
				
				
				break;

			case Login.LOGIN_WRONG:
				Toast.makeText(getApplicationContext(), R.string.Login_failed,
						Toast.LENGTH_SHORT).show();
				
				break;

			}

		}
	}
	//TODO вынести фунцкционал класса в отдельный сервис
	class LogInSc2tv extends AsyncTask<Void, Integer, Void> {
		
		
		
		final String LOG_TAG = "myLogs";

		@Override
		protected Void doInBackground(Void... Params) {
			String name = nameSc2tv.getText().toString();
			String pass = passSc2tv.getText().toString();
			HttpPost post = new HttpPost("http://sc2tv.ru/");
			try {
				// post
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						4);
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("pass", pass));
				nameValuePairs.add(new BasicNameValuePair("op", "Вход"));
				nameValuePairs.add(new BasicNameValuePair("form_id",
						"user_login_block"));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(post);
				Header[] headers = response.getAllHeaders();
				try {
					headers[9].getValue();
					publishProgress(LOGIN_COMPLETE);
				} catch (RuntimeException e) {
					publishProgress(LOGIN_WRONG);
				}

			} catch (ClientProtocolException e) {

			
			} catch (IOException e) {

			
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			
			switch (values[0]) {
			
			case LOGIN_COMPLETE:
			
			Toast.makeText(getApplicationContext(), R.string.Login_complete,
					Toast.LENGTH_SHORT).show();
			savePass("sc2tv", nameSc2tv.getText().toString(), passSc2tv
					.getText().toString());
			break;
			
			case Login.LOGIN_WRONG:
			
				Toast.makeText(getApplicationContext(), R.string.Login_failed,
						Toast.LENGTH_SHORT).show();
				break;
				
				
			}
		}
	}
	
	private void deletePass(String site) {
		// TODO Auto-generated method stub
		sPref = getPreferences(MODE_PRIVATE);
		final String SAVED_NAME = site;
		final String SAVED_PASS = site + "pass";
		Editor ed = sPref.edit();
		ed.remove(SAVED_NAME);
		ed.remove(SAVED_PASS);
		ed.commit();
		Toast.makeText(this, site + " " + getResources().getString(R.string.Login_delete) , Toast.LENGTH_SHORT).show();
	}


	void savePass(String site, String name, String pass) {
		sPref = getPreferences(MODE_PRIVATE);
		final String SAVED_NAME = site;
		final String SAVED_PASS = site + "pass";
		Editor ed = sPref.edit();
		ed.putString(SAVED_NAME, name);
		ed.putString(SAVED_PASS, pass);
		ed.commit();
		Toast.makeText(this,site + " " + getResources().getString( R.string.Login_saved), Toast.LENGTH_SHORT).show();
	}

	void loadLogins() {
		final String SAVED_NAME = "sc2tv";
		final String SAVED_PASS = "sc2tvpass";
		final String TWITCH_SAVED_NAME = "twitch";
		final String TWITCH_SAVED_PASS = "twitchpass";
		sPref = getPreferences(MODE_PRIVATE);
		String savedText = sPref.getString(SAVED_NAME, "");
		nameSc2tv.setText(savedText);
		savedText = sPref.getString(SAVED_PASS, "");
		passSc2tv.setText(savedText);
		savedText = sPref.getString(TWITCH_SAVED_NAME, "");
		nameTwitch.setText(savedText);
		savedText = sPref.getString(TWITCH_SAVED_PASS, "");
		passTwitch.setText(savedText);
		
	}
}
