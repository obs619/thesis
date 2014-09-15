package com.cardgame.screenapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import android.widget.Toast;

import com.cardgame.screenapi.chordimpl.ChordNetworkManager;

public class SessionManager {
	
	private List<String> publicScreenList = new ArrayList<String>();//<name, sessionID>
	private List<String> privateScreenList = new ArrayList<String>();
	private Map<String,Boolean> availableSessions = new HashMap<String,Boolean>();
	
	private Map<String,String> deviceNameIDMap = new HashMap<String,String>();
	
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
	public void addPublicScreen(String screenName){
		publicScreenList.add(screenName);
	}
	
	public void addPrivateScreen(String screenName){
		privateScreenList.add(screenName);
	}
	
	public void addAvailableSession(String sessionID, Boolean isLock){
		availableSessions.put(sessionID, isLock);
	}
	
	//remove functions
	public void removePublicScreen(String screenName){
		publicScreenList.remove(screenName);
	}
	
	public void removePrivateScreen(String screenName){
		privateScreenList.remove(screenName);
	}
	
	public void removeAvailableSession(String sessionID){
		availableSessions.remove(sessionID);
	}
	
	//getters
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
	public void setPublicScreenList(List<String> publicScreenList) {
		this.publicScreenList = publicScreenList;
	}
	
	public void setPrivateScreenList(List<String> privateScreenList) {
		this.privateScreenList = privateScreenList;
	}
	
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
