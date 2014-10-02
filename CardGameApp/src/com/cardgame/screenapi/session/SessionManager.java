package com.cardgame.screenapi.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventManager;

public class SessionManager {
	
	public static final String DEFAULT_SESSION = "DEFAULT";
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
	
	public void saveSessionID(){
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PPSManager.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putString("session", chosenSession);
		editor.putBoolean("isLock", isSessionLocked(chosenSession));
		editor.commit();
	}
	
	public void loadSavedSessionID() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PPSManager.getContext());
		String session = sharedPreferences.getString("session", DEFAULT_SESSION);
		Boolean isLock = sharedPreferences.getBoolean("isLock", false);
		availableSessions.put(session, isLock);
		chosenSession = session;
	}
	
	/**
	 * Adds the node into public screen list.
	 * @param nodeName fixed name of the device
	 * @param aliasName name representation for the device
	 */
	public void addPublicScreen(String nodeName, String aliasName){
		publicScreenList.add(nodeName);
		aliasList.put(nodeName, aliasName);
	}
	
	/**
	 * Adds the node into private screen list.
	 * @param nodeName fixed name of the device
	 * @param aliasName name representation for the device
	 */
	public void addPrivateScreen(String nodeName, String aliasName){
		privateScreenList.add(nodeName);
		aliasList.put(nodeName, aliasName);

	}
	/**
	 * Adds the newly created session.
	 * @param sessionID name of the session
	 * @param isLock if the session is lock
	 */
	public void addAvailableSession(String sessionID, Boolean isLock){
		availableSessions.put(sessionID, isLock);
	}
	
	
	/**
	 * Removes the session from the session list 
	 * with the specified session ID
	 * @param sessionID identifier of the session
	 */
	public void removeAvailableSession(String sessionID){
		availableSessions.remove(sessionID);
	}
	
	/**
	 * Removes the alias from alias list.
	 * @param aliasName name representation for the device
	 */
	public void removeAlias(String nodeName){
		aliasList.remove(nodeName);
	}
	
	/**
	 * Returns device's own alias.
	 * @return String value of device's own alias
	 */
	public String getOwnAlias(){
		return alias;
	}
	
	public void removeFromPrivateScreen(String nodeName){
		for(int i = 0; i < privateScreenList.size(); i++)
			if(privateScreenList.get(i).equals(nodeName))
				privateScreenList.remove(i);
	}
	
	public void removeFromPublicScreen(String nodeName){
		for(int i = 0; i < publicScreenList.size(); i++)
			if(publicScreenList.get(i).equals(nodeName))
				publicScreenList.remove(i);
	}
	
	/**
	 * Returns alias value of 
	 * @param key node name of the target alias
	 * @return alias name of the node given key
	 * or not if it doesn't exist.
	 */
	public String getAlias(String key){
		return aliasList.get(key);
	}
	
	//maybe remove
	public List<String>getPublicScreenAliasList(){
		List<String> result = new ArrayList<String>();
		for(String key: publicScreenList){
			if(aliasList.containsKey(key))
				result.add(aliasList.get(key));
		}
		
		return result;
	}
	
	public String getNodeName(String alias){
		String result="";
		for(Map.Entry<String, String> entry : aliasList.entrySet()) {
		    if(entry.getValue().equals(alias))
		    {result = entry.getKey();
		    		break;
		    }
		    	
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
	
	public void setScreenType(boolean screenType) {
		this.isPersonal = screenType;
	}
	
	public void setSessionMode(boolean sessionMode) {
		this.sessionMode = sessionMode;
	}
	
	public Boolean setChosenSession(String session) {
		if(!isSessionLocked(session) || session.contains(alias)) {
    		Toast.makeText(PPSManager.getContext(), "Session is Open!", Toast.LENGTH_LONG).show();
			this.chosenSession = session;

			this.saveSessionID();
			return true;
		}
		else {
			Toast.makeText(PPSManager.getContext(), "Session is locked! Unable to join.", Toast.LENGTH_LONG).show();
			this.chosenSession = DEFAULT_SESSION;

			this.saveSessionID();
			return false;
		}
		
	}
	
	public void setDefaultSession() {
		this.chosenSession = DEFAULT_SESSION;
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
		String deviceName = "[" + alias + "]";
		
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
		if(sessionID.contains(alias)) {
			Event e=new Event(Event.R_ALL_SCREENS
					,Event.LOCK_SESSION
					,sessionID,true);
			EventManager.getInstance().sendEvent(e);
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionID.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(true);
				}	
			}
			
			Toast.makeText(PPSManager.getContext(), "Successfuly locked " + sessionID, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PPSManager.getContext(), "Cannot lock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void unlockSession(String sessionID) {
		if(sessionID.contains(alias)) {
			Event e=new Event(Event.R_ALL_SCREENS
					,Event.UNLOCK_SESSION
					,sessionID,true);
			EventManager.getInstance().sendEvent(e);
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionID.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(false);
				}	
			}
			
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
	
	public void requestSessions() {
		Event e=new Event(Event.R_ALL_SCREENS
				,Event.REQUEST_SESSIONS
				,ChordNetworkManager.getChordManager().getName(),true);
		EventManager.getInstance().sendEvent(e);
	}
	
}
