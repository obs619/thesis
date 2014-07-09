/*
 ********************************************************************************
 * Copyright (c) 2013 Samsung Electronics, Inc.
 * All rights reserved.
 *
 * This software is a confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung Electronics.
 ********************************************************************************
 */
package com.cardgame.uiOLD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cardgame.chord.ChatChord;
import com.cardgame.screenapi.chordimpl.ChordMessage;
import com.cardgame.screenapi.chordimpl.ChordMessage.MessageOwner;
import com.cardgame.uiOLD.ChannelNameDialog.OnAddChannelListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.chord.Schord;
import com.samsung.android.sdk.chord.SchordManager;
import com.srpol.chordchat.R;

public class MainActivity extends Activity implements OnAddChannelListener {

	public static final String TAG = "ChordChat";
	private static final String CHAT_PREFERENCES = "chat_preferences";
	private static final String USER_NAME_KEY = "user_name";
	private static final String CHAT_FRAGMENT_TAG = "chat_fragment";

	private static final String PUBLIC_CHANNEL_NAME = "Public";

	private RelativeLayout mDrawerFrame;
	private DrawerLayout mDrawerLayout;
	private ListView mChannelsListView;
	private ActionBarDrawerToggle mDrawerToggle;
	private TextView mInputMessageView;
	private View mSendMessageView;
	public static TextView mUserNameView;
	private View mWifiEnabled;
	private View mWifiDisabled;

	private Map<String, ChatFragment> mFragments;
	private List<String> mChannels;
	private BaseAdapter mChannelsAdapter;
	private FragmentManager mFragmentManager;
	private ChatChord mChatChord;

	private String mUserName;
	private String mCurrentChannelName;

	private static final IntentFilter INTENT_FILTER = new IntentFilter();

	
	public static Map<String, String> map;
	public static Spinner spinnerUsernames;
	public static List<String> listUsernames;
	public static ArrayAdapter<String> dataAdapter;
	public static String currUserNodeName = "";
	
	private TextView mScreenTypeView;
	public static RelativeLayout mInputContainer;
	
	static {
		INTENT_FILTER.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	}

	 /**
	  * @param interface type:
	  * 		- SchordManager.INTERFACE_TYPE_...
	  * @return connection type name:
	  * 		- Wi-Fi (no Internet involved)
	  * 		- Wi-Fi Direct
	  * 		- Mobile AP
	  */
	private String getInterfaceName(int interfaceType) {
        if (SchordManager.INTERFACE_TYPE_WIFI == interfaceType)
            return "Wi-Fi";
        else if (SchordManager.INTERFACE_TYPE_WIFI_AP == interfaceType)
            return "Mobile AP";
        else if (SchordManager.INTERFACE_TYPE_WIFI_P2P == interfaceType)
            return "Wi-Fi Direct";

     // Connection type is unknown
        return "UNKNOWN";
    }
	
	private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			mChatChord.startChord();
			
			// Show interface type in toast
			Toast.makeText(getApplicationContext(), "ChatChord interfaceType is: " + getInterfaceName(ChatChord.interfaceType), Toast.LENGTH_LONG).show();
			
			if (ChatChord.result != 0) {
				// Chord successfully connected
				mSendMessageView.setEnabled(false);
				mWifiDisabled.setVisibility(View.VISIBLE);
				mWifiEnabled.setVisibility(View.INVISIBLE);
			} else {
				// Chord failed to connect
				mWifiEnabled.setVisibility(View.VISIBLE);
				mWifiDisabled.setVisibility(View.INVISIBLE);
			}
			
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		// Initialize layout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		mDrawerFrame = (RelativeLayout) findViewById(R.id.drawer);
		mChannelsListView = (ListView) findViewById(R.id.channels);
		mSendMessageView = findViewById(R.id.send_button);
		mInputMessageView = (TextView) findViewById(R.id.input_textview);
		mUserNameView = (TextView) findViewById(R.id.user_name);
		mWifiEnabled = findViewById(R.id.wifi_enabled);
		mWifiDisabled = findViewById(R.id.wifi_disabled);

