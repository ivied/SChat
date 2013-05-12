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

public class DialogSc2tvSmiles extends DialogFragment {
	
	GridView  sc2tvSmile;
	
	 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle("Choise smile");
		View v = inflater.inflate(R.layout.dialog_choise_sc2tv_smile, null);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth(); 
		int x = (int) Math.round((double)width/ 80);
		GridView sc2tvSmile =(GridView) v.findViewById(R.id.gridView1);
		sc2tvSmile.setNumColumns(x);
		
		sc2tvSmile.setAdapter(new ImageAdapter(getActivity()));
	
		sc2tvSmile.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				onDismiss(getDialog());
				EditText message = (EditText) getActivity().findViewById(CursorLoaderListFragment.tagNumber);
				message.setText( message.getText().toString() + " " + emoticons.get(v.getId()));
				Log.i(MainActivity.LOG_TAG, "элемент = " + emoticons.get(v.getId()));	
			}
		});
				
		

		return v;
	}

	
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    
	    List<Integer> id = new ArrayList<Integer>(emoticons.keySet());
	    
	    public ImageAdapter(Context c) {
	        this.mContext = c;
	    }

	    public int getCount() {
	        return emoticons.size();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
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
	private static final Map<Integer,String> emoticons =new LinkedHashMap<Integer,String>();

	static {
		addDrawables(emoticons, ":s:happy:", R.drawable.sc2tvsmilea);
		addDrawables(emoticons, ":s:aws:", R.drawable.sc2tvsmileawesome);
		addDrawables(emoticons, ":s:nc:", R.drawable.sc2tvsmilenocomments);
		addDrawables(emoticons, ":s:manul:", R.drawable.sc2tvsmilemanul);
		addDrawables(emoticons, ":s:crazy:", R.drawable.sc2tvsmilecrazy);
		addDrawables(emoticons, ":s:cry:", R.drawable.sc2tvsmilecry);
		addDrawables(emoticons, ":s:glory:", R.drawable.sc2tvsmileglory);
		addDrawables(emoticons, ":s:kawai:", R.drawable.sc2tvsmilekawai);
		addDrawables(emoticons, ":s:mee:", R.drawable.sc2tvsmilemee);
		addDrawables(emoticons, ":s:omg:", R.drawable.sc2tvsmileomg);
		addDrawables(emoticons, ":s:whut:", R.drawable.sc2tvsmilemhu);
		addDrawables(emoticons, ":s:sad:", R.drawable.sc2tvsmilesad);
		addDrawables(emoticons, ":s:spk:", R.drawable.sc2tvsmileslowpoke);
		addDrawables(emoticons, ":s:hmhm:", R.drawable.sc2tvsmile2);
		addDrawables(emoticons, ":s:mad:", R.drawable.sc2tvsmilemad);
		addDrawables(emoticons, ":s:angry:", R.drawable.sc2tvsmileaangry);
		addDrawables(emoticons, ":s:xd:", R.drawable.sc2tvsmileii);
		addDrawables(emoticons, ":s:huh:", R.drawable.sc2tvsmilehuh);
		addDrawables(emoticons, ":s:tears:", R.drawable.sc2tvsmilehappycry);
		addDrawables(emoticons, ":s:notch:", R.drawable.sc2tvsmilenotch);
		addDrawables(emoticons, ":s:vaga:", R.drawable.sc2tvsmilevaganych);
		addDrawables(emoticons, ":s:ra:", R.drawable.sc2tvsmilera);
		addDrawables(emoticons, ":s:fp:", R.drawable.sc2tvsmilefacepalm);
		addDrawables(emoticons, ":s:neo:", R.drawable.sc2tvsmilesmith);
		addDrawables(emoticons, ":s:peka:", R.drawable.sc2tvsmileminihappy);
		addDrawables(emoticons, ":s:trf:", R.drawable.sc2tvsmiletrollface);
		addDrawables(emoticons, ":s:fu:", R.drawable.sc2tvsmilefuuuu);
		addDrawables(emoticons, ":s:why:", R.drawable.sc2tvsmilewhy);
		addDrawables(emoticons, ":s:yao:", R.drawable.sc2tvsmileyao);
		addDrawables(emoticons, ":s:fyeah:", R.drawable.sc2tvsmilefyeah);
		addDrawables(emoticons, ":s:lucky:", R.drawable.sc2tvsmilelol);
		addDrawables(emoticons, ":s:okay:", R.drawable.sc2tvsmileokay);
		addDrawables(emoticons, ":s:alone:", R.drawable.sc2tvsmilealone);
		addDrawables(emoticons, ":s:joyful:", R.drawable.sc2tvsmileewbte);
		addDrawables(emoticons, ":s:wtf:", R.drawable.sc2tvsmilewtf);
		addDrawables(emoticons, ":s:danu:", R.drawable.sc2tvsmiledaladno);
		addDrawables(emoticons, ":s:gusta:", R.drawable.sc2tvsmilemegusta);
		addDrawables(emoticons, ":s:bm:", R.drawable.sc2tvsmilebm);
		// page 2
		addDrawables(emoticons, ":s:lol:", R.drawable.sc2tvsmileloool);
		addDrawables(emoticons, ":s:notbad:", R.drawable.sc2tvsmilenotbad);
		addDrawables(emoticons, ":s:rly:", R.drawable.sc2tvsmilereally);
		addDrawables(emoticons, ":s:ban:", R.drawable.sc2tvsmilebanan);
		addDrawables(emoticons, ":s:cap:", R.drawable.sc2tvsmilecap);
		addDrawables(emoticons, ":s:br:", R.drawable.sc2tvsmilebr);
		addDrawables(emoticons, ":s:fpl:", R.drawable.sc2tvsmileleefacepalm);
		addDrawables(emoticons, ":s:ht:", R.drawable.sc2tvsmileheart);
		// face
		addDrawables(emoticons, ":s:adolf:", R.drawable.sc2tvsmileadolf);
		addDrawables(emoticons, ":s:bratok:", R.drawable.sc2tvsmilebratok);
		addDrawables(emoticons, ":s:strelok:", R.drawable.sc2tvsmilestrelok);
		// addDrawables(emoticons, ":s:white-ra:", R.drawable.sc2tvsmilewhitera);
		addDrawables(emoticons, ":s:dimaga:", R.drawable.sc2tvsmiledimaga);
		addDrawables(emoticons, ":s:bruce:", R.drawable.sc2tvsmilebruce);
		addDrawables(emoticons, ":s:jae:", R.drawable.sc2tvsmilejaedong);
		addDrawables(emoticons, ":s:flash:", R.drawable.sc2tvsmileflash1);
		addDrawables(emoticons, ":s:bisu:", R.drawable.sc2tvsmilebisu);
		addDrawables(emoticons, ":s:jangbi:", R.drawable.sc2tvsmilejangbi);
		addDrawables(emoticons, ":s:idra:", R.drawable.sc2tvsmileidra);
		addDrawables(emoticons, ":s:vdv:", R.drawable.sc2tvsmilevitya);
		addDrawables(emoticons, ":s:imba:", R.drawable.sc2tvsmiledjigurda);
		addDrawables(emoticons, ":s:chuck:", R.drawable.sc2tvsmilechan);
		addDrawables(emoticons, ":s:tgirl:", R.drawable.sc2tvsmilebrucelove);
		addDrawables(emoticons, ":s:top1sng:", R.drawable.sc2tvsmilehappy);
		addDrawables(emoticons, ":s:slavik:", R.drawable.sc2tvsmileslavik);
		addDrawables(emoticons, ":s:olsilove:", R.drawable.sc2tvsmileolsilove);
		addDrawables(emoticons, ":s:kas:", R.drawable.sc2tvsmilekas);
		// other
		addDrawables(emoticons, ":s:pool:", R.drawable.sc2tvsmilepool);
		addDrawables(emoticons, ":s:ej:", R.drawable.sc2tvsmileejik);
		addDrawables(emoticons, ":s:mario:", R.drawable.sc2tvsmilemario);
		addDrawables(emoticons, ":s:tort:", R.drawable.sc2tvsmiletort);
		addDrawables(emoticons, ":s:arni:", R.drawable.sc2tvsmileterminator);
		addDrawables(emoticons, ":s:crab:", R.drawable.sc2tvsmilecrab);
		addDrawables(emoticons, ":s:hero:", R.drawable.sc2tvsmileheroes3);
		addDrawables(emoticons, ":s:mc:", R.drawable.sc2tvsmilemine);
		addDrawables(emoticons, ":s:osu:", R.drawable.sc2tvsmileosu);
		addDrawables(emoticons, ":s:q3:", R.drawable.sc2tvsmileq3);
		// page 3
		addDrawables(emoticons, ":s:tigra:", R.drawable.sc2tvsmiletigrica);
		addDrawables(emoticons, ":s:volck:", R.drawable.sc2tvsmilevoolchik1);
		addDrawables(emoticons, ":s:hpeka:", R.drawable.sc2tvsmileharupeka);
		addDrawables(emoticons, ":s:slow:", R.drawable.sc2tvsmilespok);
		addDrawables(emoticons, ":s:alex:", R.drawable.sc2tvsmilealfi);
		addDrawables(emoticons, ":s:panda:", R.drawable.sc2tvsmilepanda);
		addDrawables(emoticons, ":s:sun:", R.drawable.sc2tvsmilesunl);
		addDrawables(emoticons, ":s:cou:", R.drawable.sc2tvsmilecougar);
		addDrawables(emoticons, ":s:wb:", R.drawable.sc2tvsmilewormban);
		addDrawables(emoticons, ":s:dobro:", R.drawable.sc2tvsmiledobre);
		addDrawables(emoticons, ":s:theweedle:", R.drawable.sc2tvsmileweedle);
		addDrawables(emoticons, ":s:apc:", R.drawable.sc2tvsmileapochai);
		addDrawables(emoticons, ":s:globus:", R.drawable.sc2tvsmileglobus);
		addDrawables(emoticons, ":s:cow:", R.drawable.sc2tvsmilecow);
		addDrawables(emoticons, ":s:nook:", R.drawable.sc2tvsmilenookay);
		addDrawables(emoticons, ":s:noj:", R.drawable.sc2tvsmileknife);
		addDrawables(emoticons, ":s:fpd:", R.drawable.sc2tvsmilefp);
		addDrawables(emoticons, ":s:hg:", R.drawable.sc2tvsmilehg);
		// anime
		addDrawables(emoticons, ":s:yoko:", R.drawable.sc2tvsmileyoko);
		addDrawables(emoticons, ":s:miku:", R.drawable.sc2tvsmilemiku);
		addDrawables(emoticons, ":s:winry:", R.drawable.sc2tvsmilewinry);
		addDrawables(emoticons, ":s:asuka:", R.drawable.sc2tvsmileasuka);
		addDrawables(emoticons, ":s:konata:", R.drawable.sc2tvsmilekonata);
		addDrawables(emoticons, ":s:reimu:", R.drawable.sc2tvsmilereimu);
		addDrawables(emoticons, ":s:sex:", R.drawable.sc2tvsmilesex);
		addDrawables(emoticons, ":s:mimo:", R.drawable.sc2tvsmilemimo);
		addDrawables(emoticons, ":s:fire:", R.drawable.sc2tvsmilefire);
		addDrawables(emoticons, ":s:mandarin:", R.drawable.sc2tvsmilemandarin);
		addDrawables(emoticons, ":s:grafon:", R.drawable.sc2tvsmilegrafon);
		addDrawables(emoticons, ":s:epeka:", R.drawable.sc2tvsmileepeka);
		addDrawables(emoticons, ":s:opeka:", R.drawable.sc2tvsmileopeka);
		addDrawables(emoticons, ":s:ocry:", R.drawable.sc2tvsmileocry);
		addDrawables(emoticons, ":s:neponi:", R.drawable.sc2tvsmileneponi);
		addDrawables(emoticons, ":s:moon:", R.drawable.sc2tvsmilemoon);
		addDrawables(emoticons, ":s:ghost:", R.drawable.sc2tvsmilegay);
		addDrawables(emoticons, ":s:omsk:", R.drawable.sc2tvsmileomsk);
		addDrawables(emoticons, ":s:grumpy:", R.drawable.sc2tvsmilegrumpy);
	}
	
}

