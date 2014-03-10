package ivied.p001astreamchat.Sites;

import android.content.Context;
import android.graphics.drawable.Drawable;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.VideoView.HTML5WebView;

/**
 * Created by Serv on 12.06.13.
 */
public  abstract class VideoViewSetter  {
    public SetVideoView setVideoView;
    public Context context;
   /* public VideoViewSetter(Context context, SetVideoView setVideoView){
        this.setVideoView = setVideoView;
        this.context = context;
    }*/
    public  interface SetVideoView{
        void setVideoView(HTML5WebView html5WebView, String url, FactoryVideoViewSetter.VideoSiteName videoSiteName);
    }
    abstract public void getVideoView(String channel, Context context, SetVideoView setVideoView);
    abstract public Drawable getLogo();
    abstract public FragmentAddChannelStandard getFragmentAddChannel();
    abstract public FactoryVideoViewSetter.VideoSiteName getEnum();

}
