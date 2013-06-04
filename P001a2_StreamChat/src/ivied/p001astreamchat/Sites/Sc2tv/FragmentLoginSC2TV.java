package ivied.p001astreamchat.Sites.Sc2tv;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ivied.p001astreamchat.Login.FragmentLoginStandard;
import ivied.p001astreamchat.Login.Login;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

/**
 * Created by Serv on 01.06.13.
 */
public class FragmentLoginSC2TV extends FragmentLoginStandard{



    @Override
    public  Drawable getLogo() {
        return getResources().getDrawable(R.drawable.sc2tv );
    }

    @Override
    public String getFragmentName() {
        return FactorySite.SiteName.SC2TV.name();
    }

    @Override
    public String getTextOnAddBtn() {
        return getResources().getString(R.string.btn_login_sc2tv);
    }



    @Override
    public boolean tryLogin(String name, String pass) {
        Sc2tv sc2tv = new Sc2tv();
        HttpPost post = sc2tv.getSc2tvPost(name, pass);
        HttpResponse response = sc2tv.getResponse(post);
        Header[] headers = response.getAllHeaders();
        try {
            headers[9].getValue();
            return true;
        } catch (RuntimeException e) {
            return false;
        }

    }


}
