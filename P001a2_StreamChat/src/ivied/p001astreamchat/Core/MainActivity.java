package ivied.p001astreamchat.Core;



import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import ivied.p001astreamchat.AddChat.AddChat;
import ivied.p001astreamchat.ChatView.ChatList;
import ivied.p001astreamchat.ChatView.DialogChoiceSmile;
import ivied.p001astreamchat.Sites.FactorySite;
import ivied.p001astreamchat.Login.Login;
import ivied.p001astreamchat.R;


public class MainActivity extends SherlockFragmentActivity implements MenuItem.OnMenuItemClickListener {
    public static final int MENU_ITEM_ID_CUTDOWN = 8;
    public static final int MENU_ITEM_ID_REFRESH = 9;
    public static Integer focus=0;
	TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    public final static String LOG_TAG = "myLogs";
	public final static int DELETE = 1;
    public final static int ID_LOGIN_FRAGMENTS =1000;
    public final static int ID_SITE_SELECT = 100;
	static int AMOUNT_OF_VISIBLE_ROWS;
	public static boolean messageStringShow;
    public static boolean messageLinksShow;
    public static boolean autoScrollChat;
    public static boolean messageDelete;
    public static boolean showSmiles;
    public static boolean showChannelsInfo;
    public static boolean showSiteLogo;
    public static boolean showNotifySystem;
    public static int HEIGHT_OF_VIDEO;
    static boolean showNotifyHeaders;
	public final static int EDIT = 2;
	public static final String CHANNEL = "channel";
	public static final String SITE = "site";
	static final String BROADCAST_ACTION = "ivied.p001astreamchat.servicebackbroadcast";
	BroadcastReceiver br;
    Intent intent;
    ChatService chatService;
    SendMessageService SendService;
 	boolean bound = false;
 	boolean boundSend = false;
 	DialogSendChannels dlgChoseChannels;
 	DialogChoiceSmile dlgChoiceSmile;
 	DialogFragment dlgStopService;
 	  SharedPreferences sp;
 	 final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
 	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");
 	
 	  public static List<String> indexOfChats = new ArrayList<String>();
 	  static List<TextView> indexOfHeaders = new ArrayList<TextView>();


    //	 private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

 	 
    public static final class TabInfo {
         
         private final int _id;
         public TabInfo(int _id) {
             
             this._id = _id;
         }
         
         
         public String findTag (){
     		
     		return indexOfChats.get(_id);
     	}
         
         public TextView findLabel() {
        	 return indexOfHeaders.get( _id);
         }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		super.onCreate(savedInstanceState);
		indexOfChats.clear();
		indexOfHeaders.clear();
		setContentView(R.layout.fragment_tabs_pager);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		br = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {

				
				String [] chats = getChatNamesToNotif(intent);
				
				for (String chat : chats) {
					if (!mTabHost.getCurrentTabTag().equalsIgnoreCase(chat)) {
						// TabInfo tab = new
						// TabInfo(indexOfChats.indexOf(chat));
						TextView label = (TextView) mTabHost.getTabWidget()
								.getChildTabViewAt(indexOfChats.indexOf(chat))
								.findViewById(android.R.id.title);


						label.setCompoundDrawablesWithIntrinsicBounds(
								getResources()
										.getDrawable(
												android.R.drawable.radiobutton_on_background),
								null, null, null);
						Log.d(LOG_TAG, "onReceive: task = " + chat);
					}
				}
				
			}
		};

		
		intent = new Intent(this, ChatService.class);
		bindService(intent, sConn, 0);
		Log.d(LOG_TAG, "MyService onCreate");
		startService(intent);
		intent = new Intent(this, SendMessageService.class);
		startService(intent);
		bindService(intent, sendConn, 0);
		
		Intent intentFocus = getIntent();
		///Log.d(LOG_TAG, "chat intent =  " + intent.getCharSequenceExtra(CHAT_NAME));

