package ivied.p001astreamchat.VideoView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import ivied.p001astreamchat.ChatSites.Twitch.DialogTwitchChannelByGame;
import ivied.p001astreamchat.ChatSites.Twitch.DialogTwitchTopGames;
import ivied.p001astreamchat.R;

/**
 * Created by Serv on 07.06.13.
 */
public class AddVideoStream extends SherlockFragmentActivity implements View.OnClickListener, DialogTwitchChannelByGame.TwitchSelectedListener, VideoViewSetter.SetVideoView {

    private Button btnAddTwitchVideo;

    EditText streamName;
    FactoryVideoViewSetter.VideoSiteName site;
    String  channel;
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

                DialogFragment dialogSelectTwitch = new DialogTwitchTopGames();
                dialogSelectTwitch.show(getSupportFragmentManager(),"Show Twitch games");

                break;
        }
    }


    @Override
    public void pasteTwitchChannel(String channel) {

        streamName.setText(channel);

        TwitchVideoSetter videoSetter = new TwitchVideoSetter( this ,this);
        videoSetter.getVideoView(channel.toLowerCase());



    }

    @Override
    public void setVideoView(HTML5WebView html5WebView, String url, FactoryVideoViewSetter.VideoSiteName videoSiteName) {
        channel = url;
        site = videoSiteName;
        FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
        FrameLayout frameLayout = html5WebView.getLayout();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setLayoutParams(layoutParams);
        addShowVideo.addView(frameLayout);


    }



    public void onClickBtnAddStreamVideo(View v) {

        Intent i =new Intent() ;
        try {



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




   /* private class DownloadFile extends AsyncTask<String, Void, Void> {
        String name;
        @Override
        protected Void doInBackground(String... params) {
            try {
               // context.getCacheDir()
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();

                connection.connect();
                name = params[1]+".m3u8";
                File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ getResources().getString(R.string.app_name));
                appDir.mkdir();
                File file = new File(appDir, name);

                BufferedWriter  output = new BufferedWriter(new FileWriter(
                        file));
                // download the name
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
            addView(name);
        }


    }*/

}