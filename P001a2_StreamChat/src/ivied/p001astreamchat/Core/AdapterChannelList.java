package ivied.p001astreamchat.Core;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ivied.p001astreamchat.R;

public class AdapterChannelList extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<AddChatChannel> channels;

	AdapterChannelList(Context context, ArrayList<AddChatChannel> channels) {
		ctx = context;
		this.channels = channels;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// ���-�� ���������
	@Override
	public int getCount() {
		return channels.size();
	}

	// ������� �� �������
	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	// id �� �������
	@Override
	public long getItemId(int position) {
		return position;
	}

	/// ����� ������
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		AddChatChannel channel = (AddChatChannel) getItem(position);

		convertView = lInflater.inflate(R.layout.add_chat_item_for_adapter,
				parent, false);
		convertView.setBackgroundColor(channel.color);

		TextView textChannel = (TextView) convertView
				.findViewById(R.id.textChannelNames);
		textChannel.setText(Html.fromHtml(channel.channelId + "<br>" + "<\br>"
				+ channel.name + "<\br>"));

		((ImageView) convertView.findViewById(R.id.imageSite))
				.setImageResource(channel.drawable);

		return convertView;
	}

	/*
	 * // ���������� ������� ArrayList<Product> getBox() { ArrayList<Product>
	 * box = new ArrayList<Product>(); for (Product p : objects) { // ���� �
	 * ������� if (p.box) box.add(p); } return box; }
	 */

}