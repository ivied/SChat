package ivied.p001astreamchat.Sites.Twitch;

import android.graphics.drawable.Drawable;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import java.io.IOException;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

/**
 * Created by Serv on 01.06.13.
 */
public class FragmentLoginTwitch extends FragmentLoginStandard {
    @Override
    public Drawable getLogo() {
        return getResources().getDrawable(R.drawable.twitch );
    }

    @Override
    public String getFragmentName() {
        return FactorySite.SiteName.TWITCH.name();
    }

    @Override
    public String getTextOnAddBtn() {
        return getResources().getString(R.string.btn_login_twitch);
    }

    @Override
    public boolean tryLogin(String nick, String pass) {
        IrcClient bot = new IrcClient(nick);
        try {
            bot.connect("199.9.250.229", 6667, pass);
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
        if (bot.isConnected()) {
            bot.disconnect();
            return true;

        } else {

            return false;
        }

    }




}
