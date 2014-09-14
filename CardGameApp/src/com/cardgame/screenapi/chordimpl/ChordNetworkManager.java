package com.cardgame.screenapi.chordimpl;

import java.util.List;

import android.util.Log;

import com.cardgame.screenapi.NetworkManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.chord.Schord;
import com.samsung.android.sdk.chord.SchordChannel;
import com.samsung.android.sdk.chord.SchordManager;
/**
 * handles chord initialization, connection/disconnection, discovery, etc.
 * @author Andrew
 *
 */
public class ChordNetworkManager extends NetworkManager {

	public Schord chord;
	public static SchordManager mChordManager;
	
	public ChordNetworkManager() {
		initializeChord();

		mChordManager = new SchordManager(PPSManager.getContext());
		initializeChordManager();
	}
	
	private final static SchordManager.StatusListener mChordManagerListener = new SchordManager.StatusListener() {
		
		@Override
		public void onStarted(String nodeName, int reason) {
			if(SessionManager.getInstance().isSessionMode())
				ChordTransportInterface.joinDefaultChannel();
			else if(!SessionManager.getInstance().isSessionMode())
				ChordTransportInterface.joinCustomChannel();
		}

		@Override
		public void onStopped(int reason) {
			// TODO Auto-generated method stub
			
		}

	};
	
	public void initializeChord() {
		// Initialize Chord
		chord = new Schord();
		
		try {
			chord.initialize(PPSManager.getContext());
		} catch (SsdkUnsupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initializeChordManager() {
		// Initialize SchordManager
		
		//checks the available interface types which are wifi, mobile ap or wifi p2p
		List<Integer> infList =mChordManager.getAvailableInterfaceTypes();
		//starts the chordmanager with first detected available interface type
		try {
			mChordManager.start(infList.get(0), mChordManagerListener);
			Log.e("START CORD", "start cord");
		}catch(Exception e){
			Log.e("FAIL START CORD", "fail start cord");
			e.printStackTrace();
		}
	}
	
	public static SchordManager getChordManager() {
		return mChordManager;
	}
	
}
