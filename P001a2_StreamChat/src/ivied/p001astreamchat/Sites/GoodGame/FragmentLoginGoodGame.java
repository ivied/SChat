package ivied.p001astreamchat.Sites.GoodGame;

import android.graphics.drawable.Drawable;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

/**
 * Created by Serv on 21.07.13.
 */
public class FragmentLoginGoodGame extends FragmentLoginStandard{
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
        goodGame.getLogin();
        return false;
    }
}
