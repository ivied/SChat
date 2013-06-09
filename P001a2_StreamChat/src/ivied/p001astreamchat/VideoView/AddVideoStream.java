package ivied.p001astreamchat.VideoView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.Twitch.DialogTwitchChannelByGame;
import ivied.p001astreamchat.Sites.Twitch.DialogTwitchTopGames;
import ivied.p001astreamchat.Sites.Twitch.Twitch;

/**
 * Created by Serv on 07.06.13.
 */
public class AddVideoStream extends SherlockFragmentActivity implements View.OnClickListener, DialogTwitchChannelByGame.TwitchSelectedListener {


    private Button btnAddTwitchVideo;
    HTML5WebView mWebView;
    EditText streamName;
    VideoSiteName site;

    @Override
    public void pasteTwitchChannel(String channel) {
        streamName.setText(channel);
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
                DialogTwitchTopGames dialogSelectTwitch = new DialogTwitchTopGames();
                dialogSelectTwitch.show(getSupportFragmentManager(),"Show Twitch games");
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
