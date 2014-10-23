package com.llsx.pps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.llsx.pps.event.EventManager;
import com.llsx.pps.internal.chord.ChordEventManagerFactory;
import com.llsx.pps.internal.chord.ChordNetworkManager;
import com.llsx.pps.internal.chord.ChordNetworkManagerFactory;
import com.llsx.pps.internal.chord.ChordTransportInterface;
import com.llsx.pps.network.NetworkManager;
import com.llsx.pps.session.SessionManager;

public class PpsManager {

	/* Constants */
	public static final boolean PRIVATE = true;
	public static final boolean PUBLIC = false;
	public static final boolean SESSION_MODE = true;
	public static final boolean APP_MODE = false;

	/* Public and Private Screens Tracking */
	private boolean isPrivate;
	private List<String> publicScreenList = new ArrayList<String>();//<name, sessionID>
	private List<String> privateScreenList = new ArrayList<String>();
	
	/* Network / Connection Management */
	private EventManager eventManager;
	private NetworkManager networkManager; 
	private SessionManager sessionManager;
	private static Context mContext;
	
	/* PpsManager */
	private static PpsManagerFactory factory; // singleton attribute
	private static PpsManager instance = null;

	/*
	 * Constructors
	 */
	
	/**
	 * Constructs a new PpsManager with default settings
	 */
	public PpsManager() {
		initializeNetworkManager();
		initializeEventManager();
		clearSessionList();
		
		instance = this;
	}
	
	/**
	 * Constructs a new PpsManager for the given context, screen type and session mode
	 * @param mContext current context of the device
	 * @param isPrivate screen type of the device
	 * @param sessionMode current session of the device
	 */
	public PpsManager(Context mContext, boolean isPrivate, boolean sessionMode) {
		PpsManager.mContext = mContext;
		initializeNetworkManager();
		initializeEventManager();
		this.isPrivate = isPrivate;
		SessionManager.getInstance().setSessionMode(sessionMode);
		
		instance = this;
	}
	
	/*
	 * Methods
	 */
	
	/**
	 * @return The current instance of PpsManager.
	 */
	public static PpsManager getInstance() {
		if (instance == null) {
			Log.e("PPSManager is null", "getInstance PPS");
			instance = factory.createPpsManager();
		}
		return instance;
	}
	
	/**
	 * Starts the network manager and connects
	 * the app to the network.
	 */
	public void start() {
		ChordNetworkManager.initializeChordManager();
	}
	
	/**
	 * Stops the network manager and disconnects
	 * the app from the network.
	 */
	public void stop() {
		ChordNetworkManager.getChordManager().stop();
	}
	
	/*
	 * Public Screen Management
	 */
	
	/**
	 * Adds the device into the list of public screens.
	 * @param deviceId unique identifier (ID) of the device
	 * @param deviceName name representation for the device
	 * (e.g. "Gameboard1")
	 */
	public void addPublicScreen(String deviceId, String deviceName) {
		publicScreenList.add(deviceId);
		SessionManager.getInstance().addDevice(deviceId, deviceName);
	}
	
	/**
	 * Remove the given device from the list of
	 * public screens.
	 * @param deviceId unique identifier (ID)
	 * of the device to be removed
	 */
	public void removeFromPublicScreen(String deviceId) {
		for(int i = 0; i < publicScreenList.size(); i++)
			if(publicScreenList.get(i).equals(deviceId))
				publicScreenList.remove(i);
	}
	
	/**
	 * @return The list of name representations
	 * (e.g. "Gameboard1") for the public screen devices.
	 */ //maybe remove
	public List<String> getPublicScreenNameList() {
		List<String> result = new ArrayList<String>();
		for(String key: publicScreenList) {
			String alias = SessionManager.getInstance().getDeviceName(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/*
	 * Private Screen Management
	 */
	
	/**
	 * Adds the device into the list of private screens.
	 * @param deviceId unique identifier (ID) of the device
	 * @param deviceName name representation of the device
	 * (e.g. "Player1")
	 */
	public void addPrivateScreen(String deviceId, String deviceName) {
		privateScreenList.add(deviceId);
		SessionManager.getInstance().addDevice(deviceId, deviceName);
	}
	
	/**
	 * Remove the given device from the list of private screens.
	 * @param deviceId unique identifier (ID) of the device
	 * to be removed
	 */
	public void removeFromPrivateScreen(String deviceId) {
		for(int i = 0; i < privateScreenList.size(); i++)
			if(privateScreenList.get(i).equals(deviceId))
				privateScreenList.remove(i);
	}
	
	
	/**
	 * @return The list of name representations
	 * (e.g. "Player 1") for the private screen devices.
	 */ //maybe remove
	public List<String>getPrivateScreenNameList() {
		List<String> result = new ArrayList<String>();
		for(String key: privateScreenList) {
			String alias = SessionManager.getInstance().getDeviceName(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/**
	 * @return The name of the current session.
	 */
	public String getCurrentSessionName() {
		return ChordTransportInterface.mChannel.getName();
	}
	
	/**
	 * @return The name of the device.
	 */
	public String getDeviceName() {
		return ChordNetworkManager.getChordManager().getName();
	}
	
	/**
	 * Sets the screen mode as Session Mode or App Mode.
	 * @param sessionMode PpsManager.SESSION_MODE or PpsManager.APP_MODE
	 */
	public void setScreenMode(boolean sessionMode) {
		 clearSessionList();
	 	 SessionManager.getInstance().setSessionMode(sessionMode);
	}
	
	/**
	 * Clears/Empties the list of sessions.
	 */
	public void clearSessionList() {
		privateScreenList.clear();
		publicScreenList.clear();
		SessionManager.getInstance().clearDeviceMap();
	}
	
	private void initializeEventManager() {
		 EventManager.setDefaultFactory(new ChordEventManagerFactory());
		 setEventManager();
	}
	 
	private void initializeNetworkManager() {
		 NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		 setNetworkManager();
	}
	
	private void setNetworkManager() {
		this.networkManager = NetworkManager.getInstance();
	}

	private void setEventManager() {
		this.eventManager = EventManager.getInstance();
	}
	
	/*
	 * Getters and Setters for class variables
	 */
	
	/**
	 * @return The <code>Context</code> of PpsManager.
	 */ // TODO Fix this description.
	public static Context getContext() {
		return mContext;
	}
	
	/**
	 * Sets the <code>Context</code> of PpsManager as
	 * the given <code>Context</code>.
	 * @param context
	 */ // TODO Fix this description.
	public void setContext(Context context) {
	 	 PpsManager.mContext = context;
	}
	
	/**
	 * @return
	 */
	public EventManager getEventManager() {
		return eventManager;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public List<String> getPublicScreenList() {
		return publicScreenList;
	}
	
	public void setPublicScreenList(List<String> publicScreenList) {
		this.publicScreenList = publicScreenList;
	}
	
	public List<String> getPrivateScreenList() {
		return privateScreenList;
	}
	
	public void setPrivateScreenList(List<String> privateScreenList) {
		this.privateScreenList = privateScreenList;
	}
	
	public void setScreenType(boolean screenType) {
		isPrivate = screenType;
	}
	
}