		mScreenTypeView = (TextView) findViewById(R.id.screen_type);
		mInputContainer = (RelativeLayout) findViewById(R.id.input_container);
		
		// Disable send button.
		mSendMessageView.setEnabled(false);

		mFragments = Maps.newHashMap();
		mFragmentManager = getFragmentManager();
		
		spinnerUsernames = (Spinner) findViewById(R.id.spinner_usernames);

        listUsernames = new ArrayList<String>();
        listUsernames.add("Public"); 
        
        dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_list_item_1, listUsernames);
                      
        spinnerUsernames.setAdapter(dataAdapter);
        
		map = new HashMap<String, String>();
		
		// Initialize Chord
		Schord chord = new Schord();
		
		try {
			chord.initialize(this);
		} catch (SsdkUnsupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initUserName();
		initDrawer();
		restoreSavedState(savedInstanceState);
		initChannelsList();
		initChatChord();
		initPublicChannel();
		
		mScreenTypeView.setText("Screen Type: " + SplashActivity.screenType);
	}

	private void restoreSavedState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			mChannels = Lists.newArrayList();
		} else {
			mChannels = savedInstanceState.getStringArrayList("channels");
			mCurrentChannelName = savedInstanceState.getString("chat_fragment_name");
			mFragments.put(mCurrentChannelName, (ChatFragment) mFragmentManager.findFragmentByTag(CHAT_FRAGMENT_TAG));
		}
	}

	/**
	 * initializes ChatChord and joins the default channel
	 */
	private void initChatChord() {
		mChatChord = new ChatChord(this) {

			@Override
			public void onChordStarted(String userNodeName, int reason) {
				currUserNodeName = userNodeName;
				mSendMessageView.setEnabled(true);
				for (ChatFragment fragment : mFragments.values()) {
					fragment.joinChannel();
				}
			}

			@Override
			public void onChordStartFailed(int reason) {
				leaveChannels();
			}

			@Override
			public void onChordError(int error) {
				leaveChannels();
			}

			@Override
			public void onChordDisconnected() {
				leaveChannels();
			}
			
			private void leaveChannels() {
				for (ChatFragment fragment : mFragments.values()) {
					fragment.leaveChannel();
				}
			}

		};
	}

	/**
	 * creates the adapter for the list of channels in the navigation drawer
	 */
	private void initChannelsList() {
		mChannelsAdapter = new ArrayAdapter<String>(this, R.layout.channel_list_view_item, R.id.channel_name, mChannels) {

			class Tag implements OnClickListener {

				private final View mDeleteView;
				int mPosition;

				public Tag(View view) {
					mDeleteView = view.findViewById(R.id.remove_channel);
					mDeleteView.setOnClickListener(this);
				}

				@Override
				public void onClick(View v) {
					// mDeleteView is clicked
					final ChatFragment fragment = mFragments.remove(mChannels.remove(mPosition));
					fragment.leaveChannel();
					notifyDataSetChanged();
					changeChatFragment(0, false);
				}

			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				Tag tag = (Tag) view.getTag();
				if (tag == null) {
					view.setTag(tag = new Tag(view));
				}

				if (position == 0) {
					tag.mDeleteView.setVisibility(View.INVISIBLE);
				} else {
					tag.mDeleteView.setVisibility(View.VISIBLE);
				}

				tag.mPosition = position;
				return view;
			}

		};

		mChannelsListView.setAdapter(mChannelsAdapter);
		mChannelsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				changeChatFragment(position, true);
			}

		});
	}

	private void initDrawer() {
		final ActionBar actionBar = getActionBar();
		final LayoutInflater inflater = LayoutInflater.from(this);
		final TextView customActionBarTitle = (TextView) inflater.inflate(R.layout.action_bar_roboto, null);
		customActionBarTitle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isDrawerOpen()) {
					closeDrawer();
				} else {
					openDrawer();
				}
			}

		});

		actionBar.setCustomView(customActionBarTitle);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		if (mUserName.isEmpty()) {
			openDrawer();
		} else {
			customActionBarTitle.setText(PUBLIC_CHANNEL_NAME);
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				customActionBarTitle.setText(mCurrentChannelName);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				customActionBarTitle.setText(getString(R.string.chord_chat_action_bar));
				invalidateOptionsMenu();
				hideKeyboard();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * stores the username into the sharedpreference
	 */
	private void initUserName() {
		final SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.CHAT_PREFERENCES,
				Context.MODE_PRIVATE);
		mUserName = sharedPreferences.getString(MainActivity.USER_NAME_KEY, "");
		mUserNameView.append(mUserName);
		mUserNameView.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					final String userName = ((TextView) v).getText().toString().trim();
					if (!userName.equalsIgnoreCase(mUserName)) {
						mUserName = userName;
						final SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putString(MainActivity.USER_NAME_KEY, mUserName);
						editor.apply();
					}
				}
			}

		});
	}

	private void initPublicChannel() {
		if (!mChannels.contains(PUBLIC_CHANNEL_NAME)) {
			final ChatFragment fragment = ChatFragment.newInstance(PUBLIC_CHANNEL_NAME);
			mFragments.put(PUBLIC_CHANNEL_NAME, fragment);
			mChannels.add(PUBLIC_CHANNEL_NAME);
			mCurrentChannelName = PUBLIC_CHANNEL_NAME;

			final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.content, fragment, CHAT_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mBroadcastReceiver);
		mChatChord.stopChord();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mBroadcastReceiver, INTENT_FILTER);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("chat_fragment_name", mCurrentChannelName);
		outState.putStringArrayList("channels", (ArrayList<String>) mChannels);
		super.onSaveInstanceState(outState);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_button:
			final String text = mInputMessageView.getText().toString();
			
			// Initialize receiver as Public
			String sendTo = "Public";

			for(String username : MainActivity.listUsernames) {
				if(username != "Public") {
					String[] parts = username.split(":");
					String nodeName = parts[0];
					String aliasName = parts[1];
					if(spinnerUsernames.getSelectedItem().toString().contains(nodeName)) {
						sendTo = nodeName;
						break;
					}
				}	
			}
			
			
			// Check if there is a message
			if (!text.isEmpty()) {
				mInputMessageView.setText("");
				ChordMessage message = ChordMessage.obtain(text, mUserName, MessageOwner.YOU, sendTo);
				mFragments.get(mCurrentChannelName).addMessage(message, sendTo);
			}
			break;
		default:
			throw new IllegalArgumentException(Integer.toString(v.getId()));
		}
	}

	private void changeChatFragment(int index, boolean closeDrawer) {
		if (!mCurrentChannelName.equalsIgnoreCase(mChannels.get(index))) {
			final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
			mCurrentChannelName = mChannels.get(index);

			final ChatFragment fragment;
			if (mFragments.containsKey(mCurrentChannelName)) {
				fragment = mFragments.get(mCurrentChannelName);
			} else {
				fragment = ChatFragment.newInstance(mCurrentChannelName);
				mFragments.put(mCurrentChannelName, fragment);
			}

			fragmentTransaction.replace(R.id.content, fragment, CHAT_FRAGMENT_TAG);
			fragmentTransaction.commit();

		}

		if (closeDrawer) {
			closeDrawer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Called whenever the invalidateOptionsMenu() is called.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.plus).setVisible(isDrawerOpen());
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.plus:
			DialogFragment dialog = new ChannelNameDialog();
			dialog.show(getFragmentManager(), null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private boolean isDrawerOpen() {
		return mDrawerLayout.isDrawerOpen(mDrawerFrame);
	}

	private void openDrawer() {
		mDrawerLayout.openDrawer(mDrawerFrame);
	}

	private void closeDrawer() {
		mDrawerLayout.closeDrawers();
	}

	@Override
	public void onAddChannel(String channelName) {
		for (String channel : mChannels) {
			if (channel.equalsIgnoreCase(channelName)) {
				// Do not duplicate the channels.
				return;
			}
		}
		mChannels.add(channelName);
		mChannelsAdapter.notifyDataSetChanged();
		hideKeyboard();
	}

	private void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	public ChatChord getChatChord() {
		return mChatChord;
	}

}
