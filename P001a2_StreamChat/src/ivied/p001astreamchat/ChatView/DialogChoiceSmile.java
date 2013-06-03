package ivied.p001astreamchat.ChatView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import ivied.p001astreamchat.R;


public class DialogChoiceSmile extends DialogFragment implements  OnClickListener {
	DialogSmilesBySite dialogSc2tv;
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().setTitle("Choice site");
		    View v = inflater.inflate(R.layout.dialog_choise_site_smile, null);
		   v.findViewById(R.id.textSmileSc2tv).setOnClickListener(this);
		 v.findViewById(R.id.textSmileTwitch).setOnClickListener( this);
		    
		    return v;
		  }

	

	@Override
	public void onClick(View v) {
		onDismiss(getDialog());
		/// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.textSmileSc2tv:
			
			dialogSc2tv = DialogSmilesBySite.newInstance( "sc2tv");
			dialogSc2tv.show(getFragmentManager(), "sc2tv");
			
			break;
		case R.id.textSmileTwitch:
			dialogSc2tv = DialogSmilesBySite.newInstance( "twitch");
			dialogSc2tv.show(getFragmentManager(), "twitch");
			break;
		}
		 
	}

	
}
