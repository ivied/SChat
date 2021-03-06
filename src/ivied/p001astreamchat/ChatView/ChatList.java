/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ivied.p001astreamchat.ChatView;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ivied.p001astreamchat.Core.MainActivity;
import ivied.p001astreamchat.Core.MainActivity.TabInfo;
import ivied.p001astreamchat.Core.MyApp;
import ivied.p001astreamchat.Core.MyContentProvider;
import ivied.p001astreamchat.Core.SendMessageService;
import ivied.p001astreamchat.R;
import ivied.p001astreamchat.Sites.FactoryVideoViewSetter;
import ivied.p001astreamchat.Sites.VideoViewSetter;
import ivied.p001astreamchat.VideoView.HTML5WebView;

/**
 * Demonstration of the use of a CursorLoader to load and display contacts data
 * in a fragment.
 */
@SuppressWarnings("all")
public class ChatList extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment list = new CursorLoaderListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();

		}

	}

	public static class CursorLoaderListFragment extends SherlockListFragment
			implements LoaderManager.LoaderCallbacks<Cursor>,
			OnItemClickListener, OnMenuItemClickListener, VideoViewSetter.SetVideoView {

		private ListView listView;
		private ActionMode mMode;
		static String _id;
		final String LOG_TAG = "myLogs";

		final Uri CONTACT_URI = Uri
				.parse("content://ivied.p001astreamchat/chats/show");
		final Uri ADD_URI = Uri
				.parse("content://ivied.p001astreamchat/channels/add");
		final Uri INSERT_URI = Uri
				.parse("content://ivied.p001astreamchat/chats/insert");

		public String[] selectionArgs;
		String selection = "";
		SharedPreferences preferences;
		// This is the Adapter being used to display the list's data.
		AdapterChatCursor mAdapter;

		// If non-null, this is the current filter the user has provided.
		String mCurFilter;

		public ListView mList;
		boolean mListShown;
		View mProgressContainer;
		View mListContainer;
		String chatName;
		public static int tagNumber=0;
		SendMessageService SendService;
        FrameLayout streamLayout;
        WebView streamView;
        RelativeLayout chatLayout;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String tag = this.getTag();
            String delims = "[:]";
            String[] tokens = tag.split(delims);

            tagNumber = Integer.parseInt(tokens[3]);


            View root = inflater.inflate(R.layout.list_content, container,
                    false);
            if (!MainActivity.messageStringShow) {
                (root.findViewById(R.id.smiles)).setVisibility(View.GONE);
                (root.findViewById(R.id.enter)).setVisibility(View.GONE);
                (root.findViewById(R.id.textOfMessage))
                        .setVisibility(View.GONE);
            }


            Log.i(MainActivity.LOG_TAG, "id textOfMessage = " + tagNumber);
            (root.findViewById(R.id.textOfMessage)).setId(tagNumber + 1);



    /*        String path="http://usher.twitch.tv/stream/multi_playlist/hail9.m3u8?allow_cdn=true&token=a7b5781edab4256f5f1781d1d4dfc87db5a03735%3A272320e78c29de3866fbdfa923773906b2af06af%3A%7B%22expiration%22%3A%201371563311.483093%2C%20%22channel%22%3A%20%22hail9%22%2C%20%22user_agent%22%3A%20%22.*%22%7D&hd=true";
            String path1="http://commonsware.com/misc/test2.3gp";

            Uri uri=Uri.parse(path);

            VideoView video=(VideoView) root.findViewById(R.id.videoView);
            video.setVideoURI(uri);
            video.start();*/



            mList = (ListView) root.findViewById(android.R.id.list);

            mListContainer = root.findViewById(R.id.listContainer);
            mProgressContainer = root.findViewById(R.id.progressContainer);
            mListShown = true;
            TabInfo tab = new TabInfo(tagNumber);
            chatName = tab.findTag();
            chatLayout = (RelativeLayout) root.findViewById(R.id.chatLayout);
            getStreamLayout();

            return root;
        }
		

		public void setListShown(boolean shown, boolean animate) {
			if (mListShown == shown) {
				return;
			}
			mListShown = shown;
			if (shown) {
				if (animate) {
					mProgressContainer.startAnimation(AnimationUtils
							.loadAnimation(getActivity(),
									android.R.anim.fade_out));
					mListContainer.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), android.R.anim.fade_in));

				}
				mProgressContainer.setVisibility(View.GONE);
				mListContainer.setVisibility(View.VISIBLE);

			} else {
				if (animate) {
					mProgressContainer.startAnimation(AnimationUtils
							.loadAnimation(getActivity(),
									android.R.anim.fade_in));
					mListContainer.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), android.R.anim.fade_out));

				}
				mProgressContainer.setVisibility(View.VISIBLE);
				mListContainer.setVisibility(View.INVISIBLE);

			}
		}

		public void setListShown(boolean shown) {
			setListShown(shown, true);
		}

		public void setListShownNoAnimation(boolean shown) {
			setListShown(shown, false);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);


			// Give some text to display if there is no data. In a real
			// application this would come from a resource.
			//setEmptyText(getResources().getText(R.string.default_message));

			setHasOptionsMenu(true);

			String[] from = new String[] {
					MyContentProvider.MESSAGES_SITE_NAME,
					MyContentProvider.MESSAGES_NICK_NAME,
					MyContentProvider.MESSAGES_MESSAGE,
					MyContentProvider.MESSAGES_PERSONAL,
					MyContentProvider.MESSAGES_COLOR,
					
					};
			int[] to = new int[] { R.id.ivImg, 0, R.id.tvText, R.id.channelName, 0};
			// Initialize the adapter.
			mAdapter = new AdapterChatCursor(getActivity(), R.layout.message,
					null, from, to, 0);

			listView = mList;
			// lvData.setAdapter(adapter);*/

			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.setOnItemClickListener(this);
			if (!MainActivity.autoScrollChat)listView.setTranscriptMode(0);
			setListAdapter(mAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.

			getLoaderManager().initLoader(tagNumber, null, this);


		}
		@Override
	    public void setMenuVisibility(final boolean visible) {
	        super.setMenuVisibility(visible);
	      try{
	        if (visible) {
	        	String tag = this.getTag();
				String delims = "[:]";
				String[] tokens = tag.split(delims);

				int tagNum = Integer.parseInt(tokens[3]);
				Log.i(LOG_TAG, "tag" + tagNum);
				if(tagNum>=0){
				TabInfo tab = new TabInfo(tagNum);
				

				
				TextView label = tab.findLabel();
		       label.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.radiobutton_off_background), null, null, null);}
	        }
          } catch (IllegalStateException e) {
	        	
	        }
	    }
		@Override 
		public void onResume(){
			super.onResume();
			
	          // Log.i(LOG_TAG, "lrol" + label.getTextColors());*/
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Notice how the ListView api is lame
			// You can use mListView.getCheckedItemIds() if the adapter
			// has stable ids, e.g you're using a CursorAdaptor
			SparseBooleanArray checked = listView.getCheckedItemPositions();
			boolean hasCheckedElement = false;
			for (int i = 0; i < checked.size() && !hasCheckedElement; i++) {
				hasCheckedElement = checked.valueAt(i);
			}
			_id = String.valueOf(id);
			if (hasCheckedElement) {
				if (mMode == null) {
					Log.i(LOG_TAG, "chek");
					mMode = getSherlockActivity().startActionMode(callback);

				}
			} else {
				if (mMode != null) {
					mMode.finish();
				}
			}
		};

		private ActionMode.Callback callback = new ActionMode.Callback() {

			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.message_menu, menu);
				return true;
			}

			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Here, you can checked selected items to adapt available
				// actions
				return false;
			}

			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				long[] selected = listView.getCheckedItemIds();

				String[] selectionArgs = new String[] { _id };
				Cursor c = getSherlockActivity().getContentResolver().query(
						INSERT_URI, null, " _id = ?", selectionArgs, null);
				c.moveToNext();
				if (selected.length > 0) {

					switch (item.getItemId()) {
					case R.id.action_link:

						Log.i(LOG_TAG, "Item clicked: " + _id);
						break;
					case R.id.action_add_to_clipboard:
						Log.i(LOG_TAG, "Item clicked: clipboard");
						String message = c.getString(5);
						int sdk = android.os.Build.VERSION.SDK_INT;
						if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
							android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSherlockActivity()
									.getSystemService(Context.CLIPBOARD_SERVICE);
							clipboard.setText(message);
						}

						break;
					case R.id.action_private:

						String nick = c.getString(4);
						EditText msg = (EditText) getSherlockActivity()
								.findViewById(MainActivity.focus+1);

						String site = c.getString(2);
						if (site.equalsIgnoreCase("sc2tv"))
							msg.setText("[b]" + nick + "[/b], "
									+ msg.getText().toString());
						else
							msg.setText(nick + ", " + msg.getText().toString());
						String channel = c.getString(3);
						TabInfo tab = new TabInfo(MainActivity.focus);
						String chatName = tab.findTag();
						selectionArgs[0] = chatName;
						ContentValues cv = new ContentValues();
						cv.put("flag", "false");
						getSherlockActivity().getContentResolver().update(
								INSERT_URI, cv, " chat = ?", selectionArgs);
						cv.put("flag", "true");
						String[] selectionArgs2 = new String[] { chatName,
								site, channel };
						getSherlockActivity().getContentResolver().update(
								INSERT_URI, cv,
								" chat = ? AND site = ? AND channel = ?",
								selectionArgs2);

						break;// Do something with the selected item
					}

				}
                c.close();
				mode.finish();
				return true;
			}

			public void onDestroyActionMode(ActionMode mode) {
				// Destroying action mode, let's unselect all items
				for (int i = 0; i < listView.getAdapter().getCount(); i++)
					listView.setItemChecked(i, false);

				if (mode == mMode) {
					mMode = null;
				}
			}

		};

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

			// Place an action bar item for searching.
			/*MenuItem item = menu.add("Search");
			item.setIcon(android.R.drawable.ic_menu_search);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			SherlockFragmentActivity activity = (SherlockFragmentActivity) getActivity();
			View searchView = SearchViewCompat.newSearchView(activity
					.getSupportActionBar().getThemedContext());
			if (searchView != null) {
				SearchViewCompat.setOnQueryTextListener(searchView,
						new OnQueryTextListenerCompat() {
							@Override
							public boolean onQueryTextChange(String newText) {*/
								// Called when the action bar search text has
								// changed. Update
								// the search filter, and restart the loader to
								// do a new query
								// with this filter.
							/*	mCurFilter = !TextUtils.isEmpty(newText) ? newText
										: null;*/
								// getLoaderManager().restartLoader(0, null,
								// CursorLoaderListFragment.this);
								/*return true;
							}
						});
				item.setActionView(searchView);
			}*/
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Insert desired behavior here.
			Log.i("FragmentComplexList", "Item clicked: " + id);
		}

	

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {


			TabInfo tab = new TabInfo(id);
			
			String chatName = tab.findTag();
			String[] name = { chatName };
			Cursor c = getActivity().getContentResolver().query(ADD_URI, null,
					"chat = ?", name, null);
			String[] selectionArgs = new String[c.getCount()];
			List<String> channels = new ArrayList<String>();
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				// The Cursor is now set to the right position

				channels.add(c.getString(c.getColumnIndex("channel")));

			}
            c.close();
			selectionArgs = channels.toArray(selectionArgs);
			Log.i(LOG_TAG, "selectionArgs = " + selectionArgs.toString());
			for (int i = 0; i < channels.size(); i++) {
				if (i > 0)
					selection = selection.concat("OR ");
				selection = selection.concat("channel = ? ");

			}

			Loader<Cursor> mLoader = new CursorLoader(getActivity(),
					CONTACT_URI, null, selection, selectionArgs, null);

			return mLoader;

		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			
			Log.i(LOG_TAG, "lol" + this.getTag().toString().hashCode());
			
			// Swap the new cursor in. (The framework will take care of closing
			// the
			// old cursor once we return.)

			mAdapter.swapCursor(data);
			// setSelection(data.getCount());

			// The list should now be shown.
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {

			Log.i(LOG_TAG, "3" + this.getTag().toString().hashCode());
			// This is called when the last Cursor provided to onLoadFinished()
			// above is about to be closed. We need to make sure we are no
			// longer using it.
			mAdapter.swapCursor(null);
		}


        @Override
        public void onStop() {
            clearCache(MyApp.getContext(),0);
           super.onStop();
        }

        static int clearCacheFolder(final File dir, final int numDays) {

            int deletedFiles = 0;
            if (dir!= null && dir.isDirectory()) {
                try {
                    for (File child:dir.listFiles()) {

                        //first delete subdirectories recursively
                        if (child.isDirectory()) {
                            deletedFiles += clearCacheFolder(child, numDays);
                        }

                        //then delete the files and subdirectories in this dir
                        //only empty directories can be deleted, so subdirs have been done first
                        if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                            if (child.delete()) {
                                deletedFiles++;
                            }
                        }
                    }
                }
                catch(Exception e) {
                    Log.e(MainActivity.LOG_TAG, String.format("Failed to clean the cache, error %s", e.getMessage()));
                }
            }
            return deletedFiles;
        }

        /*
         * Delete the files older than numDays days from the application cache
         * 0 means all files.
         */
        public static void clearCache(final Context context, final int numDays) {
          //  MyApp.getContext().deleteDatabase("webview.db");
          //  MyApp.getContext().deleteDatabase("webviewCache.db");
            Log.i(MainActivity.LOG_TAG, String.format("Starting cache prune, deleting files older than %d days", numDays));
            int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
            Log.i(MainActivity.LOG_TAG, String.format("Cache pruning completed, %d files deleted", numDeletedFiles));
        }
		@Override
		public boolean onMenuItemClick(MenuItem item) {

			return false;
		}

        public void getStreamLayout() {

            Cursor query = MyApp.getContext().getContentResolver().query(ADD_URI,
                    new String[]{"site", "channel"}, "chat = ?",
                    new String[]{chatName}, null);
            for (query.moveToFirst(); !query.isAfterLast(); query.moveToNext() ){
                try {

                    FactoryVideoViewSetter factory = new FactoryVideoViewSetter();
                    VideoViewSetter videoViewSetter = factory.getVideoSite(FactoryVideoViewSetter.VideoSiteName.valueOf(query.getString(0)));
                    videoViewSetter.getVideoView(query.getString(1), getSherlockActivity(), this);


                }catch (IllegalArgumentException e){

                }

            }
            query.close();


        }

        @Override
        public void setVideoView(HTML5WebView html5WebView, String url, FactoryVideoViewSetter.VideoSiteName videoSiteName) {
            streamLayout = html5WebView.getLayout();
            String[] selectionArgs = new String[]{chatName};
            FrameLayout.LayoutParams layoutParams;
            Cursor cursor = getSherlockActivity().getContentResolver().query
                    (ADD_URI, null, "chat = ?", selectionArgs, null);

            if (cursor.getCount() == 1) {
                layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            } else {
                layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, MainActivity.HEIGHT_OF_VIDEO);
            }
            cursor.close();
            streamLayout.setLayoutParams(layoutParams);
            chatLayout.addView(streamLayout);

        }
    }

}
