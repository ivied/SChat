package ivied.p001astreamchat.VideoView;



import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;

import ivied.p001astreamchat.ChatView.ChatList;
import ivied.p001astreamchat.Core.MainActivity;

public class FragmentWebView extends SherlockFragment{
HTML5WebView mWebView;
final static int ViewId = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new HTML5WebView(getActivity());
        /*FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams (
                100,100);
        mWebView.setId(1);
        mWebView.setLayoutParams(lp);*/
        //mWebView.setId(ChatList.CursorLoaderListFragment.tagNumber +1 );
        if (savedInstanceState != null) {
        	mWebView.restoreState(savedInstanceState);

        } else {
            //mWebView.loadUrl("http://freebsd.csie.nctu.edu.tw/~freedom/html5/");
            //mWebView.loadUrl("http://goodgame.ru/player3?4986");
        }
        
        //setContentView(mWebView.getLayout());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mWebView.getLayout();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mWebView.saveState(outState);
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	mWebView.stopLoading();
    }
    
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.inCustomView()) {
            	mWebView.hideCustomView();
            	return true;
            }
    	}
    	return super.onKeyDown(keyCode, event);
    }*/
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
    }

}
