package ivied.p001astreamchat;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ListView;

public class DialogLoadSavedChat extends DialogFragment {
	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	ArrayList <String> names = new ArrayList<String>();
	ArrayList<AddChatChannel> channels =new ArrayList<AddChatChannel>();
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Cursor c = getActivity().getContentResolver().query(SERVICE_URI, new String[] { "chat" }, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			
			String chatName=  c.getString(0);
			
			names.add(chatName);
			
		}
		String chatNames []= new String [ names.size()];
		chatNames = names.toArray(chatNames);

		// This example shows how to add a custom layout to an AlertDialog

		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Choice chat for edit");
		adb.setSingleChoiceItems(chatNames, -1, myClickListener);
		adb.setPositiveButton("choice", myClickListener);
		return adb.create();
		
		
	}
	DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			
			
			ListView lv = ((AlertDialog) dialog).getListView();
			if (which == Dialog.BUTTON_POSITIVE) {
				if (lv.getCheckedItemPosition()>=0){
					
				
				String[] projection = new String[] { "site", "channel","color","personal" };
				String[] selectionArgs = new String[] { names.get(lv.getCheckedItemPosition())};
				Cursor c = getActivity().getContentResolver()	.query(ADD_URI, projection,	" chat = ?", selectionArgs, null);
				
				
				// ������� � ��� ������� ���������� ��������
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String channelId =c.getString(1);
					int color = c.getInt(2);
					String personalName = c.getString(3);
					String siteName = c.getString(0);
					//Log.i(MainActivity.LOG_TAG, "id = " + channelId +" color =  " + color + " personal Name = " + personalName +" site name = "+siteName);
					channels.add(new AddChatChannel(channelId,color,personalName,siteName));

				}
				AddChat callingActivity = (AddChat) getActivity();
				callingActivity.loadSavedChat(channels, names.get(lv.getCheckedItemPosition())); 
				
				}
				 dialog.dismiss();
				 
			}

			
		}
	};
}
