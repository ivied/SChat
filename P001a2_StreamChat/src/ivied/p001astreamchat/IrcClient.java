package ivied.p001astreamchat;

import java.util.logging.Logger;

import org.jibble.pircbot.PircBot;

public class IrcClient extends PircBot {
	public IrcClient(String name) {
        this.setName(name);
        
    }
	
	 public void onMessage(String channel, String sender,
             String login, String hostname, String message) {
	
		

}
	 
	 
}
