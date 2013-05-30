package ivied.p001astreamchat.Core;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import ivied.p001astreamchat.R;

public class Preference extends SherlockPreferenceActivity {
	Button savePrefs;  
	@SuppressWarnings("deprecation")
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.prefs_menu);
	    addPreferencesFromResource(R.xml.pref);
	   
	    int maxLength = 3;
	    InputFilter[] fArray = new InputFilter[1];
	    fArray[0] = new InputFilter.LengthFilter(maxLength);
	    
	EditText count = ((EditTextPreference) findPreference("count"))
            .getEditText();
	count.setFilters(fArray);
	
	
	
	
	  }
	  
	  @Override
	  public void onBuildHeaders(List<Header> target) {
	   // loadHeadersFromResource(R.xml.pref, target);
	  }
	  @Override
	  public boolean onKeyDown(int keyCode, KeyEvent event)  {
	      if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	          // do something on back.
	    	  Intent i;
		stopService(new Intent(this,SendMessageService.class));
		 stopService(new Intent(this,ChatService.class));
		MyApp.factoryReset();
		i = new Intent(MyApp.getContext(), MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApp.getContext().startActivity(i);
	          return true;
	      }

	      return super.onKeyDown(keyCode, event);
	  }

	
	}
