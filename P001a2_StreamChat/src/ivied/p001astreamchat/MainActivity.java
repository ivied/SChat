package ivied.p001astreamchat;



import ivied.p001astreamchat.ChatService.ChatBinder;
import ivied.p001astreamchat.HelloPage.onSomeEventListener;
import ivied.p001astreamchat.SendMessageService.SendBinder;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockFragmentActivity implements onSomeEventListener/*, OnTabChangeListener*/{
    static Integer focus;
	TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    final static int ADD = 1;
    final static String LOG_TAG = "myLogs";
	final static int DELETE = 1;
	static int AMOUNT_OF_VISIBLE_ROWS;
	static boolean messageStringShow;
	static boolean messageLinksShow;
	static boolean autoScrollChat;
	final static int EDIT = 2;
	public static final String CHAT_NAME = "chat";
	public static final int NEW_MESSAGE = 1;
	static final String BROADCAST_ACTION = "ivied.p001astreamchat.servicebackbroadcast";
	BroadcastReceiver br;
    Intent intent;
    ChatService chatService;
    SendMessageService SendService;
 	boolean bound = false;
 	boolean boundSend = false;
 	DialogSendChannels dlgChoseChannels;
 	DialogChoiceSmile dlgChoseSmile;
 	  SharedPreferences sp;
 	 final Uri ADD_URI = Uri.parse("content://ivied.p001astreamchat/channels/add");
 	final Uri SERVICE_URI = Uri.parse("content://ivied.p001astreamchat/channels/service");
 	
 	  static List<String> indexOfChats = new ArrayList<String>();
 	  static List<TextView> indexOfHeaders = new ArrayList<TextView>();
 //	 private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

 	 
     static final class TabInfo {
         
         private final int _id;
         TabInfo( int _id) {
             
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

		setContentView(R.layout.fragment_tabs_pager);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		mTabsAdapter.addTab(mTabHost.newTabSpec("simple").setIndicator("Menu"),
				HelloPage.class, null);
		indexOfChats.clear();
		indexOfChats.add("simple");
		indexOfHeaders.clear();
		indexOfHeaders.add(null);

		sp = PreferenceManager.getDefaultSharedPreferences(this);
		/*
		 * mTabsAdapter.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom"
		 * ), LoaderCustomSupport.AppListFragment.class, null);
		 * mTabsAdapter.addTab
		 * (mTabHost.newTabSpec("throttle").setIndicator("Throttle"),
		 * LoaderThrottleSupport.ThrottledLoaderListFragment.class, null);
		 * 
		 * if (savedInstanceState != null) {
		 * mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); }
		 */
		// mTabHost.setOnTabChangedListener(this);
		br = new BroadcastReceiver() {
			// действия при получении сообщений
			public void onReceive(Context context, Intent intent) {

				String channel = intent.getStringExtra(CHAT_NAME);
				String[] selectionArgsNotify = new String[] { channel };
				String[] projectionNotify = new String[] { "chat" };
				Cursor notify = getContentResolver().query(ADD_URI,
						projectionNotify, "channel = ?", selectionArgsNotify,
						null);
				Log.d(MainActivity.LOG_TAG,
						"размер курсора = " + notify.getCount());
				for (notify.moveToFirst(); !notify.isAfterLast(); notify
						.moveToNext()) {

					String chat = notify.getString(0);

					// TabInfo tab = new TabInfo(indexOfChats.indexOf(chat));
					TextView label = (TextView) mTabHost.getTabWidget().getChildTabViewAt(indexOfChats.indexOf(chat))
				    		   .findViewById(android.R.id.title);
					Log.d(MainActivity.LOG_TAG, chat + "индекс чата = "
							+ MainActivity.indexOfChats.indexOf(chat));
					
				

					label.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.radiobutton_on_background), null, null, null);
					Log.d(LOG_TAG, "onReceive: task = " + chat);

					
				}
			}
		};
		// создаем фильтр для BroadcastReceiver
		IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		// регистрируем (включаем) BroadcastReceiver
		registerReceiver(br, intFilt);
		intent = new Intent(this, ChatService.class);
		bindService(intent, sConn, 0);
		Log.d(LOG_TAG, "MyService onCreate");
		startService(intent);
		intent = new Intent(this, SendMessageService.class);
		startService(intent);
		bindService(intent, sendConn, 0);
		loadSavedChats();

	}
    
    @Override
    public void onStart(){
    	super.onStart();
    	getPrefs();
    	
    }
    
    public void addSmile (View v) {
    	dlgChoseSmile = new DialogChoiceSmile();
    	dlgChoseSmile.show(getSupportFragmentManager(), "Smile");
    }

    public void pressEnter(View v){
    	EditText  textOfMessage = (EditText) findViewById(
		mTabHost.getCurrentTab());
    	Log.i(MainActivity.LOG_TAG, "edit = " + mTabHost.getCurrentTab() );
    	String text = textOfMessage.getText().toString();	
    	SendService.sendMessage(text, mTabHost.getCurrentTabTag());		
    textOfMessage.setText("");  
    	
    }
    
  
    
   
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

	}
	
	public void stopServiceSend() {
		  stopService(new Intent(this,SendMessageService.class));
	  }
	
 
	
	public void stopService() {
		  stopService(new Intent(this,ChatService.class));
		 
	  }
	
	private ServiceConnection sConn = new ServiceConnection() {
      public void onServiceConnected(ComponentName name, IBinder service) {
      	ChatBinder binder = (ChatBinder) service;
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
        	SendBinder binder = (SendBinder) service;
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
    	stopService();
		stopServiceSend();
		
		MyApp.factoryReset();
		i = new Intent(MyApp.getContext(), MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MyApp.getContext().startActivity(i);
		
	
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent i;
		// запишем в лог значения requestCode и resultCode
		Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = "
				+ resultCode);
		// если пришло ОК
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
					//TODO не обновляются потоки
					break;
				}

				break;
			case 2:
				//String chatNameADD = data.getStringExtra("name");
				
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

			// если вернулось не ОК
		} 
		
	}
 
	 public boolean onCreateOptionsMenu(Menu menu) {
		    menu.add(0, 1, 1, "Preferences");
		    menu.add(0,2,0, "Channels");
		   
		    return super.onCreateOptionsMenu(menu);
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
         
 }
	 /*@Override
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
		
		}*/
	 
	 @Override
	    public void onDestroy(){
	    	
	    	unbindService(sConn);
	    	unbindService(sendConn);
	    	stopServiceSend();
	    	//unregisterReceiver(br);
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
            notifyDataSetChanged();
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
