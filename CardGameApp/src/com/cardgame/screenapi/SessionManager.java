package com.cardgame.screenapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.widget.Toast;

import com.cardgame.screenapi.chordimpl.ChordNetworkManager;

public class SessionManager {
	
	private List<String> publicScreenList = new ArrayList<String>();//<name, sessionID>
	private List<String> privateScreenList = new ArrayList<String>();
	private Map<String,Boolean> availableSessions = new HashMap<String,Boolean>();
	private String alias;
	private Map<String,String> aliasList = new HashMap<String, String>();
	
	private boolean isPersonal;
	private boolean sessionMode = true;
	private String chosenSession = "";
	
	private static SessionManager instance = null;

	public static SessionManager getInstance() {
		if(instance==null) {
			instance=new SessionManager();
		}
		return instance;
	}
	
	// add functions
	public void addPublicScreen(String[] nodeAlias){
		publicScreenList.add(nodeAlias[0]);
		aliasList.put(nodeAlias[0], nodeAlias[1]);
	}
	
	public void addPrivateScreen(String[] nodeAlias){
		privateScreenList.add(nodeAlias[0]);
		aliasList.put(nodeAlias[0], nodeAlias[1]);
	}
	
	public void addAvailableSession(String sessionID, Boolean isLock){
		availableSessions.put(sessionID, isLock);
	}
	
	//remove functions
	public void removeAvailableSession(String sessionID){
		availableSessions.remove(sessionID);
	}
	
	public void removeNodeAlias(String nodeName){
		aliasList.remove(nodeName);
	}
	
	//getters
	public String getOwnAlias(){
		/*
		if(alias == null);
			alias = ChordNetworkManager.getChordManager().getName();
			*/
		return alias;
	}
	
	public String getOthersAlias(String key){
		return aliasList.get(key);
	}
	
	public List<String>getPublicScreenAliasList(){
		List<String> result = new ArrayList<String>();
		for(String key: publicScreenList){
			if(aliasList.containsKey(key))
				result.add(aliasList.get(key));
		}
		
		return result;
	}
	
	
	public List<String>getPrivateScreenAliasList(){
		List<String> result = new ArrayList<String>();
		for(String key: privateScreenList){
			if(aliasList.containsKey(key))
				result.add(aliasList.get(key));
		}
		
		return result;
	}
	
	public List<String> getPublicScreenList() {
		return publicScreenList;
	}
	
	public List<String> getPrivateScreenList() {
		return privateScreenList;
	}
	
	public Set<String> getAvailableSessionsSet() {
		Set<String> keys = availableSessions.keySet();
		return keys;
	}
	
	public Map<String, Boolean> getAvailableSessionsMap() {
		return availableSessions;
	}
	
	public boolean isPersonal() {
		return isPersonal;
	}
	
	public boolean isSessionMode() {
		return sessionMode;
	}
	
	public String getChosenSession() {
		return chosenSession;
	}
	
	//setters
	
	public void setAlias(String alias){
		/* if no alias is given */
		
		this.alias = alias;
	}
	
	public void setPublicScreenList(List<String> publicScreenList) {
		this.publicScreenList = publicScreenList;
	}
	
	public void setPrivateScreenList(List<String> privateScreenList) {
		this.privateScreenList = privateScreenList;
	}
	
	/**
	 * Sets whether the current device will have a public or private screen
	 * @param screenType the current device is either PUBLIC or PRIVATE
	 */
	public void setScreenType(boolean screenType) {
		this.isPersonal = screenType;
	}
	
	public void setSessionMode(boolean sessionMode) {
		this.sessionMode = sessionMode;
	}
	
	public Boolean setChosenSession(String session) {
		if(!isSessionLocked(session)) {
    		Toast.makeText(PPSManager.getContext(), "Session is Open!", Toast.LENGTH_LONG).show();
			this.chosenSession = session;
			return true;
		}
		else {
			Toast.makeText(PPSManager.getContext(), "Session is locked! Unable to join.", Toast.LENGTH_LONG).show();
			this.chosenSession = "Default";
			return false;
		}
	}
	
	public void setDefaultSession() {
		this.chosenSession = "Default";
	}
	
	//clear functions
	public void clearAliasList(){
		aliasList.clear();
	}
	
	public void clearPrivateScreenList(){
		privateScreenList.clear();
	}
	
	public void clearPublicScreenList(){
		publicScreenList.clear();
	}
	
	public void clearAvailableSessionsList(){
		availableSessions.clear();
	}
	
	public String createSession(String sessionID) {
		String deviceName = "[" + ChordNetworkManager.getChordManager().getName() + "]";
		
		addAvailableSession(sessionID + deviceName, false);
		Event e=new Event(Event.R_ALL_SCREENS
				,Event.ADD_NEW_SESSION
				,sessionID + deviceName,true);
		EventManager.getInstance().sendEvent(e);
		
		Event e1=new Event(Event.R_ALL_SCREENS
				,Event.ADD_NEW_SESSION
				,sessionID + deviceName,false);
		EventManager.getInstance().sendEvent(e1);
		
		return sessionID + deviceName;
	}
	
	public void lockSession(String sessionID) {
		if(sessionID.contains(ChordNetworkManager.getChordManager().getName())) {
			Event e=new Event(Event.R_ALL_SCREENS
					,Event.LOCK_SESSION
					,sessionID,true);
			EventManager.getInstance().sendEvent(e);
			
			Toast.makeText(PPSManager.getContext(), "Successfuly locked " + sessionID, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PPSManager.getContext(), "Cannot lock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void unlockSession(String sessionID) {
		if(sessionID.contains(ChordNetworkManager.getChordManager().getName())) {
			Event e=new Event(Event.R_ALL_SCREENS
					,Event.UNLOCK_SESSION
					,sessionID,true);
			EventManager.getInstance().sendEvent(e);
			
			Toast.makeText(PPSManager.getContext(), "Successfuly unlocked " + sessionID, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PPSManager.getContext(), "Cannot unlock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}

	public Boolean isSessionLocked(String sessionID) {
		for (Map.Entry<String, Boolean> entry : availableSessions.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(sessionID)) {
				return entry.getValue();
			}	
		}
		return null;
	}
	
}
