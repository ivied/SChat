package ivied.p001astreamchat.Sites.Twitch;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Site;

/**
 * Created by Serv on 30.05.13.
 */
public class Twitch  extends Site {

    ExecutorService es;
    IrcClientShow bot;


    @Override
    public void readChannel(String channel) {


            int rnd=(int) (1 + Math.random() * 100);


            String random ="justinfan"+Integer.toString(rnd);
            bot = new IrcClientShow(random);


            // Enable debugging output.
            bot.setVerbose(true);

            // Connect to the IRC server.

            try {
                bot.connect("199.9.250.229", 6667, "");
            } catch (NickAlreadyInUseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IrcException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Join the #pircbot channel.
            bot.joinChannel("#" + channel);


    };




    @Override
    public void startThread(ChannelRun channelRun) {

        es = Executors.newSingleThreadExecutor();
        mFuture = es.submit(channelRun);

    }


    @Override
    public void destroyLoadMessages () {
        bot.disconnect();
        mFuture.cancel(true);
    }
    @Override
    public FactorySite.SiteName getSiteEnum() {
        return FactorySite.SiteName.TWITCH;
    }

    public class IrcClientShow extends PircBot {
        public IrcClientShow(String name) {
            this.setName(name);

        }

        public void onMessage(String channel, String sender, String login,
                              String hostname, String message) {
            long unixTime = System.currentTimeMillis() / 1000L;
            channel = channel.substring(1);
            insertMessage ( channel, sender, message, null, unixTime);

        }
    }


    @Override
    public FragmentLoginStandard getFragment() {
        return new FragmentLoginTwitch();
    }



}
