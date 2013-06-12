package ivied.p001astreamchat.ChatSites.Sc2tv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.R;

public class DialogCheckNameSc2tv extends DialogFragment implements OnClickListener {

	EditText channelName;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 channelName = new EditText(getActivity());
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity()).setView(channelName)
				.setTitle(getResources().getString(R.string.dialog_title_check_sc2tv_name))
				.setPositiveButton(android.R.string.ok, this);

		return adb.create();
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {

		EditText channel = (EditText) getActivity().findViewById(R.id.editChannelNumberSc2tv);
		String newChannel = channelName.getText().toString();
		if (!newChannel.equals("")){
			GetSc2tvCode newChannelCode = new GetSc2tvCode();
			newChannelCode.execute(newChannel);
			
			try {
				newChannel = newChannelCode.get();
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (ExecutionException e) {

				e.printStackTrace();
			}
			}
			channel.setText( newChannel);
			
	
		
	}

    static class GetSc2tvCode extends AsyncTask<String, Void, String> {


        private static final String SC2_CONTENT = "http://sc2tv.ru/content/";
        private static final String SC2_LOGIN_PATTERN = ".*\\bhttp://sc2tv.ru/node/[0-9]{5}\\b.*";

        @Override
        protected String doInBackground(String... a) {
            StringBuffer result = new StringBuffer();

            for (int i = 0; i < a.length; i++) {
                result.append(a[i]);

            }
            String channel = result.toString();
            String getCode = MyApp.getContext().getResources().getString(R.string.dialog_answer_wrong_sc2tv_name);
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
                    content.close();
                } else {
                    Log.e(MainActivity.LOG_TAG, "Failed to download file");
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            client.getConnectionManager().shutdown();
            return getCode.toString();
        }

    }
}
