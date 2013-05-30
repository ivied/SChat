package ivied.p001astreamchat.Core;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import ivied.p001astreamchat.R;

public class Help extends SherlockFragmentActivity {
	TextView textVersion , textLinks;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.help);
		PackageInfo pInfo;
		String version = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		textVersion = (TextView) findViewById(R.id.textHelpVersion);
		textVersion.setText(textVersion.getText().toString()+ " " + version );
		textLinks = (TextView) findViewById(R.id.textHelpLinks);
	//	textLinks.setText(Html.fromHtml(getResources().getString(R.string.text_help_links)));
		textLinks.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
