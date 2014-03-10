package ivied.p001astreamchat.Sites;

/**
 * Created by Serv on 19.07.13.
 */
public class Message {
    public String channel;
    public String nick;
    public String text;
    public long time;
    public String id;

    public Message(String channel, String nick, String text, String messageID, long time){
        this.channel = channel;
        this.nick = nick;
        this.text = text;
        this.id = messageID;
        this.time = time;
    }
}
