package ivied.p001astreamchat.Core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;

public class DialogStopService extends DialogFragment implements OnClickListener {

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dlg_stop_service_question)
				.setPositiveButton(R.string.dlg_stop, this)
				.setNegativeButton(R.string.no, this);

		return adb.create();

	}

	@Override
	public void onClick(DialogInterface dlgInterface, int id) {
		switch(id){
		case Dialog.BUTTON_POSITIVE:
			((MainActivity) getActivity()).stopService();
			((MainActivity) getActivity()).stopServiceSend(); 
			break;
		case Dialog.BUTTON_NEGATIVE:
			dismiss();
			
			break;
		}
		// TODO Auto-generated method stub
		
	}
}
 