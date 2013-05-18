package ivied.p001astreamchat;

import ivied.p001astreamchat.DialogColorPicker.OnColorChangedListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AddChannel extends SherlockFragmentActivity implements
		OnClickListener, OnColorChangedListener {
	private FragmentFindChannelSc2tv fragmentFind;
	DialogFragment dlgColorPicker;
	FragmentTransaction fTrans;
	TextView textColor;
	RadioButton radioStandartColor, radioPersonalColor;
	Button setChannel;
	EditText channelId, personalName;
	int color, site;
	Intent intent =new Intent();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_channel);
		intent = getIntent();
	
		site = intent.getIntExtra(DialogChoiceSite.SITE, 0);
		fTrans = getSupportFragmentManager().beginTransaction();
		switch (site) {
		case DialogChoiceSite.SC2TV:
			
			fragmentFind = new FragmentFindChannelSc2tv();
			
			break;

		}
		fTrans.add(R.id.frameToFragments, fragmentFind);
			fTrans.commit();
		textColor = (TextView) findViewById(R.id.textColor);
		radioStandartColor = (RadioButton) findViewById(R.id.radioStandartColor);
		radioStandartColor.setOnClickListener(this);
		radioPersonalColor = (RadioButton) findViewById(R.id.radioPersonalColor);
		radioPersonalColor.setOnClickListener(this);
		personalName = (EditText) findViewById(R.id.personalName);
		setChannel =(Button) findViewById(R.id.btnSetChannel);
		setChannel.setOnClickListener(this);
		
	}
	@Override
	public void onResume() {
		super.onResume();
		String action = intent.getStringExtra(DialogChoiceSite.FOR); 
		if(action.equalsIgnoreCase("edit")){
			String name= intent.getStringExtra(AddChat.PERSONAL_NAME);
			personalName.setText(name);
			///if( !intent.getStringExtra(AddChat.COLOR).equalsIgnoreCase("0"))
			color = intent.getIntExtra(AddChat.COLOR, 0);
			textColor.setBackgroundColor(color);
			//Log.i(MainActivity.LOG_TAG,"site = " + channel.siteInt + " channel = " + channel.channelId + " color = " +color + " personal = " + name);
			
			switch (site) {
			case DialogChoiceSite.SC2TV:
				channelId =(EditText) fragmentFind.getView().findViewById(R.id.editChannelNumberSc2tv);
				
				break;
				
			}
			channelId.setText(intent.getStringExtra(AddChat.CHANNEL));
		}else  {
		switch (site){
			case DialogChoiceSite.SC2TV:
				channelId =(EditText) fragmentFind.getView().findViewById(R.id.editChannelNumberSc2tv);
				break;
				
		}
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.radioStandartColor:
			switch (site) {
			case DialogChoiceSite.SC2TV:
				
				
				getStandartColor ();
				
				break;
			//case DialogChoiceSite.
			}
			
			break;
		case R.id.radioPersonalColor:
			
			new DialogColorPicker(this, this, Color.MAGENTA).show();

			
			break;
		case R.id.btnSetChannel:
			
			Intent i =new Intent() ;
			String [] channel = channelId.getText().toString().trim().split("\\s+");
			
			if(!channel[0].equalsIgnoreCase("")){
			
			i.putExtra("channelId", channel[0]);
			if (color == 0 ) getStandartColor();
			i.putExtra("color", color);
			i.putExtra("name", personalName.getText().toString());
			i.putExtra("site", site);
			setResult(RESULT_OK, i);
			finish();
			
			}
			Log.d(MainActivity.LOG_TAG, channel[0]);
			break;
		}
		// TODO Auto-generated method stub

	}

	
	private int getStandartColor (){
		switch (site) {
		case DialogChoiceSite.SC2TV:
			
			
		
			
			
		int length = channelId.getText().toString().length();
		
		if (length>8) length=8;
		if (!(length==0)){
		color = -Integer.parseInt(channelId.getText().toString().substring(0, length));
		Log.d(MainActivity.LOG_TAG, "color = "+ color+ "   ");
		textColor.setBackgroundColor(color);}
		
		break;
		//case DialogChoiceSite.
		}
		return color;
	}
	@Override
	public void colorChanged(int color) {
		this.color = color;
		Log.d(MainActivity.LOG_TAG, "color = "+ color);
		textColor.setBackgroundColor(color);

	}

}
