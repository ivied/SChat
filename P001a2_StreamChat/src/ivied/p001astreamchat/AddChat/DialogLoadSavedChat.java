package ivied.p001astreamchat.AddChat;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.VideoView.AddVideoStream;

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
    	AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle(R.string.dialog_load_chat_edit);
		adb.setSingleChoiceItems(chatNames, -1, myClickListener);
		adb.setPositiveButton(R.string.dialog_select, myClickListener);
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

				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					String channelId =c.getString(1);
					int color = c.getInt(2);
					String personalName = c.getString(3);
					String siteName = c.getString(0);
                    try{
					channels.add(new AddChatChannel(channelId,color,personalName, FactorySite.SiteName.valueOf(siteName.toUpperCase())));
                    } catch ( IllegalArgumentException e) {
                        channels.add(new AddChatChannel(channelId,color,personalName, AddVideoStream.VideoSiteName.valueOf(siteName)));
                    }

				}
				AddChat callingActivity = (AddChat) getActivity();
				callingActivity.loadSavedChat(channels, names.get(lv.getCheckedItemPosition())); 
				
				}
				 dialog.dismiss();
				 
			}

			
		}
	};
}
