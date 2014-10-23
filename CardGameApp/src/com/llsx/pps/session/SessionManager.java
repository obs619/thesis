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

	/*
	 * Methods
	 */
	
	/**
	 * @return The current instance of SessionManager.
	 */
	public static SessionManager getInstance() {
		if(instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}
	
	/*
	 * Session ID: Saving and Loading
	 */
	
	/**
	 * Saves the current session ID and its status (locked/unlocked)
	 * into device's memory.
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
	 * Saves the ID of the default session into the device's memory.
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
	 * from the device's memory.
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
	
	/*
	 * Session List Manager
	 */
	
	/**
	 * Adds the newly created or newly received session
	 * to the list of sessions.
	 * @param sessionID unique identifier (ID) of the session
	 * @param isLock whether or not the session is locked
	 */
	public void addAvailableSession(String sessionID, Boolean isLock) {
		availableSessions.put(sessionID, isLock);
	}
	
	/**
	 * Removes the session from the session list
	 * with the specified session ID.
	 * @param sessionID unique identifier (ID) of the session
	 */
	public void removeAvailableSession(String sessionID) {
		availableSessions.remove(sessionID);
	}
	
	/**
	 * @return A <code>Set&#60;String&#62;</code> of all session IDs
	 * that can be accessed, whether locked or unlocked.
	 */
	public Set<String> getAvailableSessionsSet() {
		Set<String> keys = availableSessions.keySet();
		return keys;
	}
	
	/**
	 * Sends an event to all devices requesting for all
	 * the known sessions.
	 */
	public void requestSessions() {
		Event event = new Event(Event.R_ALL_SCREENS,
				Event.T_REQUEST_SESSIONS,
				ChordNetworkManager.getChordManager().getName(),
				Event.PPS_EVENT);
		
		EventManager.getInstance().sendEvent(event);
	}
	
	/**
	 * Creates a new session given a unique name for
	 * the session. Also announces the creation of the
	 * session to other devices.
	 * @param sessionName unique name for the session
	 * @return The session identifier (ID), which is a
	 * <code>String</code> containing the session name
	 * and the device name.
	 */
	public String createSession(String sessionName) {
		String deviceName = "[" + alias + "]";
		
		addAvailableSession(sessionName + deviceName, UNLOCK);
		
		Event event1 = new Event(Event.R_ALL_SCREENS,
				Event.T_ADD_NEW_SESSION,
				sessionName + deviceName,
				Event.PPS_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event1);
		Log.i("New Session","ADD_NEW_SESSION event sent: "+sessionName+" "+deviceName);
		
		/* The following code sends an event for the app side
		 * (maybe for UI if necessary)
		 */
		Event event2 = new Event(Event.R_ALL_SCREENS,
				Event.T_ADD_NEW_SESSION,
				sessionName + deviceName,
				Event.APP_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event2);
		
		return sessionName + deviceName;
	}
	
	/**
	 * Sets the given session as the current session.
	 * @param sessionID the selected session to be made
	 * into the current session
	 */ //TODO return boolean?
	public void setChosenSession(String sessionID) {
		if(!isSessionLocked(sessionID) || sessionID.contains(alias)) {
			Toast.makeText(PpsManager.getContext(), "Session is Open!", Toast.LENGTH_LONG).show();
			this.chosenSession = sessionID;
			this.saveSessionID();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Session is locked! Unable to join.", Toast.LENGTH_LONG).show();
			this.chosenSession = DEFAULT_SESSION;
			this.saveDefaultSessionID();
		}
	}
	
	/**
	 * Sets the default session as the current session.
	 */
	public void setDefaultSession() {
		this.chosenSession = DEFAULT_SESSION;
	}
	
	/*
	 * Lock / Unlock a Session
	 */
	
	/**
	 * Locks the given session so that no one new can join.
	 * @param sessionID the selected session to lock
	 */
	public void lockSession(String sessionID) {
		if(sessionID.contains(alias)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.T_LOCK_SESSION,
					sessionID,
					Event.PPS_EVENT);
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
	
	/**
	 * Unlocks the given session so that other people can join.
	 * @param sessionID the selected session to lock
	 */
	public void unlockSession(String sessionID) {
		if(sessionID.contains(alias)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.T_UNLOCK_SESSION,
					sessionID,
					Event.PPS_EVENT);
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
	
	/**
	 * Checks if the given session is locked.
	 * @param sessionID the selected session to check
	 * @return <code>true</code> if the session is locked,
	 * otherwise <code>false</code>. <code>null</code> if
	 * the session does not exist.
	 */
	public Boolean isSessionLocked(String sessionID) {
		for (Map.Entry<String, Boolean> entry : availableSessions.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(sessionID)) {
				return entry.getValue();
			}	
		}
		return null;
	}
	
	/*
	 * Device List Manager
	 */
	
	/**
	 * Register a new device's node name (device ID) and alias (device name).
	 * @param nodeName the identifier (ID) of the device
	 * @param aliasName name representation for the device
	 */
	public void addAlias(String nodeName, String aliasName) {
		aliasList.put(nodeName, aliasName);
	}
	
	/**
	 * Removes the alias from alias list.
	 * @param aliasName name representation for the device
	 */
	public void removeAlias(String nodeName) {
		aliasList.remove(nodeName);
	}
	
	/**
	 * Gets the alias of the target device given the device's
	 * node name.
	 * @param key node name of the target alias
	 * @return alias name of the node given a key / node name
	 * or <code>null</code> if it doesn't exist.
	 */
	public String getAlias(String key) {
		return aliasList.get(key);
	}
	
	/**
	 * Gets the node name of the target device given the
	 * device's alias.
	 * @param alias name representation for the device
	 * @return node name of the target/given alias
	 */
	public String getNodeName(String alias) {
		String result = "";
		for(Map.Entry<String, String> entry : aliasList.entrySet()) {
			if(entry.getValue().equals(alias)) {
				result = entry.getKey();
				break;
			}
		}
		return result;
	}
	
	/*
	 * Clear Functions
	 */
	
	/**
	 * Clears/Empties the list of accessible devices
	 */
	public void clearAliasList() {
		aliasList.clear();
	}
	
	/**
	 * Clears/Empties the list of available sessions
	 */
	public void clearAvailableSessionsList() {
		availableSessions.clear();
	}
	
	/*
	 * Getters and Setters of the class variables
	 */
	
	/**
	 * @return
	 */
	public Map<String, Boolean> getAvailableSessionsMap() {
		return availableSessions;
	}
	
	/**
	 * @return The current device's own alias.
	 */
	public String getOwnAlias() {
		return alias;
	}
	
	/**
	 * Sets the current device's alias as the given string.
	 * @param alias the name you want for the current device
	 */
	public void setAlias(String alias) {
		// TODO if no alias is given
		
		this.alias = alias;
	}
	
	/**
	 * @return <code>true</code> if the app is on session
	 * management mode; <code>false</code> if the app is
	 * in app mode.
	 */
	public boolean isSessionMode() {
		return sessionMode;
	}
	
	/**
	 * Sets the current session mode as the given session mode.
	 * @param sessionMode <code>true</code> if the app should be in session
	 * management mode; <code>false</code> if the app should be
	 * in app mode
	 */
	public void setSessionMode(boolean sessionMode) {
		this.sessionMode = sessionMode;
	}
	
	/**
	 * @return The current session.
	 */
	public String getChosenSession() {
		return chosenSession;
	}
	
}
