package ivied.p001astreamchat.Core;

import ivied.p001astreamchat.Sc2tv.Sc2tv;

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

        }
        return null;
    }

}
