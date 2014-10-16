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

	public Schord chord;
	public static SchordManager mChordManager;
	
	public ChordNetworkManager() {
		initializeChord();

		mChordManager = new SchordManager(PpsManager.getContext());
		initializeChordManager();
	}
	
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
			// TODO Auto-generated method stub
			Log.e("Schord Status Listener", "Chord Stop");
		}

	};
	
	public void initializeChord() {
		// Initialize Chord
		chord = new Schord();
		
		try {
			chord.initialize(PpsManager.getContext());
		} catch (SsdkUnsupportedException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeChordManager() {

		List<Integer> infList =mChordManager.getAvailableInterfaceTypes();
		
		try {
			mChordManager.start(infList.get(0), mChordManagerListener);
			Log.e("Starting Chord", "Success");
		}catch(Exception e){
			Log.e("Starting Chord", "Fail");
			e.printStackTrace();
		}


	}
	
	public static SchordManager getChordManager() {
		return mChordManager;
	}
	
}
