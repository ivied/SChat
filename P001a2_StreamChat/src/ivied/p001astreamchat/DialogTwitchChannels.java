package ivied.p001astreamchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class DialogTwitchChannels extends DialogFragment implements
		OnClickListener {
	EditText input;

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		input = new EditText(getActivity());
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle("Check twich channel").setView(input)
				.setPositiveButton("Add", this);

		return adb.create();

	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		String channel;
		channel = input.getText().toString().trim().split("\\s+")[0];
		
		if(!channel.equalsIgnoreCase("")){
		if (!channel.equals("")) {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(
					"http://api.justin.tv/api/stream/list.json?channel="
							+ channel);
			String line = null;
			try {

				HttpResponse response = client.execute(httpGet);

				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));

				line = reader.readLine();

				Log.d(MainActivity.LOG_TAG, "прошло");

			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			if (line.length() < 10)
				channel = "not exist now";

			((EditText) getActivity().findViewById(R.id.editChannelTwitch)).setText(channel);
		}
		}
	}
}
