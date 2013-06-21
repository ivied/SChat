package ivied.p001astreamchat.AddChat;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ivied.p001astreamchat.R;

public class AdapterChannelToListView extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Channel> channels;

    AdapterChannelToListView(Context context, ArrayList<Channel> channels) {
        this.context = context;
        this.channels = channels;
        layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return channels.size();
    }


    @Override
    public Object getItem(int position) {
        return channels.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View viewChanging, ViewGroup viewParent) {
        Channel channel = (Channel) getItem(position);
        viewChanging = layoutInflater.inflate(R.layout.add_chat_item_for_adapter,
                viewParent, false);
        viewChanging.setBackgroundColor(channel.color);
        TextView textViewChannelNames = (TextView) viewChanging.findViewById(R.id.textChannelNames);
        textViewChannelNames.setText(Html.fromHtml(channel.preferName + "<br>" + "<\br>"
                + channel.channelId + "<\br>"));
        ((ImageView) viewChanging.findViewById(R.id.imageSite)).setImageDrawable(channel.drawable);
        return viewChanging;
    }



}