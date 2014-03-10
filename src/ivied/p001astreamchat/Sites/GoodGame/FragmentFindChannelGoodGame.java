package ivied.p001astreamchat.Sites.GoodGame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.R;

/**
 * Created by Serv on 24.06.13.
 */
public class FragmentFindChannelGoodGame extends FragmentAddChannelStandard{
    EditText channel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_site_goodgame, null);


        channel = (EditText) v.findViewById(R.id.EditTextGGChannelName);
        return v;
    }
    @Override
    public EditText getEditTextChannel() {
        return channel;
    }
}
