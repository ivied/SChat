package ivied.p001astreamchat.VideoView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.AsyncDownloadJson;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.Twitch.DialogTwitchChannelByGame;
import ivied.p001astreamchat.Sites.Twitch.DialogTwitchTopGames;
import ivied.p001astreamchat.Sites.Twitch.Twitch;

/**
 * Created by Serv on 07.06.13.
 */
public class AddVideoStream extends SherlockFragmentActivity implements View.OnClickListener, DialogTwitchChannelByGame.TwitchSelectedListener, AsyncDownloadJson.GetJson {

    private final static String CRYPT_KEY =  "Wd75Yj9sS26Lmhve";
    private final static String TOKEN_ADDRESS_1 = "http://usher.twitch.tv/stream/iphone_token/";
    private final static String TOKEN_ADDRESS_2 = ".json?type=iphone&allow_cdn=true";
    private final static String MULTI_PLAYLIST_1 = "http://usher.twitch.tv/stream/multi_playlist/";
    private final static String MULTI_PLAYLIST_2 = ".m3u8";
    private final static String MULTI_PLAYLIST_3 = "?allow_cdn=true&token=";
    private final static String MULTI_PLAYLIST_4 = "&hd=true";


    private Button btnAddTwitchVideo;
    HTML5WebView mWebView;
    EditText streamName;
    VideoSiteName site;
    String channel;
    @Override
    public void pasteTwitchChannel(String channel) {
        this.channel = channel.toLowerCase();
        streamName.setText(channel);
        AsyncDownloadJson.CustomDownloadJson downloadJson =
                new AsyncDownloadJson.CustomDownloadJson(TOKEN_ADDRESS_1 +this.channel + TOKEN_ADDRESS_2 , this);
        AsyncDownloadJson asyncDownloadJson = new AsyncDownloadJson();
        asyncDownloadJson.execute(downloadJson);



    }

    @Override
    public void afterGetJson(String json) {
        addTwitchVideoView(json);
    }

