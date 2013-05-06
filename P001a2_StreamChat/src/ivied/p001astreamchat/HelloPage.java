/*
 * Copyright (C) 2011 The Android Open Source Project
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

package ivied.p001astreamchat;

import ivied.p001astreamchat.R;
import ivied.p001astreamchat.HelloPage.onSomeEventListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class HelloPage extends SherlockFragment implements OnClickListener{
	public interface onSomeEventListener {
		public void stopService();
		public void stopServiceSend() ;
	}

	onSomeEventListener someEventListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			someEventListener = (onSomeEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement onSomeEventListener");
		}
	}
	final String LOG_TAG = "myLogs";
	Button addChat;
	Button login;
	Button stopStart;
	Button editChats;
	final int REQUEST_CHAT_ADD = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment, null);
		addChat = (Button) v.findViewById(R.id.createNewChat);
		addChat.setOnClickListener(this);
		login = (Button) v.findViewById(R.id.login);
		login.setOnClickListener(this);
		stopStart = (Button) v.findViewById(R.id.stopStart);
		stopStart.setOnClickListener(this);
		editChats = (Button) v.findViewById(R.id.editChats);
		editChats.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.createNewChat:
			intent = new Intent(getActivity(), EditChat.class);
			intent.putExtra("button", "Add");
			getActivity().startActivityForResult(intent, 2);

			break;
		case R.id.stopStart:

			someEventListener.stopService();
			someEventListener.stopServiceSend(); 
			break;

		case R.id.login:
			Intent intentLogin = new Intent(getActivity(), Login.class);
			
			getActivity().startActivityForResult(intentLogin, 3);
			break;
		case R.id.editChats:
			Intent intentEdit = new Intent(getActivity(), EditChat.class);
			intentEdit.putExtra("button", "Edit");
			getActivity().startActivityForResult(intentEdit, 1);
			break;

		default:
			break;
		}
	};


}
