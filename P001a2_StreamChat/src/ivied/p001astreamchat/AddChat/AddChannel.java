package ivied.p001astreamchat.AddChat;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AddChannel extends SherlockFragmentActivity implements
		OnClickListener, DialogColorPicker.OnColorChangedListener {

	FragmentTransaction fTrans;
	TextView textColor;
	RadioButton radioStandardColor, radioPersonalColor;
	Button setChannel;
	EditText channelId, personalName;
	int color;
    FactorySite.SiteName site;
    FactorySite factorySite = new FactorySite();
   	Intent intent =new Intent();
    FragmentAddChannelStandard fragment;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_channel);
		intent = getIntent();

        site = (FactorySite.SiteName) intent.getSerializableExtra(DialogChoiceSite.SITE);
		fTrans = getSupportFragmentManager().beginTransaction();
        fragment = factorySite.getSite(site).getFragmentAddChannel();
        fTrans.add(R.id.frameToFragments, fragment);
		fTrans.commit();
		textColor = (TextView) findViewById(R.id.textColor);
		radioStandardColor = (RadioButton) findViewById(R.id.radioStandartColor);
		radioStandardColor.setOnClickListener(this);
		radioPersonalColor = (RadioButton) findViewById(R.id.radioPersonalColor);
		radioPersonalColor.setOnClickListener(this);
		personalName = (EditText) findViewById(R.id.personalName);
		setChannel =(Button) findViewById(R.id.btnSetChannel);
		setChannel.setOnClickListener(this);
		
	}
	@Override
	public void onResume() {
		super.onResume();
		channelId = fragment.getEditTextChannel();
		String action = intent.getStringExtra(DialogChoiceSite.FOR); 
		if(action.equalsIgnoreCase("edit")){
			String name= intent.getStringExtra(AddChat.PERSONAL_NAME);
			personalName.setText(name);
			color = intent.getIntExtra(AddChat.COLOR, 0);
			textColor.setBackgroundColor(color);

			channelId.setText(intent.getStringExtra(AddChat.CHANNEL));

		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.radioStandartColor:
			getStandardColor();
			
			break;
		case R.id.radioPersonalColor:
			
			new DialogColorPicker(this, this, Color.MAGENTA).show();

			
			break;
		case R.id.btnSetChannel:
			
			Intent i =new Intent() ;
			String [] channel = channelId.getText().toString().trim().split("\\s+");
			
			if(!channel[0].equalsIgnoreCase("")){
			
			i.putExtra("channelId", channel[0]);
			if (color == 0 ) getStandardColor();
			i.putExtra("color", color);
			i.putExtra("name", personalName.getText().toString());
			i.putExtra("site", site);
			setResult(RESULT_OK, i);
			finish();
			
			}

			break;
		}
	}

	
	private int getStandardColor(){
		int length = channelId.getText().toString().length();
		String channel = channelId.getText().toString();
		color = factorySite.getSite(site).getColorForAdd(channel,length,color);
		textColor.setBackgroundColor(color);
		return color;
	}
	

	@Override
	public void colorChanged(int color) {
		this.color = color;
		textColor.setBackgroundColor(color);

	}

}
