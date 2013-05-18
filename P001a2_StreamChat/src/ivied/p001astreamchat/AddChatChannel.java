package ivied.p001astreamchat;

public class AddChatChannel {
	 String site;
	 String name;
	 int color;
	 String channelId;
	 int drawable;
	 int siteInt;

	 AddChatChannel (String channelId, int color, String name , int site) {
		this.channelId = channelId;
		this.color = color;
		this.name = name;
		this.siteInt = site;
		switch (site){
		case DialogChoiceSite.SC2TV:
			this.site = "sc2tv";
			
			this.drawable = R.drawable.sc2tv;
			break;
			
		}
	}

	public AddChatChannel(String channelId, int color, String name,
			String siteName) {
		this.channelId = channelId;
		this.color = color;
		this.name = name;
		this.site = siteName;
		if(siteName.equalsIgnoreCase("sc2tv")){
			this.siteInt = DialogChoiceSite.SC2TV;
			this.drawable = R.drawable.sc2tv;
		}
		
		// TODO Auto-generated constructor stub
	}

}
