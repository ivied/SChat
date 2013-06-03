package ivied.p001astreamchat.Sites.Twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;

public class DialogTwitchChannels extends DialogFragment implements
		OnClickListener {
	EditText input;
	CheckTwitchChannel checkTwitch;
    final static String CHECK_CHANNEL = "http://api.justin.tv/api/stream/list.json?channel=";
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		input = new EditText(getActivity());
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(getResources().getString(R.string.dialog_title_check_twitch_channel)).setView(input)
				.setPositiveButton("Add", this);

		return adb.create();

	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		String channel;
		channel = input.getText().toString().trim().split("\\s+")[0];

		if (!channel.equalsIgnoreCase("")) {
			if (!channel.equals("")) {
				checkTwitch = new CheckTwitchChannel();
				checkTwitch.execute(channel);
				try {
					channel = checkTwitch.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((EditText) getActivity().findViewById(R.id.editChannelTwitch)).setText(channel);
			}
		}
	}
	
	class CheckTwitchChannel extends AsyncTask<String, Void , String> {

	   

		@Override
		protected String doInBackground(String... a) {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(CHECK_CHANNEL+ a[0]);
			String line = null;
			try {

				HttpResponse response = client.execute(httpGet);

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));

				line = reader.readLine();


                content.close();
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			if (line.length() < 10)	a[0] = "not exist now";
			 client.getConnectionManager().shutdown();
			return a[0];
		}
		
		  @Override
		    protected void onPostExecute(String result) {
		      super.onPostExecute(result);
		      
		    }
	}
}
