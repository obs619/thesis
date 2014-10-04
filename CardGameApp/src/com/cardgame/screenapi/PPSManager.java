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

	public static final boolean PRIVATE = true;
	public static final boolean PUBLIC = false;
	public static final boolean AS_DEFAULT = true;
	public static final boolean AS_CUSTOM = false;

	private EventManager eventManager;
	private NetworkManager networkInitializer; 
	private SessionManager sessionManager;
	private static Context mContext;
	
	//singleton attributes
	private static PPSManagerFactory factory;
	private static PPSManager instance=null;
	
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
	/* begin code required for singleton
	 */
	 public PPSManager()
	  {
	  	NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		EventManager.setDefaultFactory(new ChordEventManagerFactory());

		setNetworkInitializer();
		setEventManager();
	  }
	  
	  public static PPSManager getInstance() {
		if (instance==null)
			instance= factory.createPPSManager();
		return instance;
		}
	  public void setContext(Context context){
	  	PPSManager.mContext = context;
	  }
	  
	  public void setPersonal(boolean isPersonal){
	  	SessionManager.getInstance().setScreenType(isPersonal);
	  	SessionManager.getInstance().clearPrivateScreenList();
		SessionManager.getInstance().clearPublicScreenList();
		SessionManager.getInstance().clearAliasList();
	  }
	  
	  public void setSessionMode(boolean sessionMode){
	  	SessionManager.getInstance().setSessionMode(sessionMode);
	  }
	  
	  
	  
	  /*
	   * end code required for singleton
	 */
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
