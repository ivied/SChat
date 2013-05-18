package ivied.p001astreamchat;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

	// кол-во элементов
	@Override
	public int getCount() {
		return channels.size();
	}

	// элемент по позиции
	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	// id по позиции
	@Override
	public long getItemId(int position) {
		return position;
	}

	/// пункт списка
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
	 * // содержимое корзины ArrayList<Product> getBox() { ArrayList<Product>
	 * box = new ArrayList<Product>(); for (Product p : objects) { // если в
	 * корзине if (p.box) box.add(p); } return box; }
	 */

}