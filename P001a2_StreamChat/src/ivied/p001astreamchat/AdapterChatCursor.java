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
import android.net.Uri;
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
public class AdapterChatCursor extends SimpleCursorAdapter {
	final String SAVED_SC2TV_NAME = "sc2tv";
	///final String SAVED_TWITCH_NAME = "twitch";
	final public static Pattern bold = Pattern.compile("(\\<b\\>)(.*)(\\<\\/b\\>)");
	final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
	 // Span to set text BOLD
	   final static StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
	   SharedPreferences preferences;
	 static String sc2tvNick;
	 //static String twitchNick;
	//static Map<Integer, Integer> linkMap= new HashMap<Integer,Integer>();
	static List<Integer> linkMap = new ArrayList<Integer>();
	public AdapterChatCursor(Context context, int _layout, Cursor cursor,
			String[] from, int[] to, int flags) {
		super(context, _layout, cursor, from, to, flags);
		// TODO выделение личных сообщений
		preferences = context.getSharedPreferences("Login",
				0);
		sc2tvNick = preferences.getString(SAVED_SC2TV_NAME, "");
		//twitchNick = preferences.getString(SAVED_TWITCH_NAME, "");
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		TextView message = (TextView) view.findViewById(R.id.tvText);
		String nick = cursor.getString(cursor
				.getColumnIndex(MyContentProvider.MESSAGES_NICK_NAME));
		// nick = "<font color=#4682B4>" + nick + ": " +
		// "</font> <font color=#ffffff>" + message.getText() + "</font>";
		//// message.setText(Html.fromHtml(nick));
	
		Spannable text = getSmiledText(this.mContext, message.getText(), nick);
		message.setText(text);
		TextView channel = (TextView) view.findViewById(R.id.channelName);
		String channelText = channel.getText().toString();
		String site = cursor.getString(2);
		Cursor c = MyApp.getContext().getContentResolver()
				.query(ADD_URI, new String [] { "personal"}, "site = ? AND channel = ?", 
				new String [] { site,channelText}, null);
		if (c.moveToNext()){String personal = c.getString(0);
		
		if (!personal.equalsIgnoreCase("")) channel.setText(personal);}
		if (site.equals("sc2tv"))
		channel.setBackgroundColor(Color.parseColor("#"	+ "0"	+ channelText));
		
		if (site.equals("twitch")) {
			String color=null;
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
			v.setImageResource(R.drawable.sc2tv_small);
		if (value.equals("twitch"))
			v.setImageResource(R.drawable.twitch_small);

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


        addPattern(emoticons, "Volcania", R.drawable.twitchefbcc231b2d2d20627x28);
        addPattern(emoticons, "DatSheffy", R.drawable.twitchbf13a0595ecf649c24x30);
        addPattern(emoticons, "JKanStyle", R.drawable.twitch3a7ee1bc0e5c9af021x27);
        addPattern(emoticons, "OptimizePrime", R.drawable.twitch41f8a86c4b15b5d822x27);
        addPattern(emoticons, "StoneLightning", R.drawable.twitch8b5aaae6e2409deb20x27);
        addPattern(emoticons, "TheRinger", R.drawable.twitch1903cc415afc404c20x27);
        addPattern(emoticons, "PazPazowitz", R.drawable.twitch521420789e1e93ef18x27);
        addPattern(emoticons, ":B)", R.drawable.twitch2cde79cfe74c616924x18);
        addPattern(emoticons, ":z", R.drawable.twitchb9cbb6884788aa6224x18);
        addPattern(emoticons, ":)", R.drawable.twitchebf60cd72f7aa60024x18);
        addPattern(emoticons, ":(", R.drawable.twitchd570c4b3b8d8fc4d24x18);
        addPattern(emoticons, ":P", R.drawable.twitche838e5e34d9f240c24x18);
        addPattern(emoticons, ";p", R.drawable.twitch3407bf911ad2fd4a24x18);
        addPattern(emoticons, "<3", R.drawable.twitch577ade91d46d7edc24x18);
        addPattern(emoticons, ":\\", R.drawable.twitch374120835234cb2924x18);
        addPattern(emoticons, ";)", R.drawable.twitchcfaf6eac72fe4de624x18);
        addPattern(emoticons, "R)", R.drawable.twitch0536d670860bf73324x18);
        addPattern(emoticons, "o_O", R.drawable.twitch8e128fa8dc1de29c24x18);
        addPattern(emoticons, ":D", R.drawable.twitch9f2ac5d4b53913d724x18);
        addPattern(emoticons, ":o", R.drawable.twitchae4e17f5b9624e2f24x18);
        addPattern(emoticons, ">(", R.drawable.twitchd31223e81104544a24x18);
        addPattern(emoticons, "WinWaker", R.drawable.twitchd4e971f7a6830e9530x30);
        addPattern(emoticons, "TriHard", R.drawable.twitch6407e6947eb69e2124x30);
        addPattern(emoticons, "EagleEye", R.drawable.twitch95eb8045e7ae63b818x27);
        addPattern(emoticons, "CougarHunt", R.drawable.twitch551cd64fc3d4590a21x27);
        addPattern(emoticons, "RedCoat", R.drawable.twitch6b8d1be08f244e9219x27);
        addPattern(emoticons, "BrokeBack", R.drawable.twitch35ae4e0e8dd045e122x27);
        addPattern(emoticons, "Kappa", R.drawable.twitchddc6e3a8732cb50f25x28);
        addPattern(emoticons, "JonCarnage", R.drawable.twitch6aaca644ea5374c620x27);
        addPattern(emoticons, "PicoMause", R.drawable.twitchce027387c35fb60122x27);
        addPattern(emoticons, "MrDestructoid", R.drawable.twitchac61a7aeb52a49d339x27);
        addPattern(emoticons, "BCWarrior", R.drawable.twitch1e3ccd969459f88929x27);
        addPattern(emoticons, "SuperVinlin", R.drawable.twitch92a1b848540e934723x27);
        addPattern(emoticons, "DansGame", R.drawable.twitchce52b18fccf73b2925x32);
        addPattern(emoticons, "SwiftRage", R.drawable.twitch680b6b3887ef0d1721x28);
        addPattern(emoticons, "PJSalt", R.drawable.twitch18be1a297459453f36x30);
        addPattern(emoticons, "StrawBeary", R.drawable.twitch3dac9659e838fab220x27);
        addPattern(emoticons, "BlargNaut", R.drawable.twitcha5293e92212cadd921x27);
        addPattern(emoticons, "FreakinStinkin", R.drawable.twitchd14278fea8fad14619x27);
        addPattern(emoticons, "KevinTurtle", R.drawable.twitchd530ef454aa1709321x27);
        addPattern(emoticons, "Kreygasm", R.drawable.twitch3a624954918104fe19x27);
        addPattern(emoticons, "FPSMarksman", R.drawable.twitch6c26a3f04616c4bf20x27);
        addPattern(emoticons, "SoBayed", R.drawable.twitch58f4782b85d0069f17x27);
        addPattern(emoticons, "NoNoSpot", R.drawable.twitch179f310b0746584d23x27);
        addPattern(emoticons, "NinjaTroll", R.drawable.twitch89e474822a97692819x27);
        addPattern(emoticons, "SSSsss", R.drawable.twitch5d019b356bd3836024x24);
        addPattern(emoticons, "PunchTrees", R.drawable.twitchb85003ffba04e03e24x24);
        addPattern(emoticons, "TehFunrun", R.drawable.twitcha204e65775b969c527x27);
        addPattern(emoticons, "UleetBackup", R.drawable.twitch5342e829290d1af017x27);
        addPattern(emoticons, "ArsonNoSexy", R.drawable.twitche13a8382e40b19c718x27);
        addPattern(emoticons, "SMSkull", R.drawable.twitch50b9867ba05d1ecc24x24);
        addPattern(emoticons, "SMOrc", R.drawable.twitch9f276ed33053ec7032x32);
        addPattern(emoticons, "MVGame", R.drawable.twitch1a1a8bb5cdf6efb924x32);
        addPattern(emoticons, "FuzzyOtterOO", R.drawable.twitchd141fc57f627432f26x26);
        addPattern(emoticons, "GingerPower", R.drawable.twitch2febb829eae08b0a21x27);
        addPattern(emoticons, "BionicBunion", R.drawable.twitch740242272832a10830x30);
        addPattern(emoticons, "FrankerZ", R.drawable.twitch3b96527b46b1c94140x30);
        addPattern(emoticons, "OneHand", R.drawable.twitchb6d67569a0c6340a20x27);
        addPattern(emoticons, "TinyFace", R.drawable.twitchb93007bc230754e119x30);
        addPattern(emoticons, "HassanChop", R.drawable.twitch22c6299e539344a919x28);
        addPattern(emoticons, "BloodTrail", R.drawable.twitchf124d3a96eff228a41x28);
        addPattern(emoticons, "TheTarFu", R.drawable.twitch1fcfa48228bbd6ea25x28);
        addPattern(emoticons, "UnSane", R.drawable.twitch4eea6f01e372a99628x30);
        addPattern(emoticons, "EvilFetus", R.drawable.twitch484439fc20e0d36d29x30);
        addPattern(emoticons, "DBstyle", R.drawable.twitch1752876c0d0ec35f21x30);
        addPattern(emoticons, "AsianGlow", R.drawable.twitcha3708d1e15c3f19724x30);
        addPattern(emoticons, "BibleThump", R.drawable.twitchf6c13c7fc0a5c93d36x30);
        addPattern(emoticons, "ShazBotstix", R.drawable.twitchccaf06d02a01a80424x30);
        addPattern(emoticons, "PogChamp", R.drawable.twitch60aa1af305e32d4923x30);
        addPattern(emoticons, "Jebaited", R.drawable.twitch39dff1bb9b42cf3821x30);
        addPattern(emoticons, "OMGScoots", R.drawable.twitche01723a9ae4fbd8b22x28);
        addPattern(emoticons, "PMSTwin", R.drawable.twitcha33f6c484c27e24923x30);
        addPattern(emoticons, "ItsBoshyTime", R.drawable.twitche8e0b0c4e70c4fb818x18);
        addPattern(emoticons, "BORT", R.drawable.twitch6f9fa95e9e3d6a6919x30);
        addPattern(emoticons, "FUNgineer", R.drawable.twitch731296fdc2d37bea24x30);
        addPattern(emoticons, "ResidentSleeper", R.drawable.twitch1ddcc54d77fc4a6128x28);
        addPattern(emoticons, "4Head", R.drawable.twitch76292ac622b0fc3820x30);
        addPattern(emoticons, "SoonerLater", R.drawable.twitch696192d9891880af23x30);
        addPattern(emoticons, "OpieOP", R.drawable.twitch21e708123d6a896d21x30);
        addPattern(emoticons, "HotPokket", R.drawable.twitch55873089390f4a1028x30);
        addPattern(emoticons, "Poooound", R.drawable.twitch61a08075ecef6afa21x30);
        addPattern(emoticons, "TooSpicy", R.drawable.twitchf193772ca6e512f223x30);
        addPattern(emoticons, "FailFish", R.drawable.twitchc8a77ec0c49976d322x30);
        addPattern(emoticons, "RuleFive", R.drawable.twitch4e65703c52fb67b520x30);
		addPattern(emoticons, "BrainSlug", R.drawable.twitchd8eee0a259b7dfaa30x30);
   
   
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
		if(MainActivity.messageLinksShow){
		matcher = ActionProviderLink.URL.matcher(message);
		while (matcher.find()){
		
		linkMap.add(matcher.start());	
		message = message.replace(matcher.group(), "link");
		matcher = ActionProviderLink.URL.matcher(message);
		}}
		
		Spannable spannable = spannableFactory.newSpannable(nick + ": " + message);
		int length = nick.length() + 1;

		addSmiles(context, spannable, length, adressLength, privateM);

		return spannable;
	}
}
