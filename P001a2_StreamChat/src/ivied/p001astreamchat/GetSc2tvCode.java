package ivied.p001astreamchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * класс ищет введеное имя чата на портале возвращая код стримера
 * 
 * @author Serv
 * 
 */
class GetSc2tvCode extends AsyncTask<String, Void, String> {
	
	
	/**
	 * ответ диалога при неправильно заданном имени чата
	 */
	final static String STANDART_ANS = "wrong chatname";
	private static final String SC2_CONTENT = "http://sc2tv.ru/content/";
	private static final String SC2_LOGIN_PATTERN = ".*\\bhttp://sc2tv.ru/node/[0-9]{5}\\b.*";
	
	@Override
	protected String doInBackground(String... a) {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < a.length; i++) {
			result.append(a[i]);
			// result.append( optional separator );
		}
		String channel = result.toString();
		String getCode = STANDART_ANS;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(SC2_CONTENT  + channel);
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

				boolean b = false;
				while (!b ) {
					line = reader.readLine();
					Pattern p = Pattern
							.compile(SC2_LOGIN_PATTERN );
					Matcher m = p.matcher(line);
					b = m.matches();
					if (b) {
						getCode = m.group().replaceAll("\\D+", "")
								.replaceFirst("2", "");
					}
					
				}

			} else {
				Log.e(MainActivity.LOG_TAG, "Failed to download file");
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return getCode.toString();
	}

}