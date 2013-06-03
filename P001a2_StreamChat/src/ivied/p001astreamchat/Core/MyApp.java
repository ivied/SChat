package ivied.p001astreamchat.Core;

import android.app.Application;
import android.content.Context;

	public class MyApp extends Application {
	    private  static Context context;
        static public SmileHelper SmileCache = new SmileHelper();
	    @Override
	    public void onCreate() {
	        super.onCreate();

	        context = getApplicationContext();
	        
	    }

	    public static Context getContext() {
	        return context;
	    }

	    public static void factoryReset() {
	        // ...
	    }
	}

