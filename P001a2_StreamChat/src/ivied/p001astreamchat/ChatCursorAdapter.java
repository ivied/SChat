package ivied.p001astreamchat;

import java.io.UnsupportedEncodingException;
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
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  ласс приводит данные курсора к стандртному виду сообщени€
 * 
 * @author Serv
 * 
 */
public class ChatCursorAdapter extends SimpleCursorAdapter {
	final String SAVED_NAME = "sc2tv";
	final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
	
	 // Span to set text BOLD
	   final static StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
	   SharedPreferences preferences;
	 static String sc2tvNick;
	//static Map<Integer, Integer> linkMap= new HashMap<Integer,Integer>();
	static List<Integer> linkMap = new ArrayList<Integer>();
	public ChatCursorAdapter(Context context, int _layout, Cursor cursor,
			String[] from, int[] to, int flags) {
		super(context, _layout, cursor, from, to, flags);
		// TODO выделение личных сообщений
		preferences = context.getSharedPreferences("Login",
				0);
		sc2tvNick = preferences.getString(SAVED_NAME, "");
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		TextView message = (TextView) view.findViewById(R.id.tvText);
		String nick = cursor.getString(cursor
				.getColumnIndex(MyContentProvider.MESSAGES_NICK_NAME));
		// nick = "<font color=#4682B4>" + nick + ": " +
		// "</font> <font color=#ffffff>" + message.getText() + "</font>";
		// message.setText(Html.fromHtml(nick));

		Spannable text = getSmiledText(this.mContext, message.getText(), nick);
		message.setText(text);
		TextView channel = (TextView) view.findViewById(R.id.channelName);
		String channelText = channel.getText().toString();
		String site = cursor.getString(2);
		if (site.equals("sc2tv"))
		channel.setBackgroundColor(Color.parseColor("#"	+ "0"	+ channelText));
		String color=null;
		if (site.equals("twitch")) {
			
			 try {
				color = stringToHex(channelText).substring(0, 6);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			channel.setBackgroundColor(Color.parseColor("#"+color));
		}
		
		
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
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.message, parent, false);
		bindView(v, context, cursor);
		return v;
	}

	@Override
	public void setViewImage(ImageView v, String value) {
		// метод супер-класса
		super.setViewImage(v, value);
		// разрисовываем ImageView
		if (value.equals("sc2tv"))
			v.setImageResource(R.drawable.sc2tv);
		if (value.equals("twitch"))
			v.setImageResource(R.drawable.twitch);

	}

