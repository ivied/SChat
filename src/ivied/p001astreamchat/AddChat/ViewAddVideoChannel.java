package ivied.p001astreamchat.AddChat;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.VideoViewSetter;
import ivied.p001astreamchat.VideoView.HTML5WebView;
/**
 * Created by Serv on 07.06.13.
 */
public class ViewAddVideoChannel extends SherlockFragmentActivity implements ChannelIdSelectedListener, VideoViewSetter.SetVideoView {


    FactoryVideoViewSetter factorySite = new FactoryVideoViewSetter();
    EditText streamName;
    FactoryVideoViewSetter.VideoSiteName site;

    FragmentAddChannelStandard fragment;
    EditText channelId;
    FactoryVideoViewSetter factoryVideoViewSetter = new FactoryVideoViewSetter();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView (R.layout.add_video_stream);
        Intent intent = getIntent();

       site = (FactoryVideoViewSetter.VideoSiteName) intent.getSerializableExtra(DialogChoiceSite.SITE);
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

       fragment = factorySite.getVideoSite(site).getFragmentAddChannel();
        fTrans.add(R.id.frameToVideoFragments, fragment);
        fTrans.commit();

        streamName = (EditText) findViewById(R.id.editNameVideoStream);


    }


    @Override
    public void onResume() {
        super.onResume();
        channelId = fragment.getEditTextChannel();
    }


    @Override
    public void pasteIdChannel(String channel) {

        streamName.setText(channel);
        channelId.setText(channel);

        VideoViewSetter videoSetter = factoryVideoViewSetter.getVideoSite(site);
        videoSetter.getVideoView(channel, this, this);



    }

    @Override
    public void setVideoView(HTML5WebView html5WebView, String url, FactoryVideoViewSetter.VideoSiteName videoSiteName) {
        channelId.setText(url);

        site = videoSiteName;
        FrameLayout addShowVideo = (FrameLayout) findViewById(R.id.frame_add_show_video);
        FrameLayout frameLayout = html5WebView.getLayout();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setLayoutParams(layoutParams);
        addShowVideo.addView(frameLayout);


    }



    public void onClickBtnAddStreamVideo(View v) {

        Intent i =new Intent() ;

        i.putExtra("channelId", channelId.getText().toString());

        i.putExtra("color", Color.BLACK);
        i.putExtra("name", streamName.getText().toString());
        i.putExtra("site", site);
        setResult(RESULT_OK, i);
        finish();

    }



}