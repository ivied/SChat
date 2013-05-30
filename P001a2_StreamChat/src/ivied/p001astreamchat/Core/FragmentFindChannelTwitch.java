package ivied.p001astreamchat.Core;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

import ivied.p001astreamchat.R;

public class FragmentFindChannelTwitch extends SherlockFragment implements OnClickListener {
	
	DialogFragment dlgTwitchChannels;
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_find_site_twitch, null);
		    
		    Button btnChekChannelTwitch = (Button) v.findViewById(R.id.btnChekChannelTwitch);
		    btnChekChannelTwitch.setOnClickListener(this);
		    
		    return v;
		  }
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case (R.id.btnChekChannelTwitch):
			dlgTwitchChannels = new DialogTwitchChannels();
			dlgTwitchChannels.show(getFragmentManager(), "Check twitch");
			break;
		}
		// TODO Auto-generated method stub
		
	}

}
