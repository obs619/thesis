package com.llsx.pps.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.internal.chord.ChordNetworkManager;

public class SessionManager {
	
	/* Constants */
	public static final String DEFAULT_SESSION = "DEFAULT";
	public static final boolean LOCK = true;
	public static final boolean UNLOCK = false;
	
	/* Session Management Variables */
	private String alias;
	private boolean sessionMode = true;
	private String chosenSession = "";
	private Map<String,String> aliasList = new HashMap<String, String>();
	private Map<String,Boolean> availableSessions = new HashMap<String,Boolean>();
	
	/* SessionManager */
	private static SessionManager instance = null;

	/**
	 * @return The current instance of SessionManager.
	 */
	public static SessionManager getInstance() {
		if(instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}
	
	/**
	 * Saves the current session ID and its status (locked/unlocked)
	 * into device's memory
	 */
	public void saveSessionID() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PpsManager.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putString("session", chosenSession);
		
		editor.putBoolean("isLock", isSessionLocked(chosenSession));
		editor.commit();
	}
	
	/**
	 * Saves the ID for the default session into the device's memory
	 */
	public void saveDefaultSessionID() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PpsManager.getContext());
		Editor editor = sharedPreferences.edit();
		editor.putString("session", chosenSession);
		
		editor.putBoolean("isLock", UNLOCK);
		editor.commit();
	}
	
	/**
	 * Loads the last session ID and its status (locked/unlocked)
	 * from the device's memory
	 */
	public void loadSavedSessionID() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PpsManager.getContext());
		String session = sharedPreferences.getString("session", DEFAULT_SESSION);
		Boolean isLock = sharedPreferences.getBoolean("isLock", UNLOCK);
		if(!session.equals(DEFAULT_SESSION))
			availableSessions.put(session, isLock);
		chosenSession = session;
	}
	
	/**
	 * Adds the newly created session.
	 * @param sessionID name of the session
	 * @param isLock if the session is lock
	 */
	public void addAvailableSession(String sessionID, Boolean isLock) {
		availableSessions.put(sessionID, isLock);
	}
	
	public void addAlias(String nodeName, String aliasName) {
		aliasList.put(nodeName, aliasName);
	}
	
	/**
	 * Removes the session from the session list 
	 * with the specified session ID
	 * @param sessionID identifier of the session
	 */
	public void removeAvailableSession(String sessionID) {
		availableSessions.remove(sessionID);
	}
	
	/**
	 * Removes the alias from alias list.
	 * @param aliasName name representation for the device
	 */
	public void removeAlias(String nodeName) {
		aliasList.remove(nodeName);
	}
	
	/**
	 * Returns device's own alias.
	 * @return String value of device's own alias
	 */
	public String getOwnAlias() {
		return alias;
	}
	
	/**
	 * Returns alias value of 
	 * @param key node name of the target alias
	 * @return alias name of the node given key
	 * or not if it doesn't exist.
	 */
	public String getAlias(String key) {
		return aliasList.get(key);
	}
	
	public String getNodeName(String alias) {
		String result = "";
		for(Map.Entry<String, String> entry : aliasList.entrySet()) {
		  if(entry.getValue().equals(alias))
		  {result = entry.getKey();
		  		break;
		  }
		  	
		}
		return result;
	}
	
	public Set<String> getAvailableSessionsSet() {
		Set<String> keys = availableSessions.keySet();
		return keys;
	}
	
	public Map<String, Boolean> getAvailableSessionsMap() {
		return availableSessions;
	}
	
	public boolean isSessionMode() {
		return sessionMode;
	}
	
	public String getChosenSession() {
		return chosenSession;
	}
	
	//setters
	
	public void setAlias(String alias) {
		/* if no alias is given */
		
		this.alias = alias;
	}
	
	public void setSessionMode(boolean sessionMode) {
		this.sessionMode = sessionMode;
	}
	
	//set session return boolean?
	public void setChosenSession(String session) {
		if(!isSessionLocked(session) || session.contains(alias)) {
  		Toast.makeText(PpsManager.getContext(), "Session is Open!", Toast.LENGTH_LONG).show();

			this.chosenSession = session;
			this.saveSessionID();

		}
		else {
			Toast.makeText(PpsManager.getContext(), "Session is locked! Unable to join.", Toast.LENGTH_LONG).show();
			this.chosenSession = DEFAULT_SESSION;
			this.saveDefaultSessionID();
		}
	}
	
	public void setDefaultSession() {
		this.chosenSession = DEFAULT_SESSION;
	}
	
	//clear functions
	public void clearAliasList() {
		aliasList.clear();
	}
	
	public void clearAvailableSessionsList() {
		availableSessions.clear();
	}
	
	public String createSession(String sessionID) {
		String deviceName = "[" + alias + "]";
		
		addAvailableSession(sessionID + deviceName, UNLOCK);
		
		Event event1 = new Event(Event.R_ALL_SCREENS,
				Event.ADD_NEW_SESSION,
				sessionID + deviceName,
				Event.API_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event1);
		Log.i("New Session","ADD_NEW_SESSION event sent: "+sessionID+" "+deviceName);
		
		
		/*THE FF CODE IS SPECIFIC TO OUR APP. USED TO AUTO-REFRESH THE UI'S LIST OF SESSIONS*/
		Event event2 = new Event(Event.R_ALL_SCREENS,
				Event.ADD_NEW_SESSION,
				sessionID + deviceName,
				Event.APP_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event2);
		
		return sessionID + deviceName;
	}
	
	public void lockSession(String sessionID) {
		if(sessionID.contains(alias)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.LOCK_SESSION,
					sessionID,
					Event.API_EVENT);
			EventManager.getInstance().sendEventOnDefaultChannel(event );
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionID.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(LOCK);
				}	
			}
			
			Toast.makeText(PpsManager.getContext(), "Successfuly locked " + sessionID, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Cannot lock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void unlockSession(String sessionID) {
		if(sessionID.contains(alias)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.UNLOCK_SESSION,
					sessionID,
					Event.API_EVENT);
			EventManager.getInstance().sendEventOnDefaultChannel(event );
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionID.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(UNLOCK);
				}	
			}
			
			Toast.makeText(PpsManager.getContext(), "Successfuly unlocked " + sessionID, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Cannot unlock a session you did not create!", Toast.LENGTH_LONG).show();
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
		Event event = new Event(Event.R_ALL_SCREENS,
				Event.REQUEST_SESSIONS,
				ChordNetworkManager.getChordManager().getName(),
				Event.API_EVENT);
		
		EventManager.getInstance().sendEvent(event);
	}
	
}
