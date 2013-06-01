package ivied.p001astreamchat.Core;

import java.io.UnsupportedEncodingException;

import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Site;
import ivied.p001astreamchat.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
		OnClickListener, DialogColorPicker.OnColorChangedListener {
	private FragmentFindChannelSc2tv fragmentFindSc2tv;
	private FragmentFindChannelTwitch fragmentFindTwitch;
	DialogFragment dlgColorPicker;
	FragmentTransaction fTrans;
	TextView textColor;
	RadioButton radioStandartColor, radioPersonalColor;
	Button setChannel;
	EditText channelId, personalName;
	int color;
    FactorySite.SiteName site;
	Intent intent =new Intent();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_channel);
		intent = getIntent();
	
		site = (FactorySite.SiteName) intent.getSerializableExtra(DialogChoiceSite.SITE);
		fTrans = getSupportFragmentManager().beginTransaction();
		switch (site) {
		case SC2TV:
            FactorySite factorySite = new FactorySite();
            Site siteClass = factorySite.getSite(site);
			fragmentFindSc2tv = new FragmentFindChannelSc2tv();
			fTrans.add(R.id.frameToFragments, fragmentFindSc2tv);
			break;
		case TWITCH:
			fragmentFindTwitch = new FragmentFindChannelTwitch();
			fTrans.add(R.id.frameToFragments, fragmentFindTwitch);
			break;
		}
		
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
		switch (site) {
		case SC2TV:
		channelId =(EditText) fragmentFindSc2tv.getView().findViewById(R.id.editChannelNumberSc2tv);
		
		break;
		case TWITCH:
		channelId = (EditText) fragmentFindTwitch.getView().findViewById(R.id.editChannelTwitch);
		
		break;
		}
		String action = intent.getStringExtra(DialogChoiceSite.FOR); 
		if(action.equalsIgnoreCase("edit")){
			String name= intent.getStringExtra(AddChat.PERSONAL_NAME);
			personalName.setText(name);
			///if( !intent.getStringExtra(AddChat.COLOR).equalsIgnoreCase("0"))
			color = intent.getIntExtra(AddChat.COLOR, 0);
			textColor.setBackgroundColor(color);
			//Log.i(MainActivity.LOG_TAG,"site = " + channel.siteInt + " channel = " + channel.channelId + " color = " +color + " personal = " + name);
			
			
				
			
			channelId.setText(intent.getStringExtra(AddChat.CHANNEL));
		
	
		
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.radioStandartColor:
			getStandartColor ();
			
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
		int length = channelId.getText().toString().length();
		
		
		switch (site) {
		
		
		case SC2TV:
		if (!(length==0)){
		
		if (length>8) length=8;
		color = -Integer.parseInt(channelId.getText().toString().substring(0, length));
		Log.d(MainActivity.LOG_TAG, "color = "+ color+ "   ");
		}
		
		break;
		case TWITCH:
			if ((length>3)){
			try {
				String text =stringToHex (channelId.getText().toString()).substring(0, 6);
				Log.d(MainActivity.LOG_TAG, "color = "+ text);
				color = Color.parseColor("#"+text); 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			break;
		
		};
		textColor.setBackgroundColor(color);
		
		return color;
	}
	
	private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    public String stringToHex(String input) throws UnsupportedEncodingException
    {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }
 
   
 
    private String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
	@Override
	public void colorChanged(int color) {
		this.color = color;
		Log.d(MainActivity.LOG_TAG, "color = "+ color);
		textColor.setBackgroundColor(color);

	}

}