    public enum VideoSiteName {
        TWITCHStream
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView (R.layout.add_video_stream);
        btnAddTwitchVideo = (Button) findViewById(R.id.btnTwitchAddVideo);
        btnAddTwitchVideo.setOnClickListener(this);
        streamName = (EditText) findViewById(R.id.editNameVideoStream);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTwitchAddVideo:
              /*  FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
                mWebView = new HTML5WebView(this);
               String url = "<video x-webkit-airplay=\"allow\" controls=\"\" alt=\"Live Stream\" width=\"100%\" height=\"100%\" src=\"http://moxa.no-ip.biz/some_shit.m3u8\"></video>";

               mWebView.loadData(url,"text/html", "UTF-8");
               //mWebView.loadUrl("http://moxa.no-ip.biz/video.htm");
               // mWebView.loadUrl("http://goodgame.ru/player3?1053");
                FrameLayout frameLayout = mWebView.getLayout();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                frameLayout.setLayoutParams(layoutParams);
                addShowVideo.addView(frameLayout);
                site = VideoSiteName.TWITCHStream;*/
            DialogFragment dialogSelectTwitch = new DialogTwitchTopGames();
                dialogSelectTwitch.show(getSupportFragmentManager(),"Show Twitch games");
             //   addView("lol");
             /*   TwitchFindChannelPlaylist twitchFindChannelPlaylist = new TwitchFindChannelPlaylist();
                twitchFindChannelPlaylist.execute("");*/
                break;
        }
    }

    public void onClickBtnAddStreamVideo(View v) {

        Intent i =new Intent() ;
        try {


        String  channel = mWebView.getUrl();

        if(!channel.equalsIgnoreCase("")){

            i.putExtra("channelId", channel);

            i.putExtra("color", Color.BLACK);
            i.putExtra("name", streamName.getText().toString());
            i.putExtra("site", site);
            setResult(RESULT_OK, i);
            finish();
        }
        }catch (Exception e){
            Toast.makeText(this, getString(R.string.toast_add_stream_first), Toast.LENGTH_SHORT).show();
        }


    }


    private void addTwitchVideoView (String json) {

        try {
            json = json.replace("[", "").replace("]","");
            JSONObject  jsonObj = new JSONObject(json);
            String token = jsonObj.getString("token");
            String hmacSha1 =hmacSha1(token, CRYPT_KEY);
            String query = URLEncoder.encode(hmacSha1 + ":" + token, "utf-8");
            String address = MULTI_PLAYLIST_1 + channel +MULTI_PLAYLIST_2 + MULTI_PLAYLIST_3+ query + MULTI_PLAYLIST_4;

            DownloadFile downloadFile = new DownloadFile();
            downloadFile.execute(new String [] {address,hmacSha1});
        } catch (JSONException e) {
        e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addView(String playlist) {
        FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
        File playlistFile = new File (MyApp.getContext() + "/"+ playlist);
        URL file = null;
        try {
            file = playlistFile.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mWebView = new HTML5WebView(this);
        String html= "<video x-webkit-airplay=\"allow\" controls=\"\" alt=\"Live Stream\" width=\"100%\" height=\"100%\" src=\""+ file +"\"></video>";
        Log.i(MainActivity.LOG_TAG, file.toString()+  "  "+ playlistFile.exists());





        //String html = "<img src= \""+ file +"\" alt=\"\" width=\"340\" height=\"560\" id=\"irc_mi\" style=\"margin-top: 0px;\" />";

       // String html = "<iframe width=800 height=600 src=\"http://www.google.com\"></iframe>";
        mWebView. loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);


       /* FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
        mWebView = new HTML5WebView(this);
        //String html= "<video x-webkit-airplay=\"allow\" controls=\"\" alt=\"Live Stream\" width=\"100%\" height=\"100%\" src=\"igkgi\"></video>";
        File img = new File ("data/data/ivied.p001astreamchat/files/images.jpg");


        URL file = null;
        try {
            file = img.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(MainActivity.LOG_TAG, file.toString());
        String html = "<img src= \""+ file +"\" alt=\"\" width=\"340\" height=\"560\" id=\"irc_mi\" style=\"margin-top: 0px;\" />";

        // String html = "<iframe width=800 height=600 src=\"http://www.google.com\"></iframe>";
        mWebView. loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);;*/


        //mWebView.loadUrl("http://moxa.no-ip.biz/video.htm");
        // mWebView.loadUrl("http://goodgame.ru/player3?1053");
        FrameLayout frameLayout = mWebView.getLayout();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setLayoutParams(layoutParams);
        addShowVideo.addView(frameLayout);
        site = VideoSiteName.TWITCHStream;
    }

    public static String hmacSha1(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private class DownloadFile extends AsyncTask<String, Void, Void> {
        String file;
        @Override
        protected Void doInBackground(String... params) {
            try {
               // context.getCacheDir()
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                connection.connect();
                file = params[1]+".m3u8";
                MyApp.getContext().getCacheDir().mkdir();
                BufferedWriter  output = new BufferedWriter(new OutputStreamWriter(
                        openFileOutput( file, MODE_WORLD_READABLE)));
                // download the file
                InputStream input = new BufferedInputStream(url.openStream());



                byte data[] = new byte[1024];
                int x;
                while ((x = input.read(data)) != -1) {
                    String string = new String(data, 0, x);
                    output.write(string); }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            addView(file);
        }


    }



  /*  class TwitchFindChannelPlaylist extends AsyncTask <String,Void,String> {

        StringBuilder builder;
        @Override
        protected String doInBackground(String... params) {
            Site site = new Twitch();
            HttpGet get = new HttpGet( "http://www.twitch.tv/dandinh");
            HttpResponse response = site.getResponse(get);

            HttpEntity entity = response.getEntity();
            InputStream content = null;
            try {
                content = entity.getContent();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
                String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
                int i =0;
                i++;
            } catch (IOException e) {
            e.printStackTrace();
        }
            String resp =builder.toString();
            return resp;
        }
    }*/
}
