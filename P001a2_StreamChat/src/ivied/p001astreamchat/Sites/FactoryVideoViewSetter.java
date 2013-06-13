package ivied.p001astreamchat.Sites;

import ivied.p001astreamchat.Sites.GoodGame.GoodGameVideoSetter;
import ivied.p001astreamchat.Sites.Twitch.TwitchVideoSetter;

/**
 * Created by Serv on 12.06.13.
 */
public class FactoryVideoViewSetter {


    public VideoViewSetter getVideoSite(VideoSiteName site) {

        switch (site){
            case TWITCHStream:
                return new TwitchVideoSetter();
            case GOODGAMEStream:
                return new GoodGameVideoSetter();
        }
        return null;
    }



    public enum VideoSiteName {
        TWITCHStream, GOODGAMEStream
    }
}
