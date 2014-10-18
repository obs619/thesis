package com.llsx.pps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	/* Logical Groups / Team Tracking */
	private Map<Integer,List<String>>teamMap = new HashMap<Integer, List<String>>();
	
	/* Network / Connection Management */
	private EventManager eventManager;
	private NetworkManager networkInitializer; 
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
	 * Starts the network manager and connects the app to the network
	 */
	public void start() {
		ChordNetworkManager.initializeChordManager();
	}
	
	/**
	 * Stops the network manager and disconnects the app from the network
	 */
	public void stop() {
		ChordNetworkManager.getChordManager().stop();
	}
	
	/*
	 * Public Screen Management
	 */
	
	/**
	 * Adds the node into the list of public screens.
	 * @param nodeName fixed name of the device
	 * @param aliasName alias or name representation for the device (e.g. "Gameboard1")
	 */
	public void addPublicScreen(String nodeName, String aliasName) {
		publicScreenList.add(nodeName);
		SessionManager.getInstance().addAlias(nodeName, aliasName);
	}
	
	/**
	 * Remove the given node from the list of public screens
	 * @param nodeName fixed name of the device to be removed
	 */
	public void removeFromPublicScreen(String nodeName) {
		for(int i = 0; i < publicScreenList.size(); i++)
			if(publicScreenList.get(i).equals(nodeName))
				publicScreenList.remove(i);
	}
	
	/**
	 * @return The list of aliases for the public screen devices.
	 */ //maybe remove
	public List<String> getPublicScreenAliasList() {
		List<String> result = new ArrayList<String>();
		for(String key: publicScreenList) {
			String alias = SessionManager.getInstance().getAlias(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/*
	 * Private Screen Management
	 */
	
	/**
	 * Adds the node into the list of private screens.
	 * @param nodeName fixed name of the device
	 * @param aliasName alias or name representation for the device (e.g. "Player1")
	 */
	public void addPrivateScreen(String nodeName, String aliasName) {
		privateScreenList.add(nodeName);
		SessionManager.getInstance().addAlias(nodeName, aliasName);
	}
	
	/**
	 * Remove the given node from the list of private screens
	 * @param nodeName fixed name of the device to be removed
	 */
	public void removeFromPrivateScreen(String nodeName) {
		for(int i = 0; i < privateScreenList.size(); i++)
			if(privateScreenList.get(i).equals(nodeName))
				privateScreenList.remove(i);
	}
	
	
	/**
	 * @return The list of aliases for the private screen devices.
	 */ //maybe remove
	public List<String>getPrivateScreenAliasList() {
		List<String> result = new ArrayList<String>();
		for(String key: privateScreenList) {
			String alias = SessionManager.getInstance().getAlias(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/*
	 * Team Screen Management
	 */
	
	/**
	 * Adds the node to a team or logical group
	 * @param teamNo the assigned team number or logical group number
	 * @param nodeName fixed name of the device
	 * @param aliasName alias or name representation for the device (e.g. "Player1")
	 */
	public void addTeamScreen(int teamNo, String nodeName, String aliasName) {
		if(teamMap.get(teamNo) != null)
			teamMap.get(teamNo).add(nodeName);
		
		else {
			List<String>newTeamList = new ArrayList<String>();
			newTeamList.add(nodeName);
			teamMap.put(teamNo, newTeamList);
		}
		//aliasList.put(nodeName, aliasName); not used since we assume the node already exists?
		//TODO check if node is in public or private screen list, ensure it is no longer a "public" screen?
	}
	
	/**
	 * @param teamNo the assigned team number or logical group number
	 * @return The list of public screens belonging to the given team.
	 */
	public List<String> getTeamPublicScreenList(int teamNo) {
		List<String> listScreens = new ArrayList<String>();
		for (Iterator<String> iterator = teamMap.get(teamNo).iterator(); iterator.hasNext();) {
			String current = iterator.next();
			if (publicScreenList.contains(current))
			{
				listScreens.add(current);
			}
		}
		return listScreens;
	}
	
	/**
	 * @param teamNo the assigned team number or logical group number
	 * @return The list of private screens belonging to the given team.
	 */
	public List<String> getTeamPrivateScreenList(int teamNo) {
		List<String> listScreens = new ArrayList<String>();
		for (Iterator<String> iterator = teamMap.get(teamNo).iterator(); iterator.hasNext();) {
			String current = iterator.next();
			if (privateScreenList.contains(current))
			{
				listScreens.add(current);
			}
		}
		return listScreens;
	}
	
	/**
	 * @param teamNo the assigned team number or logical group number
	 * @return The list screens (public and private) belonging to the given team.
	 */
	public List<String> getTeamScreenList(int teamNo) {
		return teamMap.get(teamNo);
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
	 * Select which implementation you want (The default implementation is Chord).
	 */
	public void setNetworkInitializer() {
		this.networkInitializer = NetworkManager.getInstance();
	}
	
	// TODO Should this be made private instead?
	public void clearSessionList() {
		privateScreenList.clear();
		publicScreenList.clear();
		SessionManager.getInstance().clearAliasList();
	}
	
	
	private void initializeEventManager() {
		 EventManager.setDefaultFactory(new ChordEventManagerFactory());
		 setEventManager();
	}
	 
	
	private void initializeNetworkManager() {
		 NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		 setNetworkInitializer();
	}
	
	/*
	 * Getters and Setters for class variables below
	 */
	
	public static Context getContext() {
		return mContext;
	}
	
	public void setContext(Context context) {
	 	 PpsManager.mContext = context;
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
