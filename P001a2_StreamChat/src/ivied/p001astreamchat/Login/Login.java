package ivied.p001astreamchat.Login;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;



public class Login extends SherlockFragmentActivity {
    static final public String XML_LOGIN = "Login";
    protected static final String PASSWORD = "pass";
    DialogSelectLogin mDialogSelectLogin;
    FactorySite factorySite = new FactorySite();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));

       for (FactorySite.SiteName siteName : FactorySite.SiteName.values()){

            LinearLayout innerLayout1 = new LinearLayout(this);

            innerLayout1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            innerLayout1.setId(MainActivity.ID_LOGIN_FRAGMENTS+siteName.ordinal());
            {
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

                fTrans.add(MainActivity.ID_LOGIN_FRAGMENTS+siteName.ordinal(), factorySite.getSite(siteName).getFragment());
                fTrans.commit();
            }
            layout.addView(innerLayout1);
            setContentView(layout);
       }
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 1, 0, "Delete login");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDialogSelectLogin = new DialogSelectLogin();
        mDialogSelectLogin.show(getSupportFragmentManager(), "Load chat");
        return super.onOptionsItemSelected(item);
    }

    private class DialogSelectLogin extends SherlockDialogFragment{
        ArrayList<String> siteNames;
        public Dialog onCreateDialog(Bundle savedInstanceState) {
             siteNames =new  ArrayList < String >();
            for( FactorySite.SiteName siteName : FactorySite.SiteName.values()){
                siteNames.add(siteName.name());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.select_dialog_singlechoice, siteNames);
            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
            adb.setTitle(R.string.dialog_select_login_title);
            adb.setSingleChoiceItems(adapter, -1, myClickListener);
            adb.setPositiveButton(R.string.dialog_select, myClickListener);
            return adb.create();


        }

        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                ListView lv = ((AlertDialog) dialog).getListView();
                if (which == Dialog.BUTTON_POSITIVE) {
                    if (lv.getCheckedItemPosition()>=0){
                        deletePass(siteNames.get(lv.getCheckedItemPosition()));

                    }
                }
            }
        };

        private void deletePass(String site) {

            SharedPreferences sPref = getSharedPreferences(Login.XML_LOGIN, MODE_PRIVATE);
            final String SAVED_NAME = site;
            final String SAVED_PASS = site + PASSWORD;
            SharedPreferences.Editor ed = sPref.edit();
            ed.remove(SAVED_NAME);
            ed.remove(SAVED_PASS);
            ed.commit();
            Toast.makeText(getActivity(), site + " " + getResources().getString(R.string.Login_delete) , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(RESULT_OK, null);

            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
