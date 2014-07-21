package com.cardgame.screenapi.chordimpl;

import java.util.List;

import android.util.Log;

import com.cardgame.screenapi.NetworkManager;
import com.cardgame.screenapi.PPSManager;
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
	public SchordChannel channel;
	
	
	public ChordNetworkManager() {
		initializeChord();
		initializeChordManager();
	}
	
	private final SchordManager.StatusListener mChordManagerListener = new SchordManager.StatusListener() {
		
		@Override
		public void onStarted(String nodeName, int reason) {
			// TODO Auto-generated method stub
			
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
	
	public void initializeChordManager() {
		// Initialize SchordManager
		mChordManager = new SchordManager(PPSManager.getContext());
		
		//checks the available interface types which are wifi, mobile ap or wifi p2p
		List<Integer> infList =mChordManager.getAvailableInterfaceTypes();
		Log.e("inflist", infList.get(0) + "");
		//starts the chordmanager with first detected available interface type
		try {
			mChordManager.start(infList.get(0), mChordManagerListener);
		}catch(Exception e){
			Log.e("FAIL START CORD", "fail start cord");
			//e.printStackTrace();
		}
	}
	
	public static SchordManager getChordManager() {
		return mChordManager;
	}
	
}
