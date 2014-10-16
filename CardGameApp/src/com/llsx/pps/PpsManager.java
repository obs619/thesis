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
	public static final boolean GAME_MODE = false;

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
	
	public PpsManager() {
		initializeNetworkManager();
		initializeEventManager();
		clearSessionList();
		
		instance = this;
	}
	
	public PpsManager(Context mContext, boolean isPrivate, boolean sessionMode) {
		PpsManager.mContext = mContext;
		initializeNetworkManager();
		initializeEventManager();
		this.isPrivate = isPrivate;
		SessionManager.getInstance().setSessionMode(sessionMode);
		instance = this;
	}
	 
	public static PpsManager getInstance() {
		if (instance == null) {
			Log.e("PPSManager is null", "getInstance PPS");
			instance = factory.createPpsManager();
		}
		return instance;
	}	
	 
	public void clearSessionList() {
		privateScreenList.clear();
		publicScreenList.clear();
		SessionManager.getInstance().clearAliasList();
	}
	 
	public void initializeEventManager() {
		 EventManager.setDefaultFactory(new ChordEventManagerFactory());
		 setEventManager();
	}
	 
	public void initializeNetworkManager() {
		 NetworkManager.setDefaultFactory(new ChordNetworkManagerFactory());
		 setNetworkInitializer();
	}
	
	public void start() {
		ChordNetworkManager.initializeChordManager();
	}
	 
	public void stop() {
		ChordNetworkManager.getChordManager().stop();
	}
	
	/**
	 * Adds the node into public screen list.
	 * @param nodeName fixed name of the device
	 * @param aliasName name representation for the device
	 */
	public void addPublicScreen(String nodeName, String aliasName) {
		publicScreenList.add(nodeName);
		SessionManager.getInstance().addAlias(nodeName, aliasName);
	}
	
	public void removeFromPublicScreen(String nodeName) {
		for(int i = 0; i < publicScreenList.size(); i++)
			if(publicScreenList.get(i).equals(nodeName))
				publicScreenList.remove(i);
	}
	
	//maybe remove
	public List<String> getPublicScreenAliasList() {
		List<String> result = new ArrayList<String>();
		for(String key: publicScreenList) {
			String alias = SessionManager.getInstance().getAlias(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/**
	 * Adds the node into private screen list.
	 * @param nodeName fixed name of the device
	 * @param aliasName name representation for the device
	 */
	public void addPrivateScreen(String nodeName, String aliasName) {
		privateScreenList.add(nodeName);
		SessionManager.getInstance().addAlias(nodeName, aliasName);
	}
	
	public void removeFromPrivateScreen(String nodeName) {
		for(int i = 0; i < privateScreenList.size(); i++)
			if(privateScreenList.get(i).equals(nodeName))
				privateScreenList.remove(i);
	}
	
	public List<String>getPrivateScreenAliasList() {
		List<String> result = new ArrayList<String>();
		for(String key: privateScreenList) {
			String alias = SessionManager.getInstance().getAlias(key);
			if(alias != null)
				result.add(alias);
		}
		
		return result;
	}
	
	/*Team screen methods
	 * 
	 *
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
	
	public List<String> getTeamScreenList(int teamNo) {
		return teamMap.get(teamNo);
	}
	
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
	/* End team screen methods
	 * 
	 *
	 */
	
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
	
	/*
	 * Getters and Setters below
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
	
	public void setScreenMode(boolean sessionMode) {
		 clearSessionList();
	 	 SessionManager.getInstance().setSessionMode(sessionMode);
	}
	
	public void setScreenType(boolean screenType) {
		isPrivate = screenType;
	}
	
}
