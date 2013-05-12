package ivied.p001astreamchat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;



public class DialogChoseSmile extends DialogFragment implements  OnClickListener {
	DialogSc2tvSmiles dialogSc2tv;
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    getDialog().setTitle("Choise site");
		    View v = inflater.inflate(R.layout.dialog_choise_site_smile, null);
		   v.findViewById(R.id.textSmileSc2tv).setOnClickListener(this);
		 v.findViewById(R.id.textSmileTwitch).setOnClickListener( this);
		    
		    return v;
		  }

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.textSmileSc2tv:
			onDismiss(getDialog());
			dialogSc2tv = new DialogSc2tvSmiles();
			dialogSc2tv.show(getFragmentManager(), "sc2tv");
			
			break;
		case R.id.textSmileTwitch:
			break;
		}
		 
	}

	
}
