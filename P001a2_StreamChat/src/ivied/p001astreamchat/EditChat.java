package ivied.p001astreamchat;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.ActionMenuView.LayoutParams;


/**
 * @author Serv Класс создает окно activity для редактирования чатов
 */
public class EditChat extends SherlockFragmentActivity implements OnClickListener {
	final String LOG_TAG = "myLogs";
	final int SC2_DIALOG = 1;
	final int LOAD_CHAT = 2;
	DialogTwitchChannels dlg2;
	ImageButton addSc2,addTwitch;
	EditText sc2channels;
	static EditText twitchChannels;
	EditText setName;
	Button clearSc2tv,clearTwitch;
	Button setChat;
	String newchannelAdd = "ded";
	RelativeLayout mLayout;
	Button closeChat;
	Button loadSavedChats;
	
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");
	ArrayList <String> names = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		String button = i.getStringExtra("button");
		setContentView(R.layout.add_chat);
		twitchChannels = (EditText) findViewById(R.id.twitchChannels);
		mLayout = (RelativeLayout) findViewById(R.id.mLayout);
		addSc2 = (ImageButton) findViewById(R.id.btnSc2tvAdd);
		addSc2.setOnClickListener(this);
		clearSc2tv = (Button) findViewById(R.id.btnCleanSc2tv);
		clearSc2tv.setOnClickListener(this);
		setChat = (Button) findViewById(R.id.setChat);
		setChat.setOnClickListener(this);
		sc2channels = (EditText) findViewById(R.id.sc2tvChannels);
		setName = (EditText) findViewById(R.id.chatName);
		addTwitch = (ImageButton) findViewById(R.id.btnTwitchAdd);
		addTwitch.setOnClickListener(this);
		clearTwitch = (Button) findViewById(R.id.btnCleanTwitch);
		clearTwitch.setOnClickListener(this);
		if (button.equalsIgnoreCase("Edit")){
			
		closeChat = new Button(this);
		
		RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lParams.addRule(RelativeLayout.ALIGN_TOP, setChat.getId());
		lParams.addRule(RelativeLayout.LEFT_OF, sc2channels.getId());
		closeChat.setText("Close Chat");
		closeChat.setId(3);
		mLayout.addView(closeChat, lParams);
		closeChat.setOnClickListener(this);
		RelativeLayout.LayoutParams lParams2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lParams2.addRule(RelativeLayout.LEFT_OF, setChat.getId());
		lParams2.addRule(RelativeLayout.ALIGN_TOP, setChat.getId());
		lParams2.addRule(RelativeLayout.RIGHT_OF, addSc2.getId());
		loadSavedChats = new Button(this);
		loadSavedChats.setText("Load chat");
		loadSavedChats.setId(2);
		
		mLayout.addView(loadSavedChats, lParams2);
		loadSavedChats.setOnClickListener(this);}
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SC2_DIALOG:

			// This example shows how to add a custom layout to an AlertDialog
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory
					.inflate(R.layout.sc2choise, null);

