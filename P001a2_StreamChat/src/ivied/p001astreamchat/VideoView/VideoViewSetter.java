package ivied.p001astreamchat.VideoView;

import android.content.Context;

/**
 * Created by Serv on 12.06.13.
 */
public  abstract class VideoViewSetter  {
    SetVideoView setVideoView;
    Context context;
    public VideoViewSetter(Context context, SetVideoView setVideoView){
        this.setVideoView = setVideoView;
        this.context = context;
    }
    public  interface SetVideoView{
        void setVideoView(HTML5WebView html5WebView, String url, FactoryVideoViewSetter.VideoSiteName videoSiteName);
    }
    abstract public void getVideoView(String Channel);

}
