package ivied.p001astreamchat.AddChat;

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

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;

public class ViewAddChannel extends SherlockFragmentActivity implements
		OnClickListener, DialogColorPicker.OnColorChangedListener, ChannelIdSelectedListener {

	FragmentTransaction fragmentTransaction;
	TextView textColor;
	RadioButton radioStandardColor, radioPersonalColor;
	Button setChannel;
	EditText editTextChannelId, personalName;
	int color;
    FactorySite.SiteName site;
    FactorySite factorySite = new FactorySite();
   	Intent configData =new Intent();
    FragmentAddChannelStandard fragment;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_channel);
		configData = getIntent();

        site = (FactorySite.SiteName) configData.getSerializableExtra(DialogChoiceSite.SITE);
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = factorySite.getSite(site).getFragmentAddChannel();
        fragmentTransaction.add(R.id.frameToFragments, fragment);
		fragmentTransaction.commit();
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
        editTextChannelId = fragment.getEditTextChannel();
		String action = configData.getStringExtra(DialogChoiceSite.FOR);
		if(action.equalsIgnoreCase("edit")){
			String name= configData.getStringExtra(ViewAddChat.PERSONAL_NAME);
			personalName.setText(name);
			color = configData.getIntExtra(ViewAddChat.COLOR, 0);
			textColor.setBackgroundColor(color);

            editTextChannelId.setText(configData.getStringExtra(ViewAddChat.CHANNEL));

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
			String [] channel = editTextChannelId.getText().toString().trim().split("\\s+");
			
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
		int length = editTextChannelId.getText().toString().length();
		String channel = editTextChannelId.getText().toString();
		color = factorySite.getSite(site).getColorForAdd(channel,length,color);
		textColor.setBackgroundColor(color);
		return color;
	}
	

	@Override
	public void colorChanged(int color) {
		this.color = color;
		textColor.setBackgroundColor(color);

	}

    @Override
    public void pasteIdChannel(String channel) {
        editTextChannelId.setText(channel.toLowerCase());
    }
}
