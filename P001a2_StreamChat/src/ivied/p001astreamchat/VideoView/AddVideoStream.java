package ivied.p001astreamchat.VideoView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;

/**
 * Created by Serv on 07.06.13.
 */
public class AddVideoStream extends SherlockFragmentActivity implements View.OnClickListener {


    private Button btnAddTwitchVideo;
    HTML5WebView mWebView;
    EditText streamName;
    VideoSiteName site;
    public enum VideoSiteName {
        TWITCHStream
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_video_stream);
        btnAddTwitchVideo = (Button) findViewById(R.id.btnTwitchAddVideo);
        btnAddTwitchVideo.setOnClickListener(this);
        streamName = (EditText) findViewById(R.id.editNameVideoStream);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTwitchAddVideo:
                FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
                mWebView = new HTML5WebView(this);
                mWebView.loadUrl("http://goodgame.ru/player3?pomi");

                FrameLayout frameLayout = mWebView.getLayout();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

                frameLayout.setLayoutParams(layoutParams);
                addShowVideo.addView(frameLayout);
                site = VideoSiteName.TWITCHStream;
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
}
