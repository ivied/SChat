package ivied.p001astreamchat.Core;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.ActionMenuView.LayoutParams;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;

public class AddChat extends SherlockFragmentActivity implements OnClickListener, OnItemClickListener{
	 static final int TASK_ADD = 1;
	 static final int TASK_EDIT = 2;
	 static final String CHANNEL = "channel";
	 static final String COLOR = "color";
	 static final String PERSONAL_NAME = "personal";
	 final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	Button closeChat,loadSavedChats,setChat;
	LinearLayout mLayout;
	private EditText setName;
	private ImageButton addChannel;
	DialogChoiceSite dialogChoice;
	DialogFragment dlgLoadSavedChat;
	ListView channelList;
	private ActionMode mMode;
	protected static int _id;
	String button;
	 ArrayList<AddChatChannel> channels =new ArrayList<AddChatChannel>();
	 AdapterChannelList adapter;
	protected void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		button = i.getStringExtra("button");
		setContentView(R.layout.add_chat);
		setChat= (Button) findViewById(R.id.setChat);
		setChat.setOnClickListener(this);
		addChannel = (ImageButton) findViewById(R.id.btnAddChannel);
		addChannel.setOnClickListener(this);
		mLayout = (LinearLayout) findViewById(R.id.layoutControlEdit);
		setName = (EditText) findViewById(R.id.chatName);
		channelList = (ListView) findViewById(R.id.listChannels);
		 adapter = new AdapterChannelList(this, channels);
		 channelList.setAdapter(adapter);
		 channelList.setOnItemClickListener(this);
		
		if (button.equalsIgnoreCase("Edit")){
			
			closeChat = new Button(this);
			loadSavedChats = new Button(this);
			LinearLayout.LayoutParams 	lParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.2f);
			setChat.setLayoutParams(lParams);
			closeChat.setText("Close Chat");
			closeChat.setId(3);
			mLayout.addView(closeChat, 0);
			closeChat.setLayoutParams(lParams);
			closeChat.setOnClickListener(this);
			lParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.6f);
			
			
			loadSavedChats.setText("Load chat");
			loadSavedChats.setId(2);
			loadSavedChats.setLayoutParams(lParams);
			mLayout.addView(loadSavedChats, 1);
			
			loadSavedChats.setOnClickListener(this);
			}
			
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btnAddChannel:
			dialogChoice = new DialogChoiceSite();
			dialogChoice.show(getSupportFragmentManager(), "Smile");
			break;
		case R.id.setChat:
			if (button.equalsIgnoreCase("Edit")){
				getContentResolver().delete
				(ADD_URI, "chat = ?", new String [] {  setName.getText().toString()});
				intent.putExtra("action", MainActivity.EDIT);
			}
			//Add "action"   "edit" in intent  	
			switch (saveChat()) {
			
			case 0:
				Toast.makeText(this, "Chat name exist", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				Toast.makeText(this, "Chat saved", Toast.LENGTH_SHORT).show();
				
				  intent.putExtra("name",
				  setName.getText().toString()); setResult(RESULT_OK, intent);
				  finish();
				 

				break;
			case 2:
				Toast.makeText(this, "Type chat name plz", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(this, "Add some channels plz", Toast.LENGTH_SHORT)
				.show();
				break;
			}
			
			break;
		case 2:
			dlgLoadSavedChat = new DialogLoadSavedChat();
			dlgLoadSavedChat.show(getSupportFragmentManager(), "Load chat");
			break;
		case 3:
			getContentResolver().delete
			(ADD_URI, "chat = ?", new String [] {  setName.getText().toString()});
			intent.putExtra("name", setName.getText().toString());
			intent.putExtra("action", MainActivity.DELETE);	
			
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// запишем в лог значения requestCode и resultCode
		Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = "
				+ resultCode);
		// если пришло ОК
		if (resultCode == RESULT_OK) {
				String channel =data.getStringExtra("channelId");
				int color = data.getIntExtra("color", 0);
				String personalName = data.getStringExtra("name");
				FactorySite.SiteName site = (FactorySite.SiteName) data.getSerializableExtra(DialogChoiceSite.SITE);
				Log.i("myLogs", channel + " " +color + " " + personalName + " " + site );
			switch (requestCode) {
			case TASK_ADD:
				
				channels.add( new AddChatChannel(channel,color,personalName,site));
				
				
				
				break;
			case TASK_EDIT:
				channels.set(_id, new AddChatChannel(channel,color,personalName,site));
				
				break;
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(MainActivity.LOG_TAG, "chek" +id);
		_id=(int) id;
		if (mMode == null)	mMode = startActionMode(callback);
		else	 mMode.finish();
	
	}
	
	private ActionMode.Callback callback = new ActionMode.Callback() {

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.channel_refresh_menu, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {
			case (R.id.actionDeleteChannel):
				Log.i(MainActivity.LOG_TAG, "chek" +_id);
				channels.remove(_id);
				adapter.notifyDataSetChanged();
				break;
			case (R.id.actionEditChannel) :
				editChannel(_id);
				break;
				
			
			}
			mode.finish();
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {

			mMode =null;
		}
		
	};
	
	void editChannel (int id) {
		AddChatChannel channel = channels.get(id);
		Intent intent = new Intent( this, AddChannel.class);
		intent.putExtra(DialogChoiceSite.FOR, "edit");
		intent.putExtra(DialogChoiceSite.SITE, channel.siteInt);
		intent.putExtra(CHANNEL, channel.channelId);
		intent.putExtra(COLOR, channel.color);
		intent.putExtra(PERSONAL_NAME, channel.name);
		
		startActivityForResult(intent, AddChat.TASK_EDIT);
		
	}
	
	public int saveChat() {
		int j = 0;
		String chatName = setName.getText().toString();
		if (chatName.equalsIgnoreCase(""))
			return 2;
		String[] name = { chatName };
		Cursor c = getContentResolver().query(ADD_URI, null, "chat = ?", name,
				null);
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put("chat", chatName);
			cv.put("flag", "true");
			j=3;
			for (AddChatChannel channel : channels) {
				cv.put("site", channel.site);
				cv.put("channel", channel.channelId);
				cv.put("color", channel.color);
				cv.put("personal", channel.name);
				
				String[] channelExist = { chatName , channel.channelId };
				Cursor q = getContentResolver().query(ADD_URI, null, "chat = ? AND channel = ?", channelExist,
						null);
				if (q.getCount() == 0) {Uri newUri = getContentResolver().insert(ADD_URI, cv);
				Log.d(MainActivity.LOG_TAG,
						"insert, result Uri : " + newUri.toString());};
				
				j = 1;
			}

		} else {
			j = 0;

		}
		return j;
	}
	
	void loadSavedChat( ArrayList<AddChatChannel> channels_,String chatName){
		channels = channels_;
		Log.d(MainActivity.LOG_TAG,
				"channelId = " + channels.get(0).channelId);
		adapter= new AdapterChannelList(this, channels);
		 channelList.setAdapter(adapter);
		 channelList.setOnItemClickListener(this);
		setName.setText(chatName);
		
	}

}
