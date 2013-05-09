package ivied.p001astreamchat;

import ivied.p001astreamchat.ChatList.CursorLoaderListFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.view.ActionProvider;

public class ActionProviderLink extends ActionProvider implements  OnItemSelectedListener {
	Spinner linkSpinner;
	final Uri INSERT_URI = Uri.parse("content://ivied.p001astreamchat/chats/insert");
	final static Pattern URL = Pattern.compile("(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+" +
			"([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
			//+"|(\\w+\\.)+(ru|com|org|tv|net|ua|at|edu|gov|int|mil|biz|πτ)+[^ ]*"
	Context mContext;
    public ActionProviderLink(Context context) {
        super(context);
        mContext = context;
    }
 
    @Override
    public View onCreateActionView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.link_layout,null);
        linkSpinner = (Spinner) view.findViewById(R.id.spinner_link);
        String[] selectionArgs = new String[] { CursorLoaderListFragment._id };
        Cursor c = mContext.getContentResolver().query(
				INSERT_URI, null, " _id = ?", selectionArgs, null);
        c.moveToNext();
        String msg = c.getString(5);
        
        Matcher m = URL.matcher(msg);	
     
        int i = 0;
        while ( m.find()) i++;
        m = URL.matcher(msg);
        String[] data = new String [i+1]; 
        data[0] = "Link";
    	   
        
        for (i = 0; m.find(); i++ ) {
        	data[i+1] = m.group();
        }
     // String[] data = {"one", "two", "three", "four", "five"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, data);
       
		
		linkSpinner.setAdapter(adapter);
		linkSpinner.setPrompt("Chose link");
		linkSpinner.setOnItemSelectedListener(this);
        return view;
    }
    @Override
    public boolean hasSubMenu() {
        return true;
    }


    @Override
    public boolean onPerformDefaultAction() {
        // This is called if the host menu item placed in the overflow menu of the
        // action bar is clicked and the host activity did not handle the click.
        return true;
    }

	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		String url =linkSpinner.getSelectedItem().toString();
		if (!url.equalsIgnoreCase("Link")){
			
		Uri uri =Uri.parse(linkSpinner.getSelectedItem().toString());	
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		
	      mContext.startActivity(intent);}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
   
}