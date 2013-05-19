package ivied.p001astreamchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class DialogChoiceSite extends DialogFragment implements  OnClickListener {
	 public static final String SITE = "site";
	 public static final String FOR = "for";
	 public static final int SC2TV = 1;
	public static final int TWITCH = 2;



	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().setTitle("Choice site");
		    View v = inflater.inflate(R.layout.dialog_choice_site_add_channel, null);
			   v.findViewById(R.id.textSiteSc2tv).setOnClickListener(this);
			 v.findViewById(R.id.textSiteTwitch).setOnClickListener( this);
		    
		    return v;
		  }

	

	@Override
	public void onClick(View v) {
		onDismiss(getDialog());
		// TODO Auto-generated method sstub
		switch (v.getId()){
		case R.id.textSiteSc2tv:
			Intent intentSc2tv = new Intent(getActivity(), AddChannel.class);
			intentSc2tv.putExtra(SITE, SC2TV);
			intentSc2tv.putExtra(FOR, "add");
			getActivity().startActivityForResult(intentSc2tv, AddChat.TASK_ADD);
			
			
			break;
		case R.id.textSiteTwitch:
			Intent intentTwitch = new Intent(getActivity(),AddChannel.class);
			intentTwitch.putExtra(SITE, TWITCH);
			intentTwitch.putExtra(FOR, "add");
			getActivity().startActivityForResult(intentTwitch, AddChat.TASK_ADD);
			break;
		}
		 
	}
}
