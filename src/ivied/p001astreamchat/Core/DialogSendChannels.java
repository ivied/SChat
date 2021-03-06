package ivied.p001astreamchat.Core;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;


public class DialogSendChannels extends DialogFragment implements
		OnClickListener {
	
	final String LOG_TAG = "myLogs";
	List<String> channels = new ArrayList<String>();
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	Cursor c;
	String chatName;
	public static DialogSendChannels newInstance(String title) {
		DialogSendChannels frag = new DialogSendChannels();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
			chatName= getArguments().getString("title");
		 Log.i(LOG_TAG, "dialogNameINside = " + chatName );
		 String[] projection = {"site","channel","flag"};
		 String selection = "chat = ?";
		 String [] selectionArgs = {chatName};
		
		 c = getActivity().getContentResolver().query(ADD_URI, projection, selection, selectionArgs, null);
		



		ArrayList<Integer> strings = new ArrayList<Integer>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            for(FactorySite.SiteName siteName : FactorySite.SiteName.values()){
                if (siteName.name().equalsIgnoreCase(c.getString(0))){
                    strings.add(c.getPosition());

                }
            }

		}
        String[] data = new String[strings.size()];
        boolean[] chkd = new boolean[strings.size()];
        int a =0;
        for (int i : strings){
            c.moveToPosition(i);
            data[a] = c.getString(0)+ " " + c.getString(1);

            chkd[a] = c.getString(2).equalsIgnoreCase("true");
            a++;
        }



		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
				.setTitle("Set channels for message")
				.setPositiveButton(R.string.yes, this)
				.setMultiChoiceItems(data, chkd, myItemsMultiClickListener);

		return adb.create();
	}

	OnMultiChoiceClickListener myItemsMultiClickListener = new OnMultiChoiceClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			c.moveToPosition(which);
			String [] selectionArgs = {chatName, c.getString(0), c.getString(1)} ;
		
			Log.d(LOG_TAG, "1" + getArguments().getString("title"));
			String val = isChecked ? "true" : "false";
			ContentValues cv = new ContentValues();
			cv.put("flag", val);
			getActivity().getContentResolver().update(ADD_URI, cv, "(chat = ?) AND (site = ?) AND (channel = ?)", selectionArgs);
			
		}
	};

	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
		case Dialog.BUTTON_POSITIVE:
            c.close();
			onCancel(dialog);
			break;

		}
	}

	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		Log.d(LOG_TAG, "Dialog 2: onDismiss");
	}

	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Log.d(LOG_TAG, "Dialog 2: onCancel");
	}

	
}
