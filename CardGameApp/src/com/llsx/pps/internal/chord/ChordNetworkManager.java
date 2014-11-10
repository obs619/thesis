package com.llsx.pps.internal.chord;

import java.util.List;

import android.util.Log;

import com.llsx.pps.PpsManager;
import com.llsx.pps.network.NetworkManager;
import com.llsx.pps.session.SessionManager;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.chord.Schord;
import com.samsung.android.sdk.chord.SchordManager;

public class ChordNetworkManager extends NetworkManager {

	/**Chord class required to use Chord API
	 * 
	 */
	public Schord chord;
	/**
	 * Chord manager provided by Chord API
	 */
	public static SchordManager mChordManager;
	
	/**
	 * Initialises Chord and the SchordManager
	 */
	public ChordNetworkManager() {
		initializeChord();

		mChordManager = new SchordManager(PpsManager.getContext());
		initializeChordManager();
	}
	/**
	 * Initialises Chord and checks whether the device supports Chord
	 */
	public void initializeChord() {
		// Initialize Chord
		chord = new Schord();
		
		try {
			chord.initialize(PpsManager.getContext());
		} catch (SsdkUnsupportedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialises the SchordManager on the first available interface type
	 */
	public static void initializeChordManager() {
		List<Integer> infList = mChordManager.getAvailableInterfaceTypes();
		
		try {
			mChordManager.start(infList.get(0), mChordManagerListener);
			Log.e("Starting Chord", "Successfully connected to the network");
		}catch(Exception e){
			Log.e("Starting Chord", "Unable to connect to network or already has a connected network");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return the current SchordManager
	 */
	public static SchordManager getChordManager() {
		return mChordManager;
	}

	/**
	 * Listener for the SchordManager
	 */
	private final static SchordManager.StatusListener mChordManagerListener = new SchordManager.StatusListener() {
		
		@Override
		public void onStarted(String nodeName, int reason) {
			Log.e("Schord Status Listener", "Chord start");
			//check if the device has a session

			ChordTransportInterface.joinDefaultChannel();
			if(!SessionManager.getInstance().isSessionMode())
				ChordTransportInterface.joinCustomChannel();
		}

		@Override
		public void onStopped(int reason) {
			Log.e("Schord Status Listener", "Chord Stop");
		}

	};
	
}
