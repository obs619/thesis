package com.cardgame.screenapi;

import android.content.Context;

import com.cardgame.screenapi.chordimpl.ChordEventManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordTransportInterface;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.network.NetworkManager;
import com.cardgame.screenapi.session.SessionManager;

public class PPSManager {

	private EventManager eventManager;
	private NetworkManager networkInitializer; 
	private SessionManager sessionManager;
	private static Context mContext;
	
	public PPSManager(Context mContext, boolean isPersonal, boolean sessionMode) {
		PPSManager.mContext = mContext;

		NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		EventManager.setDefaultFactory(new ChordEventManagerFactory());
		
		SessionManager.getInstance().setScreenType(isPersonal);
		SessionManager.getInstance().clearPrivateScreenList();
		SessionManager.getInstance().clearPublicScreenList();
		SessionManager.getInstance().clearAliasList();
		
		SessionManager.getInstance().setSessionMode(sessionMode);
		
		setNetworkInitializer();
		setEventManager();
	}
	
	public void stop() {
		ChordNetworkManager.getChordManager().stop();
	}
	
	public void start() {
		ChordNetworkManager.initializeChordManager();
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
