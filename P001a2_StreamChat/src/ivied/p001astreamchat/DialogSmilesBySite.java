package ivied.p001astreamchat;

import ivied.p001astreamchat.ChatList.CursorLoaderListFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

public class DialogSmilesBySite extends DialogFragment {
	String site;
	GridView  sc2tvSmile;
	Map<Integer,String> idMap;
	
	public static DialogSmilesBySite newInstance(String title) {
		DialogSmilesBySite frag = new DialogSmilesBySite();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	
	 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		site = getArguments().getString("title");
		
		getDialog().setTitle("Choice smile");
		View v = inflater.inflate(R.layout.dialog_choise_sc2tv_smile, null);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth(); 
		int x = (int) Math.round((double)width/ 80);
		GridView sc2tvSmile =(GridView) v.findViewById(R.id.gridChannelList);
		sc2tvSmile.setNumColumns(x);
		
		sc2tvSmile.setAdapter(new ImageAdapter(getActivity()));
	
		sc2tvSmile.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				onDismiss(getDialog());
				Log.i(MainActivity.LOG_TAG, "focus = " + MainActivity.focus);
				EditText message = (EditText) getActivity().findViewById(MainActivity.focus+1);
				 List<Integer> idArray = new ArrayList<Integer>();
				 idArray = new ArrayList<Integer>(idMap.keySet());
					 message.setText( message.getText().toString() + " " + idMap.get(idArray.get(position)));
					 
					
			}
		});
				
		

		return v;
	}

	
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    
	    List<Integer> id = new ArrayList<Integer>();
	    
	    public ImageAdapter(Context c) {
	        this.mContext = c;
	        if (site.equalsIgnoreCase("sc2tv")){
	        	idMap =emoticonsSc2tv;
	        } else{
	        	idMap =emoticonsTwitch;
	        }
	        id = new ArrayList<Integer>(idMap.keySet());
	    }

	    public int getCount() {
	        return idMap.size();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        
	       
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setId(id.get(position));
	            imageView.setPadding(8, 8, 8, 8);
	            
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(id.get(position));
	        return imageView;
	    }

	/*    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7,
	            R.drawable.sample_0, R.drawable.sample_1,
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7,
	            R.drawable.sample_0, R.drawable.sample_1,
	            R.drawable.sample_2, R.drawable.sample_3,
	            R.drawable.sample_4, R.drawable.sample_5,
	            R.drawable.sample_6, R.drawable.sample_7
	    };*/
	}
	private static void addDrawables(Map<Integer,String > emoticons, String regex, int id) {

		
        emoticons.put(id,regex );
        
        

    }
	private static final Map<Integer,String> emoticonsSc2tv =new LinkedHashMap<Integer,String>();

	static {
		
		addDrawables(emoticonsSc2tv, ":s:happy:", R.drawable.sc2tvsmilea);
		addDrawables(emoticonsSc2tv, ":s:aws:", R.drawable.sc2tvsmileawesome);
		addDrawables(emoticonsSc2tv, ":s:nc:", R.drawable.sc2tvsmilenocomments);
		addDrawables(emoticonsSc2tv, ":s:manul:", R.drawable.sc2tvsmilemanul);
		addDrawables(emoticonsSc2tv, ":s:crazy:", R.drawable.sc2tvsmilecrazy);
		addDrawables(emoticonsSc2tv, ":s:cry:", R.drawable.sc2tvsmilecry);
		addDrawables(emoticonsSc2tv, ":s:glory:", R.drawable.sc2tvsmileglory);
		addDrawables(emoticonsSc2tv, ":s:kawai:", R.drawable.sc2tvsmilekawai);
		addDrawables(emoticonsSc2tv, ":s:mee:", R.drawable.sc2tvsmilemee);
		addDrawables(emoticonsSc2tv, ":s:omg:", R.drawable.sc2tvsmileomg);
		addDrawables(emoticonsSc2tv, ":s:whut:", R.drawable.sc2tvsmilemhu);
		addDrawables(emoticonsSc2tv, ":s:sad:", R.drawable.sc2tvsmilesad);
		addDrawables(emoticonsSc2tv, ":s:spk:", R.drawable.sc2tvsmileslowpoke);
		addDrawables(emoticonsSc2tv, ":s:hmhm:", R.drawable.sc2tvsmile2);
		addDrawables(emoticonsSc2tv, ":s:mad:", R.drawable.sc2tvsmilemad);
		addDrawables(emoticonsSc2tv, ":s:angry:", R.drawable.sc2tvsmileaangry);
		addDrawables(emoticonsSc2tv, ":s:xd:", R.drawable.sc2tvsmileii);
		addDrawables(emoticonsSc2tv, ":s:huh:", R.drawable.sc2tvsmilehuh);
		addDrawables(emoticonsSc2tv, ":s:tears:", R.drawable.sc2tvsmilehappycry);
		addDrawables(emoticonsSc2tv, ":s:notch:", R.drawable.sc2tvsmilenotch);
		addDrawables(emoticonsSc2tv, ":s:vaga:", R.drawable.sc2tvsmilevaganych);
		addDrawables(emoticonsSc2tv, ":s:ra:", R.drawable.sc2tvsmilera);
		addDrawables(emoticonsSc2tv, ":s:fp:", R.drawable.sc2tvsmilefacepalm);
		addDrawables(emoticonsSc2tv, ":s:neo:", R.drawable.sc2tvsmilesmith);
		addDrawables(emoticonsSc2tv, ":s:peka:", R.drawable.sc2tvsmileminihappy);
		addDrawables(emoticonsSc2tv, ":s:trf:", R.drawable.sc2tvsmiletrollface);
		addDrawables(emoticonsSc2tv, ":s:fu:", R.drawable.sc2tvsmilefuuuu);
		addDrawables(emoticonsSc2tv, ":s:why:", R.drawable.sc2tvsmilewhy);
		addDrawables(emoticonsSc2tv, ":s:yao:", R.drawable.sc2tvsmileyao);
		addDrawables(emoticonsSc2tv, ":s:fyeah:", R.drawable.sc2tvsmilefyeah);
		addDrawables(emoticonsSc2tv, ":s:lucky:", R.drawable.sc2tvsmilelol);
		addDrawables(emoticonsSc2tv, ":s:okay:", R.drawable.sc2tvsmileokay);
		addDrawables(emoticonsSc2tv, ":s:alone:", R.drawable.sc2tvsmilealone);
		addDrawables(emoticonsSc2tv, ":s:joyful:", R.drawable.sc2tvsmileewbte);
		addDrawables(emoticonsSc2tv, ":s:wtf:", R.drawable.sc2tvsmilewtf);
		addDrawables(emoticonsSc2tv, ":s:danu:", R.drawable.sc2tvsmiledaladno);
		addDrawables(emoticonsSc2tv, ":s:gusta:", R.drawable.sc2tvsmilemegusta);
		addDrawables(emoticonsSc2tv, ":s:bm:", R.drawable.sc2tvsmilebm);
		// page 2
		addDrawables(emoticonsSc2tv, ":s:lol:", R.drawable.sc2tvsmileloool);
		addDrawables(emoticonsSc2tv, ":s:notbad:", R.drawable.sc2tvsmilenotbad);
		addDrawables(emoticonsSc2tv, ":s:rly:", R.drawable.sc2tvsmilereally);
		addDrawables(emoticonsSc2tv, ":s:ban:", R.drawable.sc2tvsmilebanan);
		addDrawables(emoticonsSc2tv, ":s:cap:", R.drawable.sc2tvsmilecap);
		addDrawables(emoticonsSc2tv, ":s:br:", R.drawable.sc2tvsmilebr);
		addDrawables(emoticonsSc2tv, ":s:fpl:", R.drawable.sc2tvsmileleefacepalm);
		addDrawables(emoticonsSc2tv, ":s:ht:", R.drawable.sc2tvsmileheart);
		// face
		addDrawables(emoticonsSc2tv, ":s:adolf:", R.drawable.sc2tvsmileadolf);
		addDrawables(emoticonsSc2tv, ":s:bratok:", R.drawable.sc2tvsmilebratok);
		addDrawables(emoticonsSc2tv, ":s:strelok:", R.drawable.sc2tvsmilestrelok);
		// addDrawables(emoticons, ":s:white-ra:", R.drawable.sc2tvsmilewhitera);
		addDrawables(emoticonsSc2tv, ":s:dimaga:", R.drawable.sc2tvsmiledimaga);
		addDrawables(emoticonsSc2tv, ":s:bruce:", R.drawable.sc2tvsmilebruce);
		addDrawables(emoticonsSc2tv, ":s:jae:", R.drawable.sc2tvsmilejaedong);
		addDrawables(emoticonsSc2tv, ":s:flash:", R.drawable.sc2tvsmileflash1);
		addDrawables(emoticonsSc2tv, ":s:bisu:", R.drawable.sc2tvsmilebisu);
		addDrawables(emoticonsSc2tv, ":s:jangbi:", R.drawable.sc2tvsmilejangbi);
		addDrawables(emoticonsSc2tv, ":s:idra:", R.drawable.sc2tvsmileidra);
		addDrawables(emoticonsSc2tv, ":s:vdv:", R.drawable.sc2tvsmilevitya);
		addDrawables(emoticonsSc2tv, ":s:imba:", R.drawable.sc2tvsmiledjigurda);
		addDrawables(emoticonsSc2tv, ":s:chuck:", R.drawable.sc2tvsmilechan);
		addDrawables(emoticonsSc2tv, ":s:tgirl:", R.drawable.sc2tvsmilebrucelove);
		addDrawables(emoticonsSc2tv, ":s:top1sng:", R.drawable.sc2tvsmilehappy);
		addDrawables(emoticonsSc2tv, ":s:slavik:", R.drawable.sc2tvsmileslavik);
		addDrawables(emoticonsSc2tv, ":s:olsilove:", R.drawable.sc2tvsmileolsilove);
		addDrawables(emoticonsSc2tv, ":s:kas:", R.drawable.sc2tvsmilekas);
		// other
		addDrawables(emoticonsSc2tv, ":s:pool:", R.drawable.sc2tvsmilepool);
		addDrawables(emoticonsSc2tv, ":s:ej:", R.drawable.sc2tvsmileejik);
		addDrawables(emoticonsSc2tv, ":s:mario:", R.drawable.sc2tvsmilemario);
		addDrawables(emoticonsSc2tv, ":s:tort:", R.drawable.sc2tvsmiletort);
		addDrawables(emoticonsSc2tv, ":s:arni:", R.drawable.sc2tvsmileterminator);
		addDrawables(emoticonsSc2tv, ":s:crab:", R.drawable.sc2tvsmilecrab);
		addDrawables(emoticonsSc2tv, ":s:hero:", R.drawable.sc2tvsmileheroes3);
		addDrawables(emoticonsSc2tv, ":s:mc:", R.drawable.sc2tvsmilemine);
		addDrawables(emoticonsSc2tv, ":s:osu:", R.drawable.sc2tvsmileosu);
		addDrawables(emoticonsSc2tv, ":s:q3:", R.drawable.sc2tvsmileq3);
		// page 3
		addDrawables(emoticonsSc2tv, ":s:tigra:", R.drawable.sc2tvsmiletigrica);
		addDrawables(emoticonsSc2tv, ":s:volck:", R.drawable.sc2tvsmilevoolchik1);
		addDrawables(emoticonsSc2tv, ":s:hpeka:", R.drawable.sc2tvsmileharupeka);
		addDrawables(emoticonsSc2tv, ":s:slow:", R.drawable.sc2tvsmilespok);
		addDrawables(emoticonsSc2tv, ":s:alex:", R.drawable.sc2tvsmilealfi);
		addDrawables(emoticonsSc2tv, ":s:panda:", R.drawable.sc2tvsmilepanda);
		addDrawables(emoticonsSc2tv, ":s:sun:", R.drawable.sc2tvsmilesunl);
		addDrawables(emoticonsSc2tv, ":s:cou:", R.drawable.sc2tvsmilecougar);
		addDrawables(emoticonsSc2tv, ":s:wb:", R.drawable.sc2tvsmilewormban);
		addDrawables(emoticonsSc2tv, ":s:dobro:", R.drawable.sc2tvsmiledobre);
		addDrawables(emoticonsSc2tv, ":s:theweedle:", R.drawable.sc2tvsmileweedle);
		addDrawables(emoticonsSc2tv, ":s:apc:", R.drawable.sc2tvsmileapochai);
		addDrawables(emoticonsSc2tv, ":s:globus:", R.drawable.sc2tvsmileglobus);
		addDrawables(emoticonsSc2tv, ":s:cow:", R.drawable.sc2tvsmilecow);
		addDrawables(emoticonsSc2tv, ":s:nook:", R.drawable.sc2tvsmilenookay);
		addDrawables(emoticonsSc2tv, ":s:noj:", R.drawable.sc2tvsmileknife);
		addDrawables(emoticonsSc2tv, ":s:fpd:", R.drawable.sc2tvsmilefp);
		addDrawables(emoticonsSc2tv, ":s:hg:", R.drawable.sc2tvsmilehg);
		// anime
		addDrawables(emoticonsSc2tv, ":s:yoko:", R.drawable.sc2tvsmileyoko);
		addDrawables(emoticonsSc2tv, ":s:miku:", R.drawable.sc2tvsmilemiku);
		addDrawables(emoticonsSc2tv, ":s:winry:", R.drawable.sc2tvsmilewinry);
		addDrawables(emoticonsSc2tv, ":s:asuka:", R.drawable.sc2tvsmileasuka);
		addDrawables(emoticonsSc2tv, ":s:konata:", R.drawable.sc2tvsmilekonata);
		addDrawables(emoticonsSc2tv, ":s:reimu:", R.drawable.sc2tvsmilereimu);
		addDrawables(emoticonsSc2tv, ":s:sex:", R.drawable.sc2tvsmilesex);
		addDrawables(emoticonsSc2tv, ":s:mimo:", R.drawable.sc2tvsmilemimo);
		addDrawables(emoticonsSc2tv, ":s:fire:", R.drawable.sc2tvsmilefire);
		addDrawables(emoticonsSc2tv, ":s:mandarin:", R.drawable.sc2tvsmilemandarin);
		addDrawables(emoticonsSc2tv, ":s:grafon:", R.drawable.sc2tvsmilegrafon);
		addDrawables(emoticonsSc2tv, ":s:epeka:", R.drawable.sc2tvsmileepeka);
		addDrawables(emoticonsSc2tv, ":s:opeka:", R.drawable.sc2tvsmileopeka);
		addDrawables(emoticonsSc2tv, ":s:ocry:", R.drawable.sc2tvsmileocry);
		addDrawables(emoticonsSc2tv, ":s:neponi:", R.drawable.sc2tvsmileneponi);
		addDrawables(emoticonsSc2tv, ":s:moon:", R.drawable.sc2tvsmilemoon);
		addDrawables(emoticonsSc2tv, ":s:ghost:", R.drawable.sc2tvsmilegay);
		addDrawables(emoticonsSc2tv, ":s:omsk:", R.drawable.sc2tvsmileomsk);
		addDrawables(emoticonsSc2tv, ":s:grumpy:", R.drawable.sc2tvsmilegrumpy);
	}
	private static final Map<Integer,String> emoticonsTwitch =new LinkedHashMap<Integer,String>();

	static {
		
        addDrawables(emoticonsTwitch, "Volcania", R.drawable.twitchefbcc231b2d2d20627x28);
        addDrawables(emoticonsTwitch, "DatSheffy", R.drawable.twitchbf13a0595ecf649c24x30);
        addDrawables(emoticonsTwitch, "JKanStyle", R.drawable.twitch3a7ee1bc0e5c9af021x27);
        addDrawables(emoticonsTwitch, "OptimizePrime", R.drawable.twitch41f8a86c4b15b5d822x27);
        addDrawables(emoticonsTwitch, "StoneLightning", R.drawable.twitch8b5aaae6e2409deb20x27);
        addDrawables(emoticonsTwitch, "TheRinger", R.drawable.twitch1903cc415afc404c20x27);
        addDrawables(emoticonsTwitch, "PazPazowitz", R.drawable.twitch521420789e1e93ef18x27);
        addDrawables(emoticonsTwitch, ":B)", R.drawable.twitch2cde79cfe74c616924x18);
        addDrawables(emoticonsTwitch, ":z", R.drawable.twitchb9cbb6884788aa6224x18);
        addDrawables(emoticonsTwitch, ":)", R.drawable.twitchebf60cd72f7aa60024x18);
        addDrawables(emoticonsTwitch, ":(", R.drawable.twitchd570c4b3b8d8fc4d24x18);
        addDrawables(emoticonsTwitch, ":P", R.drawable.twitche838e5e34d9f240c24x18);
        addDrawables(emoticonsTwitch, ";p", R.drawable.twitch3407bf911ad2fd4a24x18);
        addDrawables(emoticonsTwitch, "<3", R.drawable.twitch577ade91d46d7edc24x18);
        addDrawables(emoticonsTwitch, ":\\", R.drawable.twitch374120835234cb2924x18);
        addDrawables(emoticonsTwitch, ";)", R.drawable.twitchcfaf6eac72fe4de624x18);
        addDrawables(emoticonsTwitch, "R)", R.drawable.twitch0536d670860bf73324x18);
        addDrawables(emoticonsTwitch, "o_O", R.drawable.twitch8e128fa8dc1de29c24x18);
        addDrawables(emoticonsTwitch, ":D", R.drawable.twitch9f2ac5d4b53913d724x18);
        addDrawables(emoticonsTwitch, ":o", R.drawable.twitchae4e17f5b9624e2f24x18);
        addDrawables(emoticonsTwitch, ">(", R.drawable.twitchd31223e81104544a24x18);
        addDrawables(emoticonsTwitch, "WinWaker", R.drawable.twitchd4e971f7a6830e9530x30);
        addDrawables(emoticonsTwitch, "TriHard", R.drawable.twitch6407e6947eb69e2124x30);
        addDrawables(emoticonsTwitch, "EagleEye", R.drawable.twitch95eb8045e7ae63b818x27);
        addDrawables(emoticonsTwitch, "CougarHunt", R.drawable.twitch551cd64fc3d4590a21x27);
        addDrawables(emoticonsTwitch, "RedCoat", R.drawable.twitch6b8d1be08f244e9219x27);
        addDrawables(emoticonsTwitch, "BrokeBack", R.drawable.twitch35ae4e0e8dd045e122x27);
        addDrawables(emoticonsTwitch, "Kappa", R.drawable.twitchddc6e3a8732cb50f25x28);
        addDrawables(emoticonsTwitch, "JonCarnage", R.drawable.twitch6aaca644ea5374c620x27);
        addDrawables(emoticonsTwitch, "PicoMause", R.drawable.twitchce027387c35fb60122x27);
        addDrawables(emoticonsTwitch, "MrDestructoid", R.drawable.twitchac61a7aeb52a49d339x27);
        addDrawables(emoticonsTwitch, "BCWarrior", R.drawable.twitch1e3ccd969459f88929x27);
        addDrawables(emoticonsTwitch, "SuperVinlin", R.drawable.twitch92a1b848540e934723x27);
        addDrawables(emoticonsTwitch, "DansGame", R.drawable.twitchce52b18fccf73b2925x32);
        addDrawables(emoticonsTwitch, "SwiftRage", R.drawable.twitch680b6b3887ef0d1721x28);
        addDrawables(emoticonsTwitch, "PJSalt", R.drawable.twitch18be1a297459453f36x30);
        addDrawables(emoticonsTwitch, "StrawBeary", R.drawable.twitch3dac9659e838fab220x27);
        addDrawables(emoticonsTwitch, "BlargNaut", R.drawable.twitcha5293e92212cadd921x27);
        addDrawables(emoticonsTwitch, "FreakinStinkin", R.drawable.twitchd14278fea8fad14619x27);
        addDrawables(emoticonsTwitch, "KevinTurtle", R.drawable.twitchd530ef454aa1709321x27);
        addDrawables(emoticonsTwitch, "Kreygasm", R.drawable.twitch3a624954918104fe19x27);
        addDrawables(emoticonsTwitch, "FPSMarksman", R.drawable.twitch6c26a3f04616c4bf20x27);
        addDrawables(emoticonsTwitch, "SoBayed", R.drawable.twitch58f4782b85d0069f17x27);
        addDrawables(emoticonsTwitch, "NoNoSpot", R.drawable.twitch179f310b0746584d23x27);
        addDrawables(emoticonsTwitch, "NinjaTroll", R.drawable.twitch89e474822a97692819x27);
        addDrawables(emoticonsTwitch, "SSSsss", R.drawable.twitch5d019b356bd3836024x24);
        addDrawables(emoticonsTwitch, "PunchTrees", R.drawable.twitchb85003ffba04e03e24x24);
        addDrawables(emoticonsTwitch, "TehFunrun", R.drawable.twitcha204e65775b969c527x27);
        addDrawables(emoticonsTwitch, "UleetBackup", R.drawable.twitch5342e829290d1af017x27);
        addDrawables(emoticonsTwitch, "ArsonNoSexy", R.drawable.twitche13a8382e40b19c718x27);
        addDrawables(emoticonsTwitch, "SMSkull", R.drawable.twitch50b9867ba05d1ecc24x24);
        addDrawables(emoticonsTwitch, "SMOrc", R.drawable.twitch9f276ed33053ec7032x32);
        addDrawables(emoticonsTwitch, "MVGame", R.drawable.twitch1a1a8bb5cdf6efb924x32);
        addDrawables(emoticonsTwitch, "FuzzyOtterOO", R.drawable.twitchd141fc57f627432f26x26);
        addDrawables(emoticonsTwitch, "GingerPower", R.drawable.twitch2febb829eae08b0a21x27);
        addDrawables(emoticonsTwitch, "BionicBunion", R.drawable.twitch740242272832a10830x30);
        addDrawables(emoticonsTwitch, "FrankerZ", R.drawable.twitch3b96527b46b1c94140x30);
        addDrawables(emoticonsTwitch, "OneHand", R.drawable.twitchb6d67569a0c6340a20x27);
        addDrawables(emoticonsTwitch, "TinyFace", R.drawable.twitchb93007bc230754e119x30);
        addDrawables(emoticonsTwitch, "HassanChop", R.drawable.twitch22c6299e539344a919x28);
        addDrawables(emoticonsTwitch, "BloodTrail", R.drawable.twitchf124d3a96eff228a41x28);
        addDrawables(emoticonsTwitch, "TheTarFu", R.drawable.twitch1fcfa48228bbd6ea25x28);
        addDrawables(emoticonsTwitch, "UnSane", R.drawable.twitch4eea6f01e372a99628x30);
        addDrawables(emoticonsTwitch, "EvilFetus", R.drawable.twitch484439fc20e0d36d29x30);
        addDrawables(emoticonsTwitch, "DBstyle", R.drawable.twitch1752876c0d0ec35f21x30);
        addDrawables(emoticonsTwitch, "AsianGlow", R.drawable.twitcha3708d1e15c3f19724x30);
        addDrawables(emoticonsTwitch, "BibleThump", R.drawable.twitchf6c13c7fc0a5c93d36x30);
        addDrawables(emoticonsTwitch, "ShazBotstix", R.drawable.twitchccaf06d02a01a80424x30);
        addDrawables(emoticonsTwitch, "PogChamp", R.drawable.twitch60aa1af305e32d4923x30);
        addDrawables(emoticonsTwitch, "Jebaited", R.drawable.twitch39dff1bb9b42cf3821x30);
        addDrawables(emoticonsTwitch, "OMGScoots", R.drawable.twitche01723a9ae4fbd8b22x28);
        addDrawables(emoticonsTwitch, "PMSTwin", R.drawable.twitcha33f6c484c27e24923x30);
        addDrawables(emoticonsTwitch, "ItsBoshyTime", R.drawable.twitche8e0b0c4e70c4fb818x18);
        addDrawables(emoticonsTwitch, "BORT", R.drawable.twitch6f9fa95e9e3d6a6919x30);
        addDrawables(emoticonsTwitch, "FUNgineer", R.drawable.twitch731296fdc2d37bea24x30);
        addDrawables(emoticonsTwitch, "ResidentSleeper", R.drawable.twitch1ddcc54d77fc4a6128x28);
        addDrawables(emoticonsTwitch, "4Head", R.drawable.twitch76292ac622b0fc3820x30);
        addDrawables(emoticonsTwitch, "SoonerLater", R.drawable.twitch696192d9891880af23x30);
        addDrawables(emoticonsTwitch, "OpieOP", R.drawable.twitch21e708123d6a896d21x30);
        addDrawables(emoticonsTwitch, "HotPokket", R.drawable.twitch55873089390f4a1028x30);
        addDrawables(emoticonsTwitch, "Poooound", R.drawable.twitch61a08075ecef6afa21x30);
        addDrawables(emoticonsTwitch, "TooSpicy", R.drawable.twitchf193772ca6e512f223x30);
        addDrawables(emoticonsTwitch, "FailFish", R.drawable.twitchc8a77ec0c49976d322x30);
        addDrawables(emoticonsTwitch, "RuleFive", R.drawable.twitch4e65703c52fb67b520x30);
		addDrawables(emoticonsTwitch, "BrainSlug", R.drawable.twitchd8eee0a259b7dfaa30x30);
	}
}

