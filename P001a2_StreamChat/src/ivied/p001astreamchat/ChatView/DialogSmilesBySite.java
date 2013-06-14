package ivied.p001astreamchat.ChatView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
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

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Sites.Site;

public class DialogSmilesBySite extends DialogFragment {
	FactorySite.SiteName site;
	Map<String,Bitmap> smileMap;
    Map <Integer, Bitmap > idMap;
	Site siteClass;
	public static DialogSmilesBySite newInstance(FactorySite.SiteName siteName) {
		DialogSmilesBySite frag = new DialogSmilesBySite();
		Bundle args = new Bundle();
		args.putSerializable("title", siteName);
		frag.setArguments(args);
		return frag;
	}
	
	 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		site = (FactorySite.SiteName) getArguments().getSerializable("title");
        FactorySite factorySite = new FactorySite();
		siteClass = factorySite.getSite(site);
		getDialog().setTitle("Choice smile");
		View v = inflater.inflate(R.layout.dialog_choise_sc2tv_smile, null);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth(); 
		int x = (int) Math.round((double)width/ 80);
		GridView gridSmile =(GridView) v.findViewById(R.id.gridChannelList);
		gridSmile.setNumColumns(x);
		
		gridSmile.setAdapter(new ImageAdapter(getActivity()));
	
		gridSmile.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                onDismiss(getDialog());

                String emo = "null";
                EditText message = (EditText) getActivity().findViewById(MainActivity.focus + 1);
                for (Map.Entry<String, Bitmap> smile : smileMap.entrySet()) {
                    if (smile.getValue().equals(idMap.get(position))) emo = smile.getKey();
                }
                if( siteClass.getSiteName().equalsIgnoreCase("sc2tv")) emo = ":s"+emo;
                message.setText(Html.fromHtml(message.getText().toString() + " " + emo));


            }
        });
				
		

		return v;
	}

	
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

         List<Bitmap>  id = new ArrayList<Bitmap>();


	    public ImageAdapter(Context c) {
	        this.mContext = c;
	        smileMap =  siteClass.getSmileMap();
	        id = new ArrayList<Bitmap>(smileMap.values());
            idMap = new HashMap<Integer, Bitmap >();
            int i= 0;
            for (Bitmap smile : id){
                idMap.put(i,smile);
                        i++;
            }

	    }

	    public int getCount() {
	        return smileMap.size();
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
	            imageView.setId(id.indexOf(idMap.get(position)));
	            imageView.setPadding(8, 8, 8, 8);
	            
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageBitmap(idMap.get(position));
	        return imageView;
	    }


	}


       /* addDrawables(emoticonsTwitch, ":B)", R.drawable.twitch2cde79cfe74c616924x18);
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
        addDrawables(emoticonsTwitch, ">(", R.drawable.twitchd31223e81104544a24x18);*/

}

