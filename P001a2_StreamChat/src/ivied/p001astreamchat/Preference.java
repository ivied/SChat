package ivied.p001astreamchat;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class Preference extends SherlockPreferenceActivity implements OnClickListener {
	Button savePrefs;  
	@SuppressWarnings("deprecation")
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.prefs_menu);
	    addPreferencesFromResource(R.xml.pref);
	 savePrefs = (Button) findViewById (R.id.savePrefs);
	 savePrefs.setOnClickListener(this);
	  }
	  
	  @Override
	  public void onBuildHeaders(List<Header> target) {
	   // loadHeadersFromResource(R.xml.pref, target);
	  }

	@Override
	public void onClick(View arg0) {
		Intent i;
		stopService(new Intent(this,SendMessageService.class));
		 stopService(new Intent(this,ChatService.class));
		MyApp.factoryReset();
		i = new Intent(MyApp.getContext(), MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApp.getContext().startActivity(i);
		
		
	}
	}
