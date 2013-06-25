package ivied.p001astreamchat.Sites.GoodGame;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ivied.p001astreamchat.AddChat.ChannelIdSelectedListener;
import ivied.p001astreamchat.AddChat.FragmentAddChannelStandard;
import ivied.p001astreamchat.R;

/**
 * Created by Serv on 13.06.13.
 */
public class FragmentCheckGG extends FragmentAddChannelStandard implements View.OnClickListener {
    ChannelIdSelectedListener mCallback;
    DialogFragment dlgTwitchChannels;
    EditText channel;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ChannelIdSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ChannelIdSelectedListener ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_check_goodgame_video, null);

        Button btnCheckChannelTwitch = (Button) v.findViewById(R.id.btnCheckGGStream);
        btnCheckChannelTwitch.setOnClickListener(this);

        channel = (EditText) v.findViewById(R.id.editChannelGG);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btnCheckGGStream):
                mCallback.pasteIdChannel(channel.getText().toString());
                break;

        }
    }



    @Override
    public EditText getEditTextChannel() {
        return channel;
    }


}