	private static final Factory spannableFactory = Spannable.Factory
			.getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {
		addPattern(emoticons, ":s:happy:", R.drawable.sc2tvsmilea);
		addPattern(emoticons, ":s:aws:", R.drawable.sc2tvsmileawesome);
		addPattern(emoticons, ":s:nc:", R.drawable.sc2tvsmilenocomments);
		addPattern(emoticons, ":s:manul:", R.drawable.sc2tvsmilemanul);
		addPattern(emoticons, ":s:crazy:", R.drawable.sc2tvsmilecrazy);
		addPattern(emoticons, ":s:cry:", R.drawable.sc2tvsmilecry);
		addPattern(emoticons, ":s:glory:", R.drawable.sc2tvsmileglory);
		addPattern(emoticons, ":s:kawai:", R.drawable.sc2tvsmilekawai);
		addPattern(emoticons, ":s:mee:", R.drawable.sc2tvsmilemee);
		addPattern(emoticons, ":s:omg:", R.drawable.sc2tvsmileomg);
		addPattern(emoticons, ":s:whut:", R.drawable.sc2tvsmilemhu);
		addPattern(emoticons, ":s:sad:", R.drawable.sc2tvsmilesad);
		addPattern(emoticons, ":s:spk:", R.drawable.sc2tvsmileslowpoke);
		addPattern(emoticons, ":s:hmhm:", R.drawable.sc2tvsmile2);
		addPattern(emoticons, ":s:mad:", R.drawable.sc2tvsmilemad);
		addPattern(emoticons, ":s:angry:", R.drawable.sc2tvsmileaangry);
		addPattern(emoticons, ":s:xd:", R.drawable.sc2tvsmileii);
		addPattern(emoticons, ":s:huh:", R.drawable.sc2tvsmilehuh);
		addPattern(emoticons, ":s:tears:", R.drawable.sc2tvsmilehappycry);
		addPattern(emoticons, ":s:notch:", R.drawable.sc2tvsmilenotch);
		addPattern(emoticons, ":s:vaga:", R.drawable.sc2tvsmilevaganych);
		addPattern(emoticons, ":s:ra:", R.drawable.sc2tvsmilera);
		addPattern(emoticons, ":s:fp:", R.drawable.sc2tvsmilefacepalm);
		addPattern(emoticons, ":s:neo:", R.drawable.sc2tvsmilesmith);
		addPattern(emoticons, ":s:peka:", R.drawable.sc2tvsmileminihappy);
		addPattern(emoticons, ":s:trf:", R.drawable.sc2tvsmiletrollface);
		addPattern(emoticons, ":s:fu:", R.drawable.sc2tvsmilefuuuu);
		addPattern(emoticons, ":s:why:", R.drawable.sc2tvsmilewhy);
		addPattern(emoticons, ":s:yao:", R.drawable.sc2tvsmileyao);
		addPattern(emoticons, ":s:fyeah:", R.drawable.sc2tvsmilefyeah);
		addPattern(emoticons, ":s:lucky:", R.drawable.sc2tvsmilelol);
		addPattern(emoticons, ":s:okay:", R.drawable.sc2tvsmileokay);
		addPattern(emoticons, ":s:alone:", R.drawable.sc2tvsmilealone);
		addPattern(emoticons, ":s:joyful:", R.drawable.sc2tvsmileewbte);
		addPattern(emoticons, ":s:wtf:", R.drawable.sc2tvsmilewtf);
		addPattern(emoticons, ":s:danu:", R.drawable.sc2tvsmiledaladno);
		addPattern(emoticons, ":s:gusta:", R.drawable.sc2tvsmilemegusta);
		addPattern(emoticons, ":s:bm:", R.drawable.sc2tvsmilebm);
		// page 2
		addPattern(emoticons, ":s:lol:", R.drawable.sc2tvsmileloool);
		addPattern(emoticons, ":s:notbad:", R.drawable.sc2tvsmilenotbad);
		addPattern(emoticons, ":s:rly:", R.drawable.sc2tvsmilereally);
		addPattern(emoticons, ":s:ban:", R.drawable.sc2tvsmilebanan);
		addPattern(emoticons, ":s:cap:", R.drawable.sc2tvsmilecap);
		addPattern(emoticons, ":s:br:", R.drawable.sc2tvsmilebr);
		addPattern(emoticons, ":s:fpl:", R.drawable.sc2tvsmileleefacepalm);
		addPattern(emoticons, ":s:ht:", R.drawable.sc2tvsmileheart);
		// face
		addPattern(emoticons, ":s:adolf:", R.drawable.sc2tvsmileadolf);
		addPattern(emoticons, ":s:bratok:", R.drawable.sc2tvsmilebratok);
		addPattern(emoticons, ":s:strelok:", R.drawable.sc2tvsmilestrelok);
		// addPattern(emoticons, ":s:white-ra:", R.drawable.sc2tvsmilewhitera);
		addPattern(emoticons, ":s:dimaga:", R.drawable.sc2tvsmiledimaga);
		addPattern(emoticons, ":s:bruce:", R.drawable.sc2tvsmilebruce);
		addPattern(emoticons, ":s:jae:", R.drawable.sc2tvsmilejaedong);
		addPattern(emoticons, ":s:flash:", R.drawable.sc2tvsmileflash1);
		addPattern(emoticons, ":s:bisu:", R.drawable.sc2tvsmilebisu);
		addPattern(emoticons, ":s:jangbi:", R.drawable.sc2tvsmilejangbi);
		addPattern(emoticons, ":s:idra:", R.drawable.sc2tvsmileidra);
		addPattern(emoticons, ":s:vdv:", R.drawable.sc2tvsmilevitya);
		addPattern(emoticons, ":s:imba:", R.drawable.sc2tvsmiledjigurda);
		addPattern(emoticons, ":s:chuck:", R.drawable.sc2tvsmilechan);
		addPattern(emoticons, ":s:tgirl:", R.drawable.sc2tvsmilebrucelove);
		addPattern(emoticons, ":s:top1sng:", R.drawable.sc2tvsmilehappy);
		addPattern(emoticons, ":s:slavik:", R.drawable.sc2tvsmileslavik);
		addPattern(emoticons, ":s:olsilove:", R.drawable.sc2tvsmileolsilove);
		addPattern(emoticons, ":s:kas:", R.drawable.sc2tvsmilekas);
		// other
		addPattern(emoticons, ":s:pool:", R.drawable.sc2tvsmilepool);
		addPattern(emoticons, ":s:ej:", R.drawable.sc2tvsmileejik);
		addPattern(emoticons, ":s:mario:", R.drawable.sc2tvsmilemario);
		addPattern(emoticons, ":s:tort:", R.drawable.sc2tvsmiletort);
		addPattern(emoticons, ":s:arni:", R.drawable.sc2tvsmileterminator);
		addPattern(emoticons, ":s:crab:", R.drawable.sc2tvsmilecrab);
		addPattern(emoticons, ":s:hero:", R.drawable.sc2tvsmileheroes3);
		addPattern(emoticons, ":s:mc:", R.drawable.sc2tvsmilemine);
		addPattern(emoticons, ":s:osu:", R.drawable.sc2tvsmileosu);
		addPattern(emoticons, ":s:q3:", R.drawable.sc2tvsmileq3);
		// page 3
		addPattern(emoticons, ":s:tigra:", R.drawable.sc2tvsmiletigrica);
		addPattern(emoticons, ":s:volck:", R.drawable.sc2tvsmilevoolchik1);
		addPattern(emoticons, ":s:hpeka:", R.drawable.sc2tvsmileharupeka);
		addPattern(emoticons, ":s:slow:", R.drawable.sc2tvsmilespok);
		addPattern(emoticons, ":s:alex:", R.drawable.sc2tvsmilealfi);
		addPattern(emoticons, ":s:panda:", R.drawable.sc2tvsmilepanda);
		addPattern(emoticons, ":s:sun:", R.drawable.sc2tvsmilesunl);
		addPattern(emoticons, ":s:cou:", R.drawable.sc2tvsmilecougar);
		addPattern(emoticons, ":s:wb:", R.drawable.sc2tvsmilewormban);
		addPattern(emoticons, ":s:dobro:", R.drawable.sc2tvsmiledobre);
		addPattern(emoticons, ":s:theweedle:", R.drawable.sc2tvsmileweedle);
		addPattern(emoticons, ":s:apc:", R.drawable.sc2tvsmileapochai);
		addPattern(emoticons, ":s:globus:", R.drawable.sc2tvsmileglobus);
		addPattern(emoticons, ":s:cow:", R.drawable.sc2tvsmilecow);
		addPattern(emoticons, ":s:nook:", R.drawable.sc2tvsmilenookay);
		addPattern(emoticons, ":s:noj:", R.drawable.sc2tvsmileknife);
		addPattern(emoticons, ":s:fpd:", R.drawable.sc2tvsmilefp);
		addPattern(emoticons, ":s:hg:", R.drawable.sc2tvsmilehg);
		// anime
		addPattern(emoticons, ":s:yoko:", R.drawable.sc2tvsmileyoko);
		addPattern(emoticons, ":s:miku:", R.drawable.sc2tvsmilemiku);
		addPattern(emoticons, ":s:winry:", R.drawable.sc2tvsmilewinry);
		addPattern(emoticons, ":s:asuka:", R.drawable.sc2tvsmileasuka);
		addPattern(emoticons, ":s:konata:", R.drawable.sc2tvsmilekonata);
		addPattern(emoticons, ":s:reimu:", R.drawable.sc2tvsmilereimu);
		addPattern(emoticons, ":s:sex:", R.drawable.sc2tvsmilesex);
		addPattern(emoticons, ":s:mimo:", R.drawable.sc2tvsmilemimo);
		addPattern(emoticons, ":s:fire:", R.drawable.sc2tvsmilefire);
		addPattern(emoticons, ":s:mandarin:", R.drawable.sc2tvsmilemandarin);
		addPattern(emoticons, ":s:grafon:", R.drawable.sc2tvsmilegrafon);
		addPattern(emoticons, ":s:epeka:", R.drawable.sc2tvsmileepeka);
		addPattern(emoticons, ":s:opeka:", R.drawable.sc2tvsmileopeka);
		addPattern(emoticons, ":s:ocry:", R.drawable.sc2tvsmileocry);
		addPattern(emoticons, ":s:neponi:", R.drawable.sc2tvsmileneponi);
		addPattern(emoticons, ":s:moon:", R.drawable.sc2tvsmilemoon);
		addPattern(emoticons, ":s:ghost:", R.drawable.sc2tvsmilegay);
		addPattern(emoticons, ":s:omsk:", R.drawable.sc2tvsmileomsk);
		addPattern(emoticons, ":s:grumpy:", R.drawable.sc2tvsmilegrumpy);

		// addPattern(emoticons, ":-)", R.drawable.emo_im_happy);
		// ...
	}

