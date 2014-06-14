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
package com.srpol.chordchat.ui;

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

import com.samsung.android.sdk.chord.SchordChannel;
import com.srpol.chordchat.R;
import com.srpol.chordchat.chord.ChatChord;
import com.srpol.chordchat.chord.IChordChannelListenerAdapter;
import com.srpol.chordchat.collection.FixedArrayList;
import com.srpol.chordchat.model.ChatMessage;
import com.srpol.chordchat.model.ChatMessage.MessageOwner;

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
				final ChatMessage receivedMessage = ChatMessage.obtainChatMessage(arg3[0]);
				handleMessage(receivedMessage);
			}
		}
		
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			onNodeCallbackCommon(true, ChatChord.interfaceType, fromNode);
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			onNodeCallbackCommon(false, ChatChord.interfaceType, fromNode);
		};
		
	};

	private void onNodeCallbackCommon(boolean isJoin, int interfaceType, String fromNode) {
        if (isJoin) {
            if (MainActivity.map.containsKey(fromNode)) {
            	Log.e("ChatFragment", "already added node:" + fromNode);
            } else {

				MainActivity.map.put(fromNode, fromNode);
				
				boolean exist = false;
				for(String username : MainActivity.listUsernames) {
					if(username.equals(fromNode))
						exist = true;
				}
				if(!exist) {
					MainActivity.listUsernames.add(fromNode);
					MainActivity.dataAdapter.notifyDataSetChanged();
				}
            }
        } else {
        	MainActivity.listUsernames.remove(fromNode);
        	MainActivity.dataAdapter.notifyDataSetChanged();
        	MainActivity.map.remove(fromNode);
        }

    }
	
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
			mMessagesAdapter.addMessages((List<ChatMessage>) savedInstanceState.getSerializable(MESSAGES_KEY));
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
	public void addMessage(ChatMessage message, String sendTo) {
		
		if(sendTo.equals("Public") || sendTo.equals(MainActivity.currUserNodeName) || message.getOwner() == MessageOwner.YOU)
			mMessagesAdapter.addMessage(message);
		
		ChatMessage chatMesssageToSend = ChatMessage.obtain(message);
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
	private void sendMessage(ChatMessage message) {
		mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] { message.getBytes() });
	}
	
	private void sendPrivateMessage(ChatMessage message, String userToSend) {
		mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  message.getBytes() });
	}

	/**
	 * Handles messages received on the channel.
	 * 
	 * @param message
	 *            message to be handled
	 */
	private void handleMessage(ChatMessage message) {
		mMessagesAdapter.addMessage(message);
	}

	/**
	 * Adapter for the list view. It holds the chat messages.
	 */
	private static class MessageAdapter extends BaseAdapter {

		private final List<ChatMessage> mMessages;
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
		 *            {@link ChatMessage}
		 */
		void addMessage(ChatMessage message) {
			mMessages.add(message);
			notifyDataSetChanged();
		}

		void addMessages(List<ChatMessage> messages) {
			for (ChatMessage message : messages) {
				mMessages.add(message);
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mMessages.size();
		}

		@Override
		public ChatMessage getItem(int position) {
			return mMessages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ChatMessage message = getItem(position);
			
			//int layoutId = message.getOwner() == MessageOwner.YOU ? R.layout.message_view
			//		: R.layout.stranger_message_view;
			int layoutId = R.layout.message_view;
			convertView = View.inflate(mContext, layoutId, null);

			((TextView) convertView.findViewById(R.id.message_view_message)).setText(message.getMessage());
			
			Log.e("MessageType", message.getMessageType()+ message.getUserName());
			
			if (message.getOwner() == MessageOwner.YOU && message.getMessageType().equals("Public")) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
				.setText("[Public]");
			}
			else if (message.getOwner() == MessageOwner.STRANGER && message.getMessageType().equals("Public")) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
				.setText("[Public] \r\n" + message.getUserName() + ":");
			}
			else if (message.getOwner() == MessageOwner.YOU && !message.getMessageType().equals("Public")) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
				.setText("[Private] \r\n to:" + message.getMessageType());
			}
			else if (message.getOwner() == MessageOwner.STRANGER) {
				((TextView) convertView.findViewById(R.id.message_view_sender_name))
					.setText("[Private] \r\n from:" + message.getUserName());
			}
			return convertView;
		}
	}

}
