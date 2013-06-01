package ivied.p001astreamchat.Core;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;

public class AddChatChannel {
	 String site;
	 String name;
	 int color;
	 String channelId;
	 int drawable;
	 FactorySite.SiteName siteInt;

	 AddChatChannel (String channelId, int color, String name , FactorySite.SiteName site) {
		this.channelId = channelId;
		this.color = color;
		this.name = name;
		this.siteInt = site;
		switch (site){
		case SC2TV:
			this.site = "SC2TV";
			
			this.drawable = R.drawable.sc2tv;
			break;
		case TWITCH:
			this.site = "TWITCH";
			this.drawable = R.drawable.twitch;
			break;
		}
	}



}
