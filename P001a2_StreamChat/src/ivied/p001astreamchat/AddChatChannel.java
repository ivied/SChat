package ivied.p001astreamchat;

public class AddChatChannel {
	 String site;
	 String name;
	 String color;
	 String channelId;
	 int drawable;

	 AddChatChannel (String channelId, String color, String name , int site) {
		this.channelId = channelId;
		this.color = color;
		this.name = name;
		switch (site){
		case DialogChoiceSite.SC2TV:
			this.site = String.valueOf(site);
			this.drawable = R.drawable.sc2tv;
			break;
			
		}
	}

}
