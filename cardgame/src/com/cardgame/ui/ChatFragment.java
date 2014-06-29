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
package com.cardgame.ui;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cardgame.chord.ChatChord;
import com.cardgame.chord.IChordChannelListenerAdapter;
import com.cardgame.collection.FixedArrayList;
import com.cardgame.model.Message;
import com.cardgame.model.Message.MessageOwner;
import com.samsung.android.sdk.chord.SchordChannel;
import com.srpol.chordchat.R;

public class ChatFragment extends Fragment {

	private static final int MESSAGE_HISTORY_SIZE = 20;
	private static final String PAYLOAD_TYPE = "CHORD_CHAT";
	private static final String CHANNEL_NAME_KEY = "channel_name";
	private static final String MESSAGES_KEY = "messages";

	private MessageAdapter mMessagesAdapter;
	private ChatChord mChatChord;
	private SchordChannel mChannel;

	private final SchordChannel.StatusListener mChordChannelListener = new IChordChannelListenerAdapter() {
	
		@Override
		public void onDataReceived(String arg0, String arg1, String arg2,
				byte[][] arg3) {
			if (PAYLOAD_TYPE.equals(arg2)) {
				final Message receivedMessage = Message.obtainChatMessage(arg3[0]);
				handleMessage(receivedMessage);
			}
			
			// adds the nodename + alias username of the node that joined to the spinner
			if ("user_details".equals(arg2)) {
				boolean exist = false;
				for(String username : MainActivity.listUsernames) {
					if(username.equals(new String(arg3[0])))
						exist = true;
				}
				if(!exist) {
					MainActivity.listUsernames.add(new String(arg3[0]));
					MainActivity.dataAdapter.notifyDataSetChanged();
				}
			}
		}
		
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			if(SplashActivity.screenType == "Private")
				sendDetailsMessage(MainActivity.currUserNodeName+ ":" + MainActivity.mUserNameView.getText().toString());
			else
				MainActivity.mInputContainer.setVisibility(View.GONE);
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			for(String username : MainActivity.listUsernames) {
				if(username.contains(fromNode)) {
					MainActivity.listUsernames.remove(username);
		        	MainActivity.dataAdapter.notifyDataSetChanged();
				}	
			}
		}
		
	};
	
	// Empty constructor required by Android.
	public ChatFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mChatChord = ((MainActivity) getActivity()).getChatChord();
		joinChannel();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mMessagesAdapter == null) {
			mMessagesAdapter = new MessageAdapter(getActivity(), MESSAGE_HISTORY_SIZE);
		}

		// Restore the saved messages.
		if (savedInstanceState != null) {
			mMessagesAdapter.addMessages((List<Message>) savedInstanceState.getSerializable(MESSAGES_KEY));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Save the messages.
		outState.putSerializable(MESSAGES_KEY, (Serializable) mMessagesAdapter.mMessages);
		super.onSaveInstanceState(outState);
	}

	/**
	 * Creates new {@link ChatFragment} instance. TODO
	 * 
	 * @param channelName
	 *            name of the channel
	 * @return {@link ChatFragment} instance
	 */
	public static ChatFragment newInstance(String channelName) {
		final ChatFragment chatFragment = new ChatFragment();
		final Bundle bundle = new Bundle(1);
		bundle.putString(CHANNEL_NAME_KEY, channelName);
		chatFragment.setArguments(bundle);
		return chatFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ListView listView = (ListView) inflater.inflate(R.layout.chat_fragment, container, false);
		listView.setAdapter(mMessagesAdapter);
		return listView;
	}

	/**
	 * Adds the given message to the chat window.
	 * 
	 * @param message
	 *            message to add
	 */
	public void addMessage(Message message, String sendTo) {
		mMessagesAdapter.addMessage(message);
		
		Message chatMesssageToSend = Message.obtain(message);
		chatMesssageToSend.changeOwner();

		if(sendTo.equals("Public"))
			sendMessage(chatMesssageToSend);
		else
			sendPrivateMessage(chatMesssageToSend, sendTo);
	}

	/**
	 * Joins the channel.
	 */
	public void joinChannel() {
		try {
			mChannel = mChatChord.joinChannel(getChannelName(), mChordChannelListener);
			Log.e("ChatFragment(joinChannel function)", "successful");
		}catch(Exception e) {
			Log.e("ChatFragment(joinChannel function)", "not successful: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Leaves the channel.
	 */
	public void leaveChannel() {
		if (mChannel != null) {
			mChatChord.leaveChannel(mChannel.getName());
		}
	}

	/**
	 * Returns the name of the channel to which this fragment is connected.
	 * 
	 * @return channel name
	 */
	public String getChannelName() {
		return getArguments().getString(CHANNEL_NAME_KEY).toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Sends message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 */
	private void sendMessage(Message message) {
		mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] { message.getBytes() });
	}
	
	
	/**
	 * Sends private message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 * @param userToSend
	 * 			  nodename of the user which the message will be sent
	 */
	private void sendPrivateMessage(Message message, String userToSend) {
		mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  message.getBytes() });
	}
	
	/**
	 * Sends the current device's node name and alias user name over the channel.
	 * 
	 * @param message
	 *            includes the node name + alias user name
	 */
	public void sendDetailsMessage(String message) {
		byte[][] payload = new byte[1][];
        payload[0] = message.getBytes();
		mChannel.sendDataToAll("user_details", payload);
	}

	/**
	 * Handles messages received on the channel.
	 * 
	 * @param message
	 *            message to be handled
	 */
	private void handleMessage(Message message) {
		mMessagesAdapter.addMessage(message);
	}

	/**
	 * Adapter for the list view. It holds the chat messages.
	 */
	private static class MessageAdapter extends BaseAdapter {

		private final List<Message> mMessages;
		private final Context mContext;

		MessageAdapter(Context context, int elementsLimit) {
			super();
			mContext = context;
			mMessages = FixedArrayList.newFixedArrayList(elementsLimit);
		}

		/**
		 * Adds chat message at the end. If the {@link ChatFragment#MESSAGE_HISTORY_SIZE} limit has been reached removes
		 * first element.
		 * 
		 * @param message
		 *            {@link Message}
		 */
		void addMessage(Message message) {
			mMessages.add(message);
			notifyDataSetChanged();
		}

		void addMessages(List<Message> messages) {
			for (Message message : messages) {
				mMessages.add(message);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mMessages.size();
		}

		@Override
		public Message getItem(int position) {
			return mMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Message message = getItem(position);
			
			//int layoutId = message.getOwner() == MessageOwner.YOU ? R.layout.message_view
			//		: R.layout.stranger_message_view;
			int layoutId = R.layout.message_view;
			convertView = View.inflate(mContext, layoutId, null);

			((TextView) convertView.findViewById(R.id.message_view_message)).setText(message.getMessage());
			
			if (message.getOwner() == MessageOwner.YOU && message.getMessageType().equals("Public")) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
				.setText("[Public]");
			}
			else if (message.getOwner() == MessageOwner.STRANGER && message.getMessageType().equals("Public")) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
				.setText("[Public] \r\n" + message.getUserName() + ":");
			}
			else if (message.getOwner() == MessageOwner.YOU && !message.getMessageType().equals("Public")) {
				for(String username : MainActivity.listUsernames) {
					if(username != "Public") {
						String[] parts = username.split(":");
						String nodeName = parts[0];
						String aliasName = parts[1];
						if(message.getMessageType().equals(nodeName)) {
							((TextView) convertView.findViewById(R.id.message_view_sender_name))
							.setText("[Private] \r\n to:" + aliasName);
							break;
						}
					}	
				}
			}
			else if (message.getOwner() == MessageOwner.STRANGER) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
					.setText("[Private] \r\n from:" + message.getUserName());
			}
			return convertView;
		}
	}

}
