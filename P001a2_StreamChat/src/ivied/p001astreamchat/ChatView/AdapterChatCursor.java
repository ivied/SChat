package ivied.p001astreamchat.ChatView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.Sc2tv.Sc2tv;
import ivied.p001astreamchat.Sites.Site;


public class AdapterChatCursor extends SimpleCursorAdapter {
	final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	 // Span to set text BOLD

    FactorySite factorySite = new FactorySite();


    //static Map<Integer, Integer> linkMap= new HashMap<Integer,Integer>();
	public  static List<Integer> linkMap = new ArrayList<Integer>();
	public AdapterChatCursor(Context context, int _layout, Cursor cursor,
			String[] from, int[] to, int flags) {
		super(context, _layout, cursor, from, to, flags);

		
		
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		TextView message = (TextView) view.findViewById(R.id.tvText);
		String nick = cursor.getString(4);

        Site site = factorySite.getSite(FactorySite.SiteName.valueOf(cursor.getString(2)));
        linkMap.clear();
		Spannable text = site.getSmiledText(message.getText().toString(), nick);//getSmiledText(this.mContext, message.getText(), nick);
		message.setText(text);
		TextView channel = (TextView) view.findViewById(R.id.channelName);
		
		int color = cursor.getInt(7);
		
		channel.setBackgroundColor(color);
		if ( !MainActivity.showChannelsInfo){
			channel.setText("");
			channel.setBackgroundColor(0);
		}
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.message, parent, false);
		bindView(v, context, cursor);
		return v;
	}

	@Override
	public void setViewImage(ImageView v, String value) {

		super.setViewImage(v, value);

		if (MainActivity.showSiteLogo){
             Site site = factorySite.getSite(FactorySite.SiteName.valueOf(value.toUpperCase()));
            int logo = site.getMiniLogo();
			v.setImageResource(logo);


    	}
    }

}
