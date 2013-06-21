package ivied.p001astreamchat.AddChat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import java.util.ArrayList;

import ivied.p001astreamchat.AddChat.ViewQRReader.ViewQRReader;
import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;

public class ViewAddChat extends SherlockFragmentActivity implements OnClickListener, OnItemClickListener{
    static final int TASK_ADD = 1;
    static final int TASK_EDIT = 2;
    static final int TASK_ADD_VIDEO = 3;
    static final int TASK_EDIT_VIDEO = 4;
    static final int SAVE_CHAT_NAME_EXIST = 0;
    static final int SAVE_CHAT_ADD_COMPLETE = 1;
    static final int SAVE_CHAT_NAME_EMPTY = 2;
    static final int SAVE_CHAT_NEED_CHANNELS = 3;
    static final int BTN_LOAD_CHAT = 2;
    static final int BTN_CLOSE_CHAT = 3;

    static final String CHANNEL = "channel";
    static final String COLOR = "color";
    static final String PERSONAL_NAME = "personal";
    final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
    Button closeChat,loadSavedChats,setChat;
    LinearLayout mLayout;
	private EditText setNameEditText;
	private ImageButton addChannel;
	DialogChoiceSite dialogChoice;
	DialogFragment dlgLoadSavedChat;
	ListView channelList;
	private ActionMode mMode;
	protected static int _id;
	String button;
	 ArrayList<Channel> channels =new ArrayList<Channel>();
	 AdapterChannelToListView adapter;
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
		setNameEditText = (EditText) findViewById(R.id.chatName);
		channelList = (ListView) findViewById(R.id.listChannels);
		 adapter = new AdapterChannelToListView(this, channels);
		 channelList.setAdapter(adapter);
		 channelList.setOnItemClickListener(this);
		
