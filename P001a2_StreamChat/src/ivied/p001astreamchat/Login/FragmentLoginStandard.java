package ivied.p001astreamchat.Login;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import ivied.p001astreamchat.R;

/**
 * Created by Serv on 01.06.13.
 */
public abstract  class FragmentLoginStandard extends SherlockFragment implements View.OnClickListener {
    Button btnAddLogin;
    ImageButton btnImageAddLogin;
    EditText editPass;
    EditText editLogin;

    abstract public Drawable getLogo();
    abstract public String getFragmentName();
    abstract public String getTextOnAddBtn();
    abstract public boolean tryLogin (String nick, String pass);

    static final int LOGIN_WRONG = 2;
    static final int LOGIN_COMPLETE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_standart, null);
        btnAddLogin = (Button) v.findViewById(R.id.btnAddLogin);
        btnAddLogin.setOnClickListener(this);
        btnAddLogin.setText(getTextOnAddBtn());
        btnAddLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        btnImageAddLogin = (ImageButton) v.findViewById(R.id.btnImageAddLogin);

        btnImageAddLogin.setBackgroundDrawable(getLogo());
        editPass = (EditText) v.findViewById(R.id.editPass);
        editLogin = (EditText) v.findViewById(R.id.editNick);
        loadLogin();
        return v;
    }

    @Override
    public void onClick(View v) {
        String nick = editLogin.getText().toString();
        String pass = editPass.getText().toString();
        String [] params = {nick, pass};
        TryLogin login = new TryLogin();
        login.execute(params);
    }

    public void savePass(String site, String name, String pass) {

        SharedPreferences sPref = getActivity().getSharedPreferences(Login.XML_LOGIN, 0);
        final String SAVED_NAME = site;
        final String SAVED_PASS = site + Login.PASSWORD;
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_NAME, name);
        ed.putString(SAVED_PASS, pass);
        ed.commit();
        Toast.makeText(getActivity(), site + " " + getResources().getString(R.string.Login_saved), Toast.LENGTH_SHORT).show();

    }


    public void loadLogin() {
        final String SAVED_NAME = getFragmentName();
        final String SAVED_PASS = getFragmentName()+ Login.PASSWORD;

        SharedPreferences sPref = getActivity().getSharedPreferences(Login.XML_LOGIN, 0);
        String savedText = sPref.getString(SAVED_NAME, "");
        editLogin.setText(savedText);
        savedText = sPref.getString(SAVED_PASS, "");
        editPass.setText(savedText);


    }

    public class TryLogin extends AsyncTask<String , Integer, Void> {
        String name;
        String pass;

        @Override
        protected Void doInBackground(String... Params) {
            name = Params[0];
            pass = Params[1];
            int i = (tryLogin(name, pass))? LOGIN_COMPLETE : LOGIN_WRONG;
            publishProgress(i);
         return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            switch (values[0]) {

                case LOGIN_COMPLETE:

                    Toast.makeText(getSherlockActivity().getApplicationContext(), R.string.Login_complete,
                            Toast.LENGTH_SHORT).show();
                    savePass(getFragmentName(), name, pass);
                    break;

                case LOGIN_WRONG:

                    Toast.makeText(getSherlockActivity().getApplicationContext(), R.string.Login_failed,
                            Toast.LENGTH_SHORT).show();
                    break;


            }
        }
    }



}
