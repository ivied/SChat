package ivied.p001astreamchat.Sites;

import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.Sc2tv.Sc2tv;
import ivied.p001astreamchat.Sites.Twitch.Twitch;

/**
 * Created by Serv on 30.05.13.
 */
public class FactorySite {
    public enum SiteName {
        SC2TV(), TWITCH();

    }
    public Site getSite (SiteName site) {

        switch (site){
            case SC2TV:
            return new Sc2tv();
            case TWITCH:
            return new Twitch();
        }
        return null;
    }

}