	/**
	 * метод заполн€ет хэшмап парами текст-смайл»д
	 * 
	 * @param map
	 * @param smile
	 * @param resource
	 */
	private static void addPattern(Map<Pattern, Integer> map, String smile,
			int resource) {
		map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * добавл€ет в spannable строку смайлы и подсвечивает никнэйм
	 * 
	 * @param context
	 * @param spannable
	 *            вс€ строка сообщени€ включа€ ник
	 * @param length
	 *            дл€ ника
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable,
			int length, int lengthAdress, boolean privateM) {
		boolean hasChanges = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			Matcher matcher = entry.getKey().matcher(spannable);
			while (matcher.find()) {
				boolean set = true;
				for (ImageSpan span : spannable.getSpans(matcher.start(),
						matcher.end(), ImageSpan.class))
					if (spannable.getSpanStart(span) >= matcher.start()
							&& spannable.getSpanEnd(span) <= matcher.end())
						spannable.removeSpan(span);
					else {
						set = false;
						break;
					}

				hasChanges = true;
				spannable.setSpan(new ImageSpan(context, entry.getValue()),
						matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			}
		}
		spannable.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.nick)), 0, length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if (lengthAdress > 0){
			spannable.setSpan(bss, length+1, length + 1 + lengthAdress, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			if (privateM) {	spannable.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(R.color.private_msg)), length,
						spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		 
	/*	Collection<Integer> link = linkMap.values();
		Iterator linkIter = link.iterator();
		Integer linkClose = null;
		int linkStart = 0;
		while (linkIter.hasNext()) {
			linkClose = (Integer) linkIter.next();
			
			for (Entry<Integer, Integer> entry : linkMap.entrySet()) {
		        if (linkClose.equals(entry.getValue())) {
		            linkStart = entry.getKey();
		        }
		        
		}
			Log.i(MainActivity.LOG_TAG, "линк "+  linkClose+ " " + linkStart);
		}
		*/
		    for (Integer startOfLink : linkMap){
		    	spannable.setSpan(new UnderlineSpan(), length + 1 + startOfLink ,
		    			length + 1  + startOfLink + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	spannable.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(R.color.link)), length + 1 + startOfLink,
						length + 1  + startOfLink + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		    	
		    }
		return hasChanges;
	}

	/**
	 * выдает spannable дл€ строки cursoradapter
	 * 
	 * @param context
	 * @param text
	 *            текст основного сообщени€
	 * @param nick
	 * @return
	 */
	public static Spannable getSmiledText(Context context, CharSequence text,
			CharSequence nick) {
		// TODO выодить spannable объект с переработкой <b> тегов
		linkMap.clear();
		int adressLength = 0;
		boolean privateM = false;
		Matcher matcher = bold.matcher(text);
		String message = text.toString();
		if (matcher.find()) {
		message = message.replace("<b>", "").replace("</b>", "");
		String privateNick = sc2tvNick;
		String adress = matcher.group(2);
		privateM = adress.equalsIgnoreCase(privateNick);
		adressLength = matcher.group(2).length();
		}
		
		matcher = ActionProviderLink.URL.matcher(message);
		while (matcher.find()){
		
		linkMap.add(matcher.start());	
		message = message.replace(matcher.group(), "link");
		matcher = ActionProviderLink.URL.matcher(message);
		}
		
		Spannable spannable = spannableFactory.newSpannable(nick + ": " + message);
		int length = nick.length() + 1;

		addSmiles(context, spannable, length, adressLength, privateM);

		return spannable;
	}
}
