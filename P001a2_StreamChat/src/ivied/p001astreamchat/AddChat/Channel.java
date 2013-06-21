package ivied.p001astreamchat.AddChat;

import android.graphics.drawable.Drawable;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.VideoViewSetter;

public class Channel {
    String site;
    String preferName;
    int color;
    String channelId;
    Drawable drawable;
    FactorySite.SiteName siteInt;
    FactoryVideoViewSetter.VideoSiteName siteVideoInt;

   public Channel(String channelId, int color, String preferName, FactorySite.SiteName site) {
        FactorySite factorySite = new FactorySite();
        Site mSite = factorySite.getSite(site);
        this.channelId = channelId;
        this.color = color;
        this.preferName = preferName;
        this.siteInt = site;
        this.site = site.name();
        this.drawable = mSite.getLogo();
    }

   public Channel(String channelId, int color, String preferName, FactoryVideoViewSetter.VideoSiteName site) {
        FactoryVideoViewSetter factoryVideoViewSetter = new FactoryVideoViewSetter();
        VideoViewSetter viewSetter = factoryVideoViewSetter.getVideoSite(site);
        this.channelId = channelId;
        this.color = color;
        this.preferName = preferName;
        this.siteVideoInt = site;
        this.site =site.name();
        this.drawable = viewSetter.getLogo();
    }




}
