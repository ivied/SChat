package ivied.p001astreamchat;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.ActionMenuView.LayoutParams;

public class AddChat extends SherlockFragmentActivity implements OnClickListener{
	 static final int TASK_ADD = 1;
	Button closeChat,loadSavedChats,setChat;
	LinearLayout mLayout;
	private EditText setName;
	private ImageButton addChannel;
	DialogChoiceSite dialogChoice;
	GridView gridChannelList;
	 ArrayList<AddChatChannel> channels =new ArrayList<AddChatChannel>();
	 AdapterChannelList adapter;
	protected void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		String button = i.getStringExtra("button");
		setContentView(R.layout.add_chat);
		setChat= (Button) findViewById(R.id.setChat);
		setChat.setOnClickListener(this);
		addChannel = (ImageButton) findViewById(R.id.btnAddChannel);
		addChannel.setOnClickListener(this);
		mLayout = (LinearLayout) findViewById(R.id.layoutControlEdit);
		setName = (EditText) findViewById(R.id.chatName);
		gridChannelList = (GridView) findViewById(R.id.gridChannelList);
		 adapter = new AdapterChannelList(this, channels);
		gridChannelList.setAdapter(adapter);
		gridChannelList.setNumColumns(2);
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
			loadSavedChats.setId(1);
			loadSavedChats.setLayoutParams(lParams);
			mLayout.addView(loadSavedChats, 1);
			
			loadSavedChats.setOnClickListener(this);
			}
			
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnAddChannel:
			dialogChoice = new DialogChoiceSite();
			dialogChoice.show(getSupportFragmentManager(), "Smile");
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
			switch (requestCode) {
			case TASK_ADD:
				String channel =data.getStringExtra("channelId");
				String color = data.getStringExtra("color");
				String personalName = data.getStringExtra("name");
				int site = data.getIntExtra("site", 1);
				Log.i("myLogs", channel + " " +color + " " + personalName + " " + site );
				
				channels.add( new AddChatChannel(channel,color,personalName,site));
				channels.add( new AddChatChannel (null,null,null,0));
				adapter.notifyDataSetChanged();
				
				break;
			}
		}
	}

}
