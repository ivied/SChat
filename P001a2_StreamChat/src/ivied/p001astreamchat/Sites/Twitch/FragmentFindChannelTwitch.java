package ivied.p001astreamchat.Sites.Twitch;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.AddChat.SelectedListener;
import ivied.p001astreamchat.R;

public class FragmentFindChannelTwitch extends FragmentAddChannelStandard implements OnClickListener, SelectedListener {
	
	DialogFragment dlgTwitchChannels;
	EditText channel;
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.fragment_find_site_twitch, null);
		    
		    Button btnCheckChannelTwitch = (Button) v.findViewById(R.id.btnCheckChannelTwitch);
		    btnCheckChannelTwitch.setOnClickListener(this);
            Button btnFindByApi = (Button) v.findViewById(R.id.btnTwitchViaApi);
            btnFindByApi.setOnClickListener(this);
		    channel = (EditText) v.findViewById(R.id.editChannelTwitch);
		    return v;
		  }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btnCheckChannelTwitch):
                dlgTwitchChannels = new DialogFindTwitchChannel();
                dlgTwitchChannels.show(getFragmentManager(), "Check twitch");
                break;

            case R.id.btnTwitchViaApi:
                DialogFragment dialogSelectTwitch = new DialogTwitchTopGames();
                dialogSelectTwitch.show(getFragmentManager(),"Show Twitch games");
                break;
        }
    }



    @Override
    public EditText getEditTextChannel() {
        return channel;
    }

    @Override
    public void pasteChannel(String channel) {
        this.channel.setText(channel);
    }
}
