package ivied.p001astreamchat;

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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * @author Serv класс создает активити для определения(задания) нового чата
 */
//TODO пробел в конце добавления чата
public class AddChat extends SherlockFragmentActivity implements OnClickListener {
	final String LOG_TAG = "myLogs";
	DialogTwitchChannels dlg2;
	final int SC2DIALOG = 1;
	ImageButton addSc2, addTwitch;
	EditText sc2channels;
	EditText setName;
	Button clearSc2tv;
	Button setChat;
	String newchannelAdd = "ded";
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_chat);
		addSc2 = (ImageButton) findViewById(R.id.btnSc2tvAdd);
		addSc2.setOnClickListener(this);
		addTwitch = (ImageButton) findViewById(R.id.btnTwitchAdd);
		addTwitch.setOnClickListener(this);

		clearSc2tv = (Button) findViewById(R.id.btnCleanSc2tv);
		clearSc2tv.setOnClickListener(this);
		setChat = (Button) findViewById(R.id.setChat);
		setChat.setOnClickListener(this);
		sc2channels = (EditText) findViewById(R.id.sc2tvChannels);
		setName = (EditText) findViewById(R.id.chatName);
	};

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SC2DIALOG:

			// This example shows how to add a custom layout to an AlertDialog
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory
					.inflate(R.layout.sc2choise, null);

			return new AlertDialog.Builder(AddChat.this)
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
									GetSc2tvCode newchannelCode = new GetSc2tvCode();
									newchannelCode.execute(newchannel);
									Log.i(LOG_TAG, "1");
									try {
										newchannel = newchannelCode.get();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ExecutionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									sc2channels.setText(sc2channels.getText()
											.toString() + " " + newchannel);
									Log.i(LOG_TAG, "2");
									((EditText) textEntryView).setText("");
									Log.i(LOG_TAG, "3");
								}
							}).create();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSc2tvAdd:
			showDialog(SC2DIALOG);
			break;
		case R.id.btnCleanSc2tv:
			sc2channels.setText("");

			break;
		case R.id.setChat:
			
			if (saveChat() == 0){
				Toast.makeText(this, "Chat name exist or no channels identified", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Chat saved", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("name", setName.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		
			break;
			
		
		default:
			break;
		}
	};

	/**
	 * метод сохраняет данные из текстовых полей в файл-чат
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
			// тут можно добавить сайты
			String sc2tv = sc2channels.getText().toString();
			sc2tv.trim();
			if (!sc2tv.equals("")) {
				cv.put("site", "sc2tv");

				String channelBySite[] = sc2tv.split("\\s+");
				for (String s : channelBySite) {

					if (!s.equals("")) {
						cv.put("channel", s);
						Log.i(LOG_TAG, "строка" + s);
						cv.put("flag", "false");
						cv.put("color", "");
						Uri newUri = getContentResolver().insert(ADD_URI, cv);
						Log.d(LOG_TAG,
								"insert, result Uri : " + newUri.toString());

					}

				}
				j = 1;

			}

		} else {
			j = 0;

		}
		return j;
	}

}
