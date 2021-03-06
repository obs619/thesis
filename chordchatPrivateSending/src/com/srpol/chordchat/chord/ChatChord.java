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
package com.srpol.chordchat.chord;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.samsung.android.sdk.chord.SchordChannel;
import com.samsung.android.sdk.chord.SchordManager;

/**
 * Contains general methods related to the communication over Chord. Intitializes {@link ChordManager} and handles
 * messages passed over Chord channels.
 */
public abstract class ChatChord {

	private final SchordManager mChordManager;
	public static int result = -1;
	public static int interfaceType = -1; 
	
	private final SchordManager.StatusListener mChordManagerListener = new SchordManager.StatusListener() {
		
		@Override
		public void onStarted(String arg0, int arg1) {
			onChordStarted(arg0, arg1);
			
		}

		@Override
		public void onStopped(int arg0) {
			// TODO Auto-generated method stub
			
		}

	};

	public ChatChord(Context context) {
		mChordManager = new SchordManager(context);
	}

	public void stopChord() {
		mChordManager.stop();
	}

	/**
	 * ChordManager checks the available interface types which are wifi, mobile ap or wifi p2p
	 * interfaceType gets the first available interface
	 */
	public void startChord() {
		
		List<Integer> infList =mChordManager.getAvailableInterfaceTypes();
        
		try {
			interfaceType = infList.get(0);
			mChordManager.start(interfaceType, mChordManagerListener);
			result = 0;
			Log.e("start chord","succes");
		}catch(Exception e){
			interfaceType = -1;
			result = -1;
			Log.e("start chord","fail");
		}
	}

	/**
	 * Joins to the channel with the specified name.
	 * 
	 * @param channelName
	 * @return 
	 */
	public SchordChannel joinChannel(String channelName, SchordChannel.StatusListener listener) {
		 return mChordManager.joinChannel(channelName, listener);
	}


	public void leaveChannel(String channelName) {
		mChordManager.leaveChannel(channelName);
	}

	abstract public void onChordDisconnected();

	abstract public void onChordError(int error);

	abstract public void onChordStartFailed(int reason);

	abstract public void onChordStarted(String userNodeName, int reason);

	final String getNodeName() {
		return mChordManager.getName();
	}

}