		Cursor c = getContentResolver().query(SERVICE_URI, null, null, null, null);
		if (c.getCount() ==  0){
			intent = new Intent(this, AddChat.class);
			intent.putExtra("button", "Add");
			startActivityForResult(intent, 2);
		}
        c.close();
    	loadSavedChats();
        if( intentFocus.hasExtra(CHANNEL)){
            String [] chats = getChatNamesToNotif(intentFocus);
            Log.d(LOG_TAG, "chat intent =  " + chats[0]);
            mTabHost.setCurrentTab(indexOfChats.indexOf(chats[0]));}
	}
    
    protected String[] getChatNamesToNotif(Intent intent) {
		// TODO Auto-generated method stub
    	String channel = intent.getStringExtra(CHANNEL);
        FactorySite.SiteName site = (FactorySite.SiteName) intent.getSerializableExtra(SITE);
    	String[] selectionArgsNotify = new String[] { channel, site.name()};
		String[] projectionNotify = new String[] { "chat" };
		Cursor notify = getContentResolver().query(ADD_URI,
				projectionNotify, "channel = ? AND site = ?", selectionArgsNotify,
				null);
		String[] chats =new String [notify.getCount()];
		int i=0;
		for (notify.moveToFirst(); !notify.isAfterLast(); notify
				.moveToNext()) {
			
			 chats[i] = notify.getString(0);
			i++;
		}
        notify.close();
		return chats;
	}

	@Override
    public void onStart(){
    	super.onStart();
    	getPrefs();
    	
    }
	 
	@Override
    public void onResume(){
    	super.onResume();
    	IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
    	registerReceiver(br, intFilt);
    
		
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK&& event.getRepeatCount() == 0) {
            // do something on back.


            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }




    public void addSmile (View v) {
    	dlgChoiceSmile = new DialogChoiceSmile();
    	dlgChoiceSmile.show(getSupportFragmentManager(), "Smile");
    }

    public void pressEnter(View v){
    	
    	/*String chtaName = mTabHost.getCurrentTab();
    	String [] projection =  new String[] { "site", "channel"}; 
		String [] selectionArgs = new String [] {chatName, "true"};
    	Cursor c = getContentResolver().query
				(ADD_URI, projection, "chat = ? AND flag = ?", selectionArgs, null);
		if (c.getCount()== 0) Toast.makeText(MyApp.getContext(), "" + getResources().getString(R.string.notify_channels_not_set) , Toast.LENGTH_SHORT).show();
		*/
    	EditText  textOfMessage = (EditText) 
    			findViewById(mTabHost.getCurrentTab()+1);
    	Log.i(MainActivity.LOG_TAG, "edit = " + mTabHost.getCurrentTab() );
    	String text = textOfMessage.getText().toString();	
    	SendService.sendMessage(text, mTabHost.getCurrentTabTag());		
    	if  (messageDelete)     textOfMessage.setText("");  
    	
    }

    /*public void addStreamTab () {
        mTabHost.setup();
        mTabsAdapter.addTab(mTabHost.newTabSpec("VideoView").setIndicator("VideoView"), FragmentWebView.class, null);
        indexOfChats.add("VideoView");
        final TextView label = (TextView) mTabHost.getTabWidget().getChildTabViewAt(indexOfChats.indexOf("VideoView"))
                .findViewById(android.R.id.title);
        // label.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.radiobutton_off_background), null, null, null);
        indexOfHeaders.add(label);

    }*/
    
   
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    
	public void loadSavedChats() {
		Cursor c = getContentResolver().query(SERVICE_URI,
				new String[] { "chat" }, null, null, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			String chatName = c.getString(0);
			addTab(chatName);
			Log.i(LOG_TAG, "chat = " + chatName);
			 final TextView label = (TextView) mTabHost.getTabWidget().getChildTabViewAt(indexOfChats.indexOf(chatName))
		    		   .findViewById(android.R.id.title);
			// label.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.radiobutton_off_background), null, null, null);
		    indexOfHeaders.add(label);
			 
		}
        c.close();
	}
	
	public void stopServiceSend() {

		  stopService(new Intent(this,SendMessageService.class));
	  }
	
 
	
	public void stopService() {

		  stopService(new Intent(this,ChatService.class));
		 
	  }
	
	private ServiceConnection sConn = new ServiceConnection() {
      public void onServiceConnected(ComponentName name, IBinder service) {
      	ChatService.ChatBinder binder = (ChatService.ChatBinder) service;
      	chatService = binder.getService();
        Log.d(LOG_TAG, "MainActivity onServiceConnectedd");
        bound = true;
        
      }

      public void onServiceDisconnected(ComponentName name) {
        Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
        bound = false;
      }
    };
    
    private ServiceConnection sendConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
        	SendMessageService.SendBinder binder = (SendMessageService.SendBinder) service;
        	SendService = binder.getService();
          Log.d(LOG_TAG, "MainActivity onServiceConnected");
          boundSend = true;
          
        }

        public void onServiceDisconnected(ComponentName name) {
          Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
          boundSend = false;
        }
      };
      
    public void addTab (String chatName) { 
    	
    	mTabHost.setup();
    	 mTabsAdapter.addTab(mTabHost.newTabSpec(chatName).setIndicator(chatName/*, getResources().getDrawable(android.R.drawable.presence_online)*/),
                 ChatList.CursorLoaderListFragment.class, null);
    	
       
        indexOfChats.add(chatName);
      
        if (!bound) return;
	    Log.i(LOG_TAG, "here");
	   chatService.startChatThread(chatName);

    }
   
    public void restartApp () {
        Intent i;
        stopService(new Intent(this,SendMessageService.class));
        stopService(new Intent(this,ChatService.class));
        MyApp.factoryReset();
        i = new Intent(MyApp.getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApp.getContext().startActivity(i);

		
	
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// çàïèøåì â ëîã çíà÷åíèÿ requestCode è resultCode
		Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = "
                + resultCode);
		// åñëè ïðèøëî ÎÊ
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				String chatName = data.getStringExtra("name");
				int action = data.getIntExtra("action", 0);
				switch (action) {
				case DELETE:
					restartApp () ;
					
					break;
				case EDIT:
					restartApp () ;
					//TODO íå îáíîâëÿþòñÿ ïîòîêè
					break;
				}

				break;
			case 2:
				 
				///String chatNameADD = data.getStringExtra("name");
				
					/*if (indexOfChats.size() > 2) {
						addTab(chatNameADD);
					} else {*/
				restartApp () ;

					

				/*}*/
				break;
			case 3:
				
				
				intent = new Intent(this, SendMessageService.class);
				unbindService(sendConn);

				stopServiceSend();
				startService(intent);
				bindService(intent, sendConn, 0);
			
			break;}

			// åñëè âåðíóëîñü íå ÎÊ
		} 
		
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem cutDown = menu.add(1, MENU_ITEM_ID_CUTDOWN, 0, "Cut down");
        cutDown.setIcon(android.R.drawable.arrow_down_float);
        cutDown.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cutDown.setOnMenuItemClickListener(this);

        MenuItem refresh = menu.add(1, MENU_ITEM_ID_REFRESH,0,"Refresh");
        refresh.setIcon(android.R.drawable.stat_notify_sync);
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh.setOnMenuItemClickListener(this);

        menu.add(0, 1, 1, "Preferences");
        menu.add(0, 2, 0, "Channels");
        menu.add(0,3,2, "Add chat");
        menu.add(0,4,3, "Edit chat");
        menu.add(0,5,4, "Login");
        menu.add(0,6,5, "Stop App");
        menu.add(0,7,6, "Help");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case MENU_ITEM_ID_REFRESH:
                restartApp();
                break;
            case MENU_ITEM_ID_CUTDOWN:
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case 1:
                Intent intent = new Intent(this, Preference.class);
                startActivity(intent);
                break;
            case 2:
                dlgChoseChannels= DialogSendChannels.newInstance( mTabHost.getCurrentTabTag());
                dlgChoseChannels.show(getSupportFragmentManager(),  mTabHost.getCurrentTabTag());

                break;
            case 3:
                intent = new Intent(this, AddChat.class);
                intent.putExtra("button", "Add");
                startActivityForResult(intent, 2);
                break;
            case 4:
                Intent intentEdit = new Intent(this, AddChat.class);
                intentEdit.putExtra("button", "Edit");
                startActivityForResult(intentEdit, 1);
                break;
            case 5:
                Intent intentLogin = new Intent(this, Login.class);
                startActivityForResult(intentLogin, 3);
                break;
            case 6:
                dlgStopService = new DialogStopService();
                dlgStopService.show(getSupportFragmentManager(), "Stop service");
                break;
            case 7:
                intent = new Intent (this, Help.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    private void getPrefs() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        messageStringShow = prefs.getBoolean("stringMessage", true);
        AMOUNT_OF_VISIBLE_ROWS =Integer.parseInt( prefs.getString("count", "30"));
        messageLinksShow = prefs.getBoolean("linkShow", true);
        autoScrollChat = prefs.getBoolean("autoScroll", true);
        messageDelete = prefs.getBoolean("deleteMessage", true);
        showSmiles = prefs.getBoolean("showSmiles", true);
        showChannelsInfo = prefs.getBoolean("showChannelInfo", true);
        showSiteLogo = prefs.getBoolean("showSiteLogo", true);
        showNotifyHeaders = prefs.getBoolean("notifHeaders", true);
        showNotifySystem = prefs.getBoolean("notifSystem", true);
        HEIGHT_OF_VIDEO = Integer.parseInt( prefs.getString("videoPixels", "250"));
    }
	 
	
	 /*@Override
		public void onTabChanged(String tabId) {
			/// TODO Auto-generated method stub
		
		}*/
	 @Override
	    public void onPause(){
		 unregisterReceiver(br);
		 super.onPause();
		 
	 }
	 
	 @Override
	    public void onDestroy(){

	    	//unbindService(sConn);
	    	//unbindService(sendConn);
	    	//stopServiceSend();
	    	//stopService();
	    	super.onDestroy();
	    }
	 
	 
    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
          ///  notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
            
 		// Log.d("myLogs", "label = " + label.getText().toString());
           
      // 
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
            focus= position;
            
        
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    

	
  
}