		if (button.equalsIgnoreCase("Edit")){
			
			closeChat = new Button(this);
			loadSavedChats = new Button(this);
			LinearLayout.LayoutParams 	lParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.2f);
			setChat.setLayoutParams(lParams);
			closeChat.setText("Close Chat");
			closeChat.setId(BTN_CLOSE_CHAT);
			mLayout.addView(closeChat, 0);
			closeChat.setLayoutParams(lParams);
			closeChat.setOnClickListener(this);
			lParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.6f);
			
			
			loadSavedChats.setText("Load chat");
			loadSavedChats.setId( BTN_LOAD_CHAT);
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
			dialogChoice = DialogChoiceSite.newInstance(channels);
			dialogChoice.show(getSupportFragmentManager(), "Smile");
			break;
		case R.id.setChat:
			if (button.equalsIgnoreCase("Edit")){
				getContentResolver().delete
				(ADD_URI, "chat = ?", new String [] {  setNameEditText.getText().toString()});
				intent.putExtra("action", MainActivity.EDIT);
			}
			//Add "action"   "edit" in configData
			switch (saveChat()) {
			
			case SAVE_CHAT_NAME_EXIST :
				Toast.makeText(this, getString(R.string.toast_chat_name_exist), Toast.LENGTH_SHORT)
						.show();
				break;
			case SAVE_CHAT_ADD_COMPLETE:
				Toast.makeText(this, getString(R.string.toast_chat_saved), Toast.LENGTH_SHORT).show();
				
				  intent.putExtra("name",
                          setNameEditText.getText().toString()); setResult(RESULT_OK, intent);
				  finish();

				break;
			case SAVE_CHAT_NAME_EMPTY:
				Toast.makeText(this, getString(R.string.toast_type_chat_name), Toast.LENGTH_SHORT).show();
				break;
			case SAVE_CHAT_NEED_CHANNELS:
				Toast.makeText(this, getString(R.string.toast_add_some_channels), Toast.LENGTH_SHORT)
				.show();
				break;
			}
			
			break;
		case  BTN_LOAD_CHAT:
			dlgLoadSavedChat = new DialogLoadSavedChat();
			dlgLoadSavedChat.show(getSupportFragmentManager(), "Load chat");
			break;
		case BTN_CLOSE_CHAT:
			getContentResolver().delete
			(ADD_URI, "chat = ?", new String [] {  setNameEditText.getText().toString()});
			intent.putExtra("name", setNameEditText.getText().toString());
			intent.putExtra("action", MainActivity.DELETE);	
			
			setResult(RESULT_OK, intent);
			finish();
			break;
		}


	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_OK:
                String channel =data.getStringExtra("channelId");
                int color = data.getIntExtra("color", Color.MAGENTA);
                String personalName = data.getStringExtra("name");
                switch (requestCode) {
                    case TASK_ADD_VIDEO:
                        FactoryVideoViewSetter.VideoSiteName siteVideo = (FactoryVideoViewSetter.VideoSiteName) data.getSerializableExtra(DialogChoiceSite.SITE);
                        channels.add ( new Channel(channel,color,personalName,siteVideo));
                        break;
                    case TASK_EDIT_VIDEO:
                        siteVideo = (FactoryVideoViewSetter.VideoSiteName) data.getSerializableExtra(DialogChoiceSite.SITE);
                        channels.set (_id, new Channel(channel,color,personalName,siteVideo));
                        break;

                    case TASK_ADD:

                        FactorySite.SiteName site = (FactorySite.SiteName) data.getSerializableExtra(DialogChoiceSite.SITE);

                        channels.add( new Channel(channel,color,personalName,site));

                        break;

                    case TASK_EDIT:

                        site = (FactorySite.SiteName) data.getSerializableExtra(DialogChoiceSite.SITE);

                        channels.set(_id, new Channel(channel,color,personalName,site));

                        break;

                }
            case RESULT_FIRST_USER:
                setNameEditText.setText(data.getStringExtra(ViewQRReader.CHAT_NAME));
                for (Channel channelFromQR: ViewQRReader.channels){
                    channels.add(channelFromQR);
                }
                break;

        }
        adapter.notifyDataSetChanged();
    }


	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

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
		Channel channel = channels.get(id);
        int task;
        Intent intent;
        if (channel.siteInt != null){
            intent = new Intent( this, ViewAddChannel.class);
            intent.putExtra(DialogChoiceSite.SITE, channel.siteInt);
            task = TASK_EDIT;
        }else {
            intent = new Intent( this, ViewAddVideoChannel.class);
            intent.putExtra(DialogChoiceSite.SITE, channel.siteVideoInt);
            task = TASK_EDIT_VIDEO;
        }
        intent.putExtra(DialogChoiceSite.FOR, "edit");
		intent.putExtra(CHANNEL, channel.channelId);
		intent.putExtra(COLOR, channel.color);
		intent.putExtra(PERSONAL_NAME, channel.preferName);
		
		startActivityForResult(intent, task);
		
	}
	
	public int saveChat() {

		String chatName = setNameEditText.getText().toString();
		if (chatName.equalsIgnoreCase(""))
			return SAVE_CHAT_NAME_EMPTY;
		String[] name = { chatName };
		Cursor c = getContentResolver().query(ADD_URI, null, "chat = ?", name,
				null);
		if (c.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put("chat", chatName);

            if (channels.isEmpty()) return SAVE_CHAT_NEED_CHANNELS;
			for (Channel channel : channels) {
                if (channel.site.contains("tream")){
                    cv.put("flag", "false");
                }else{
                    cv.put("flag", "true");
                }
				cv.put("site", channel.site);
				cv.put("channel", channel.channelId);
				cv.put("color", channel.color);
				cv.put("personal", channel.preferName);

				getContentResolver().insert(ADD_URI, cv);
				}
            c.close();
            return SAVE_CHAT_ADD_COMPLETE;
		} else {
			return SAVE_CHAT_NAME_EXIST ;

		}

	}
	
	void loadSavedChat( ArrayList<Channel> channels_,String chatName){
		channels = channels_;

		adapter= new AdapterChannelToListView(this, channels);
		 channelList.setAdapter(adapter);
		 channelList.setOnItemClickListener(this);
		setNameEditText.setText(chatName);
		
	}

}
