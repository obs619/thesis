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
	private String deviceName;
	private boolean sessionMode = true;
	private String chosenSession = "";
	private Map<String,String> deviceMap = new HashMap<String, String>();
	private Map<String,Boolean> availableSessionsMap = new HashMap<String,Boolean>();
	
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
	public void saveSessionId() {
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
	public void saveDefaultSessionId() {
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
	public void loadSavedSessionId() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(PpsManager.getContext());
		String session = sharedPreferences.getString("session", DEFAULT_SESSION);
		Boolean isLock = sharedPreferences.getBoolean("isLock", UNLOCK);
		if(!session.equals(DEFAULT_SESSION))
			availableSessionsMap.put(session, isLock);
		chosenSession = session;
	}
	
	/*
	 * Session List Manager
	 */
	
	/**
	 * Adds the newly created or newly received session
	 * to the list of sessions.
	 * @param sessionId unique identifier (ID) of the session
	 * @param isLock whether or not the session is locked
	 */
	public void addAvailableSession(String sessionId, Boolean isLock) {
		availableSessionsMap.put(sessionId, isLock);
	}
	
	/**
	 * Removes the session from the session list
	 * with the specified session ID.
	 * @param sessionId unique identifier (ID) of the session
	 */
	public void removeAvailableSession(String sessionId) {
		availableSessionsMap.remove(sessionId);
	}
	
	/**
	 * @return A <code>Set&#60;String&#62;</code> of all session IDs
	 * that can be accessed, whether locked or unlocked.
	 */
	public Set<String> getAvailableSessions() {
		Set<String> keys = availableSessionsMap.keySet();
		return keys;
	}
	
	/**
	 * Sends an event to all devices requesting for all
	 * the known sessions.
	 */
	public void requestSessions() {
		Event event = new Event(Event.R_ALL_SCREENS,
				Event.REQUEST_SESSIONS,
				ChordNetworkManager.getChordManager().getName(),
				Event.API_EVENT);
		
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
		String taggedDeviceName = "[" + deviceName + "]";
		
		addAvailableSession(sessionName + taggedDeviceName, UNLOCK);
		
		Event event1 = new Event(Event.R_ALL_SCREENS,
				Event.ADD_NEW_SESSION,
				sessionName + taggedDeviceName,
				Event.API_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event1);
		Log.i("New Session","ADD_NEW_SESSION event sent: "+sessionName+" "+taggedDeviceName);
		
		/* The following code sends an event for the app side
		 * (maybe for UI if necessary)
		 */
		Event event2 = new Event(Event.R_ALL_SCREENS,
				Event.ADD_NEW_SESSION,
				sessionName + taggedDeviceName,
				Event.APP_EVENT);
		EventManager.getInstance().sendEventOnDefaultChannel(event2);
		
		return sessionName + taggedDeviceName;
	}
	
	/**
	 * Sets the given session as the current session.
	 * @param sessionId the selected session to be made
	 * into the current session
	 */ //TODO return boolean?
	public void setChosenSession(String sessionId) {
		if(!isSessionLocked(sessionId) || sessionId.contains(deviceName)) {
			Toast.makeText(PpsManager.getContext(), "Session is Open!", Toast.LENGTH_LONG).show();
			this.chosenSession = sessionId;
			this.saveSessionId();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Session is locked! Unable to join.", Toast.LENGTH_LONG).show();
			this.chosenSession = DEFAULT_SESSION;
			this.saveDefaultSessionId();
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
	 * @param sessionId the selected session to lock
	 */
	public void lockSession(String sessionId) {
		if(sessionId.contains(deviceName)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.LOCK_SESSION,
					sessionId,
					Event.API_EVENT);
			EventManager.getInstance().sendEventOnDefaultChannel(event );
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionId.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(LOCK);
				}	
			}
			
			Toast.makeText(PpsManager.getContext(), "Successfuly locked " + sessionId, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Cannot lock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Unlocks the given session so that other people can join.
	 * @param sessionId the selected session to lock
	 */
	public void unlockSession(String sessionId) {
		if(sessionId.contains(deviceName)) {
			Event event = new Event(Event.R_ALL_SCREENS,
					Event.UNLOCK_SESSION,
					sessionId,
					Event.API_EVENT);
			EventManager.getInstance().sendEventOnDefaultChannel(event );
			
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(sessionId.equalsIgnoreCase(entry.getKey())) {
					entry.setValue(UNLOCK);
				}	
			}
			
			Toast.makeText(PpsManager.getContext(), "Successfuly unlocked " + sessionId, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(PpsManager.getContext(), "Cannot unlock a session you did not create!", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Checks if the given session is locked.
	 * @param sessionId the selected session to check
	 * @return <code>true</code> if the session is locked,
	 * otherwise <code>false</code>. <code>null</code> if
	 * the session does not exist.
	 */
	public Boolean isSessionLocked(String sessionId) {
		for (Map.Entry<String, Boolean> entry : availableSessionsMap.entrySet()) {
			if(entry.getKey().equalsIgnoreCase(sessionId)) {
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
	 * @param deviceId the identifier (ID) of the device
	 * @param deviceName name representation for the device
	 */
	public void addDevice(String deviceId, String deviceName) {
		deviceMap.put(deviceId, deviceName);
	}
	
	/**
	 * Removes the alias from alias list.
	 * @param deviceId name representation for the device
	 */
	public void removeDevice(String deviceId) {
		deviceMap.remove(deviceId);
	}
	
	/**
	 * Gets the alias of the target device given the device's
	 * node name.
	 * @param deviceId node name of the target alias
	 * @return alias name of the node given a key / node name
	 * or <code>null</code> if it doesn't exist.
	 */
	public String getDeviceName(String deviceId) {
		return deviceMap.get(deviceId);
	}
	
	/**
	 * Gets the node name of the target device given the
	 * device's alias.
	 * @param deviceName name representation for the device
	 * @return The device ID of the target/given alias.
	 */
	public String getDeviceId(String deviceName) {
		String result = "";
		for(Map.Entry<String, String> entry : deviceMap.entrySet()) {
			if(entry.getValue().equals(deviceName)) {
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
	public void clearDeviceMap() {
		deviceMap.clear();
	}
	
	/**
	 * Clears/Empties the list of available sessions
	 */
	public void clearAvailableSessionsMap() {
		availableSessionsMap.clear();
	}
	
	/*
	 * Getters and Setters of the class variables
	 */
	
	/**
	 * @return
	 */
	public Map<String, Boolean> getAvailableSessionsMap() {
		return availableSessionsMap;
	}
	
	/**
	 * @return The current device's own alias.
	 */
	public String getOwnDeviceName() {
		return deviceName;
	}
	
	/**
	 * Sets the current device's alias as the given string.
	 * @param deviceName the name you want for the current device
	 */
	public void setDeviceName(String deviceName) {
		// TODO if no alias is given
		
		this.deviceName = deviceName;
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
