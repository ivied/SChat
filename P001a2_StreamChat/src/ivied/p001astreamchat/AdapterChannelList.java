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

	  // пункт списка
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    // используем созданные, но не используемые view
	    View view = convertView;
	    
	    if ((position & 1)  == 1){
	    	view = new ImageView(ctx);
	    	((ImageView) view).setImageResource(R.drawable.ic_launcher);
	    	
	    	//view.setLayoutParams((new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT)));
	    }else{	
	    	
	    	
	    if (view == null) {
	    
	      view = lInflater.inflate(R.layout.add_chat_item_for_adapter, parent, false);
	    }
	    AddChatChannel channel = (AddChatChannel) getItem(position);
	    //Log.i("myLogs", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
	    // заполняем View в пункте списка данными из товаров: наименование, цена
	    // и картинка
	    TextView textChannel =(TextView) view.findViewById(R.id.textChannelNames);
	    		textChannel.setText(Html.fromHtml(channel.channelId+"<br>"+"<\br>"+channel.name+"<\br>"));
	    		textChannel.setBackgroundColor(Integer.parseInt(channel.color));
	    
	    
	    ((ImageView) view.findViewById(R.id.imageSite)).setImageResource(channel.drawable);
	    ((ImageView) view.findViewById(R.id.imageSite)).setBackgroundColor(Integer.parseInt(channel.color));
	   // view.setLayoutParams((new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.WRAP_CONTENT)));
	    }
	    
	    return view;
	  }

	 

	/*  // содержимое корзины
	  ArrayList<Product> getBox() {
	    ArrayList<Product> box = new ArrayList<Product>();
	    for (Product p : objects) {
	      // если в корзине
	      if (p.box)
	        box.add(p);
	    }
	    return box;
	  }*/

	  
	}