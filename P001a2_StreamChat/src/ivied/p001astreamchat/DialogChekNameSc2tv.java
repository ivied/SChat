package ivied.p001astreamchat;

import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class DialogChekNameSc2tv extends DialogFragment implements OnClickListener {

	EditText channelName;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 channelName = new EditText(getActivity());
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity()).setView(channelName)
				.setTitle("Type channel name")
				.setPositiveButton(android.R.string.ok, this);

		return adb.create();
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		EditText channel = (EditText) getActivity().findViewById(R.id.editChannelNumberSc2tv);
		String newchannel = channelName.getText().toString();
		if (!newchannel.equals("")){
			GetSc2tvCode newchannelCode = new GetSc2tvCode();
			newchannelCode.execute(newchannel);
			
			try {
				newchannel = newchannelCode.get();
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (ExecutionException e) {

				e.printStackTrace();
			}
			}
			channel.setText( newchannel);
			
	
		
	}
}
