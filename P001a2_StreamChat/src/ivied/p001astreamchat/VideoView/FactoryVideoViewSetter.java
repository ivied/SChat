package ivied.p001astreamchat.VideoView;

import android.content.Context;

import ivied.p001astreamchat.ChatSites.Sc2tv.Sc2tv;
import ivied.p001astreamchat.ChatSites.Site;
import ivied.p001astreamchat.ChatSites.Twitch.Twitch;
import ivied.p001astreamchat.ChatView.ChatList;

/**
 * Created by Serv on 12.06.13.
 */
public class FactoryVideoViewSetter {


    public VideoViewSetter getVideoSite(VideoSiteName site, Context context, VideoViewSetter.SetVideoView setVideoView) {

        switch (site){
            case TWITCHStream:
                return new TwitchVideoSetter(context ,setVideoView);

        }
        return null;
    }



    public enum VideoSiteName {
        TWITCHStream
    }
}
