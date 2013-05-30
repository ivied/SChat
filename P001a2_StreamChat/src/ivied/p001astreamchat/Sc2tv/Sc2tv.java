package ivied.p001astreamchat.Sc2tv;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ivied.p001astreamchat.Core.FactorySite;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.Site;

/**
 * Cddreated by Serv on 29.05.13.
 */
public class Sc2tv extends Site {

    final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
    private static final String CHANNEL_MESSAGES = "http://chat.sc2tv.ru/memfs/channel-";

    @Override
    public void readChannel(String channel) {

        StringBuilder builderJson = jsonRequest(channel);

        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject jsonObj = new JSONObject(builderJson.toString());


            jsonArray = jsonObj.getJSONArray("messages");} catch (JSONException e) {
            e.printStackTrace();
        }

        insertSc2tv (jsonArray, 0, channel);


    }

    private void insertSc2tv(JSONArray jsonArray, int i, String channel) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String[] selectionArgs = new String[] { jsonObject.getString("id") };
            Cursor c = MyApp.getContext().getContentResolver().query(
                    INSERT_URI, null,
                    "identificator = ?", selectionArgs, null);
            if (c.getCount() == 0) {

                i++;
                insertSc2tv(jsonArray, i, channel);
                i--;
                String nick = jsonObject.getString("name");
                String message = jsonObject.getString("message");
                String id = jsonObject.getString("id");
                long time = 0;
                try {

                    final SimpleDateFormat df = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    final Date d = df.parse(jsonObject.getString("date"));

                    time = d.getTime() / 1000;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                insertMessage( FactorySite.SiteName.SC2TV, channel, nick, message, id, time);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private StringBuilder jsonRequest(String channel) {
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
                content.close();
            } else {

            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    client.getConnectionManager().shutdown();
    return builder;
    }

    @Override
    protected String  getSite () {
        return FactorySite.SiteName.SC2TV.name();
    }
      /*private int getCountOfNew(JSONArray jsonArray) {

        int i = 0 ;

        Cursor c  ;
        JSONObject jsonObj = new JSONObject();
        try {


            do{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String[] selectionArgs = new String[] { jsonObject.getString("id") };

                c = MyApp.getContext().getContentResolver().query(
                        INSERT_URI, null,
                        "identificator = ?", selectionArgs, null);
                if (c.getCount() == 0) i++;
            }while ((c.getCount() == 0) && (jsonArray.length() != i));
            c.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } return i;
    }
*/
}