			return new AlertDialog.Builder(EditChat.this)
					.setIcon(R.drawable.sc2tv)
					.setTitle(R.string.add_channel)
					.setView(textEntryView)
					.setPositiveButton(R.string.set_code,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String newchannel = ((EditText) textEntryView)
											.getText().toString();
									sc2channels.setText(sc2channels.getText()
											.toString() + " " + newchannel);
									((EditText) textEntryView).setText("");
								}
							})
					.setNegativeButton(R.string.set_name,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									String newchannel = ((EditText) textEntryView)
											.getText().toString();
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
									sc2channels.setText(sc2channels.getText()
											.toString() + " " + newchannel);
									
									((EditText) textEntryView).setText("");
									
								}
							}).create();
		case LOAD_CHAT:
			Cursor c = getContentResolver().query(SERVICE_URI, new String[] { "chat" }, null, null, null);
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				
				String chatName=  c.getString(0);
				Log.i(LOG_TAG, chatName );
				names.add(chatName);
				
			}
			String chatNames []= new String [ names.size()];
			chatNames = names.toArray(chatNames);

			// This example shows how to add a custom layout to an AlertDialog

			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Chose chat for edit");
			adb.setSingleChoiceItems(chatNames, -1, myClickListener);
			adb.setPositiveButton("chose", myClickListener);
			return adb.create();

		}
		return null;
	}

	DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			
			sc2channels.setText("");
			twitchChannels.setText("");
			ListView lv = ((AlertDialog) dialog).getListView();
			if (which == Dialog.BUTTON_POSITIVE) {
				if (lv.getCheckedItemPosition()>=0){
					
				
				String[] projection = new String[] { "site", "channel" };
				String[] selectionArgs = new String[] { names.get(lv.getCheckedItemPosition())};
				Cursor c = getContentResolver()	.query(ADD_URI, projection,	" chat = ?", selectionArgs, null);
				setName.setText(names.get(lv.getCheckedItemPosition()));
				// выводим в лог позицию выбранного элемента
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

					String site = c.getString(0);
					if (site.equals("sc2tv")) 
						sc2channels.setText(sc2channels.getText().toString()
								+ " " + c.getString(1));
					
					if (site.equals("twitch"))
						twitchChannels.setText(twitchChannels.getText().toString()+ " " + c.getString(1));

				}
				}
			}

			
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btnSc2tvAdd:
			showDialog(SC2_DIALOG);
			break;
		case R.id.btnCleanSc2tv:
			sc2channels.setText("");

			break;
		case R.id.setChat:
			getContentResolver().delete(ADD_URI, "chat = ?", new String [] { setName.getText().toString()});
 
			if (saveChat() == 0){
				Toast.makeText(this, "Chat name exist or no channels identified", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Chat saved", Toast.LENGTH_SHORT).show();
				 intent = new Intent();
				intent.putExtra("name", setName.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		case R.id.btnTwitchAdd:
			dlg2 = new DialogTwitchChannels();
	    	dlg2.show(getSupportFragmentManager(), "dlg2");	
			break;
		case R.id.btnCleanTwitch:
			twitchChannels.setText("");
			break;
		case 2:

			showDialog(LOAD_CHAT);
			break;
		case 3:

			intent.putExtra("name", setName.getText().toString());
			intent.putExtra("action", MainActivity.DELETE);	
			getContentResolver().delete(ADD_URI, "chat = ?", new String [] { setName.getText().toString()});
			setResult(RESULT_OK, intent);
			finish();
		default:
			break;
		}
	};

	/**
	 * метод сохраняет изменные данные чата из текстовых полей в файл-чат
	 * 
	 * @return название нового чата
	 */
	public int saveChat() {
		int j = 0;
		String chatName = setName.getText().toString();
		String[] name = { chatName };
		Cursor c = getContentResolver().query(ADD_URI, null, "chat = ?", name,
				null);
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put("chat", chatName);
			cv.put("flag", "true");
			cv.put("color", "");
			// тут можно добавить сайты
			String sc2tv = sc2channels.getText().toString();
			
			
			
			
				cv.put("site", "sc2tv");

				String channelBySite[] = sc2tv.split("\\s+");
				for (String s : channelBySite) {

					if (!s.equals("")) {
						cv.put("channel", s);
						Log.i(LOG_TAG, "строка" + s);
						
						Uri newUri = getContentResolver().insert(ADD_URI, cv);
						Log.d(LOG_TAG,
								"insert, result Uri : " + newUri.toString());

					}

				}
				String twitch = twitchChannels.getText().toString();
				
				
				cv.put("site", "twitch");

				String channelBySiteTwitch[] = twitch.split("\\s+");
				for (String s : channelBySiteTwitch) {

					if (!s.equals("")) {
						cv.put("channel", s);
						Log.i(LOG_TAG, "строка" + s);
						
						Uri newUri = getContentResolver().insert(ADD_URI, cv);
						Log.d(LOG_TAG,
								"insert, result Uri : " + newUri.toString());

					}

				}
				j = 1;

			

		} else {
			j = 0;

		}
		return j;
	}

	public static void twitchAdd(String channel) {
		// TODO Auto-generated method stub
		
		twitchChannels.setText(twitchChannels.getText().toString() + " " + channel);
	}

}
