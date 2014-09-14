package com.cardgame.screenapi;

import com.cardgame.screenapi.chordimpl.ChordEventManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordNetworkManagerFactory;

import android.content.Context;
import android.util.Log;



public class PPSManager {

	private EventManager eventManager;
	private NetworkManager networkInitializer; 
	private SessionManager sessionManager;
	private static Context mContext;
	
	public PPSManager(Context mContext, boolean isPersonal) {
		PPSManager.mContext = mContext;

		NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		EventManager.setDefaultFactory(new ChordEventManagerFactory());
		
		//SessionManager.getInstance().setReady(true);
		SessionManager.getInstance().setScreenType(isPersonal);
		SessionManager.getInstance().clearPrivateScreenList();
		SessionManager.getInstance().clearPublicScreenList();
		
		setNetworkInitializer();
		setEventManager();
		
	}
	
	public PPSManager(Context mContext, boolean isPersonal, boolean sessionMode) {
		PPSManager.mContext = mContext;

		NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		EventManager.setDefaultFactory(new ChordEventManagerFactory());
		
		//SessionManager.getInstance().setReady(true);
		SessionManager.getInstance().setScreenType(isPersonal);
		SessionManager.getInstance().clearPrivateScreenList();
		SessionManager.getInstance().clearPublicScreenList();
		
		SessionManager.getInstance().setSessionMode(sessionMode);
		
		setNetworkInitializer();
		setEventManager();
		
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
