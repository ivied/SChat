package ivied.p001astreamchat.Sites.GoodGame;

import android.graphics.drawable.Drawable;
import android.util.Log;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Login.LoginException;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

/**
 * Created by Serv on 21.07.13.
 */
public class FragmentLoginGoodGame extends FragmentLoginStandard{
    private String LOG_TAG = "FragmentLoginGoodGame";

    @Override
    public Drawable getLogo() {
        return getResources().getDrawable(R.drawable.goodgame);
    }

    @Override
    public String getFragmentName() {
        return FactorySite.SiteName.GOODGAME.name();
    }

    @Override
    public String getTextOnAddBtn() {
        return getResources().getString(R.string.btn_login_goodgame);
    }

    @Override
    public boolean tryLogin(String nick, String pass) {
        GoodGame goodGame = new GoodGame();
        try {
            goodGame.getLogin();
        } catch (LoginException e) {
            Log.d(LOG_TAG, "Negative login attempt\n" + e.getMessage());
        }

        return GoodGame.GGNick == null ? false : true;
    }
}
