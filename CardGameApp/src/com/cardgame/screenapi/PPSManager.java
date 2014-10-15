package com.cardgame.screenapi;

import android.content.Context;
import android.util.Log;

import com.cardgame.screenapi.chordimpl.ChordEventManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordTransportInterface;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.network.NetworkManager;
import com.cardgame.screenapi.session.SessionManager;

public class PpsManager {

	public static final boolean PRIVATE = true;
	public static final boolean PUBLIC = false;
	public static final boolean SESSION_MODE = true;
	public static final boolean GAME_MODE = false;

	private boolean isPrivate;
	private EventManager eventManager;
	private NetworkManager networkInitializer; 
	private SessionManager sessionManager;
	private static Context mContext;
	
	//singleton attributes
	private static PpsManagerFactory factory;
	
	//temporary public
	private static PpsManager instance=null;
	
	public PpsManager(Context mContext, boolean isPersonal, boolean sessionMode) {
		PpsManager.mContext = mContext;
		initializeNetworkManager();
		initializeEventManager();
		isPrivate = isPersonal;
		SessionManager.getInstance().setSessionMode(sessionMode);
		instance = this;
	}

	public PpsManager() {
		initializeNetworkManager();
		initializeEventManager();
		clearSessionList();
		
		instance = this;
	}
	  
	public static PpsManager getInstance() {
		if (instance==null){
			Log.e("PPSManager is null", "getInstance PPS");
			instance= factory.createPPSManager();
		}
		return instance;
	}	
	  
	public void clearSessionList(){
		 SessionManager.getInstance().clearPrivateScreenList();
		 SessionManager.getInstance().clearPublicScreenList();
		 SessionManager.getInstance().clearAliasList();
	}
	  
	public void initializeEventManager(){
		 EventManager.setDefaultFactory(new ChordEventManagerFactory());
		 setEventManager();
	}
	  
	public void initializeNetworkManager(){
		 NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		 setNetworkInitializer();
	}
	  
	public void setContext(Context context){
	  	 PpsManager.mContext = context;
	}
	  
	public void setScreenMode(boolean sessionMode){
		 clearSessionList();
	  	 SessionManager.getInstance().setSessionMode(sessionMode);
	}
	  
	public void stop() {
		ChordNetworkManager.getChordManager().stop();
	}
	
	public void setScreenType(boolean screenType) {
		isPrivate = screenType;
	}
	
	public void start() {
		ChordNetworkManager.initializeChordManager();
	}
	
	public boolean isPersonal() {
		return isPrivate;
	}
	
	public String getCurrentSessionName() {
		return ChordTransportInterface.mChannel.getName();
	}
	
	public String getDeviceName() {
		return ChordNetworkManager.getChordManager().getName();
	}
	
	/**
	 * Select which implementation you want (Chord in this case)
	 */
	public void setNetworkInitializer() {
		this.networkInitializer = NetworkManager.getInstance();
	}
	
	public static Context getContext() {
		return mContext;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager() {
		this.eventManager = EventManager.getInstance();
	}

	public NetworkManager getNetworkInitializer() {
		return networkInitializer;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
