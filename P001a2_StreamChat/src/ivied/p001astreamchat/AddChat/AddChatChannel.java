package ivied.p001astreamchat.AddChat;

import android.graphics.drawable.Drawable;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.Sites.VideoViewSetter;

public class AddChatChannel {
    String site;
    String name;
    int color;
    String channelId;
    Drawable drawable;
    FactorySite.SiteName siteInt;
    FactoryVideoViewSetter.VideoSiteName siteVideoInt;
    AddChatChannel (String channelId, int color, String name , FactorySite.SiteName site) {
        FactorySite factorySite = new FactorySite();
        Site mSite = factorySite.getSite(site);
        this.channelId = channelId;
        this.color = color;
        this.name = name;
        this.siteInt = site;
        this.site = site.name();
        this.drawable = mSite.getLogo();
    }

    AddChatChannel (String channelId, int color, String name , FactoryVideoViewSetter.VideoSiteName site) {
        FactoryVideoViewSetter factoryVideoViewSetter = new FactoryVideoViewSetter();
        VideoViewSetter viewSetter = factoryVideoViewSetter.getVideoSite(site);
        this.channelId = channelId;
        this.color = color;
        this.name = name;
        this.siteVideoInt = site;
        this.site =site.name();
        this.drawable = viewSetter.getLogo();
    }

}
