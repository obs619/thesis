package com.llsx.pps.event;

import java.io.Serializable;

import com.llsx.pps.internal.chord.ChordNetworkManager;

/**
 * 
 * Represents a change in application state, triggered on certain screens in a session
 * 
 * @author Andrew
 *
 */
public class Event {
	
	/**
	 * Constant used to indicate that an event is application-specific (fired from the application)
	 */
	public static final boolean APP_EVENT = false;
	/**
	 * Constant used to indicate that an event is a common event (automatically fired by the PPS API)
	 */
	public static final boolean PPS_EVENT = true;
	
	/**
	 * Recipient constant indicating that an event will be triggered on all public screens
	 */
	public static final String R_PUBLIC_SCREENS = "shared";
	/**
	 * Recipient constant indicating that an event will be triggered on all personal screens
	 */
	public static final String R_PERSONAL_SCREENS = "personal";
	/**
	 * Recipient constant indicating that an event will be triggered on all screens
	 */
	public static final String R_ALL_SCREENS = "all";
	/**
	 * Recipient constant indicating that an event will be triggered only on the local device
	 */
	public static final String R_LOCAL_SCREEN = "onlyme";
	
	/**
	 * PPS Event Type constant representing a private screen joining a session (?)
	 */
	public static final int T_USER_JOIN_PRIVATE = 100;
	/**
	 * PPS Event Type constant representing a public screen joining a session (?)
	 */
	public static final int T_USER_JOIN_PUBLIC = 101;
	/**
	 * PPS Event Type constant representing a private screen leaving a session (?)
	 */
	public static final int T_USER_LEFT_PRIVATE = 102;
	/**
	 * PPS Event Type constant representing a public screen leaving a session (?)
	 */
	public static final int T_USER_LEFT_PUBLIC = 103;
	
	/**
	 * PPS Event Type constant indicating that a device has changed its screen type
	 */
	public static final int T_SCREEN_TYPE_CHANGED = 104;
	/**
	 * PPS Event Type constant indicating that a device has joined a session
	 */
	public static final int T_JOIN_SESSION = 105;
	/**
	 * PPS Event Type constant indicating that a device has locked a session
	 */
	public static final int T_LOCK_SESSION = 106;
	/**
	 * PPS Event Type constant indicating that a device has unlocked a session
	 */
	public static final int T_UNLOCK_SESSION = 107;
	/**
	 * PPS Event Type constant indicating that a device has left a session
	 */
	public static final int T_LEAVE_SESSION = 108;
	/**
	 * PPS Event Type constant indicating that a device has connected to the local network
	 */
	public static final int T_JOIN_NETWORK = 109;
	/**
	 * PPS Event Type constant indicating that a device has sent its current state (?)
	 */
	public static final int T_SEND_CURRENT_STATE = 110;	
	
	/**
	 * PPS Event Type constant indicating that a device has created a new session
	 */
	public static final int T_ADD_NEW_SESSION = 111;
	/**
	 * PPS Event Type constant indicating that a device has requested an updated list of sessions
	 */
	public static final int T_REQUEST_SESSIONS = 112;
	
	/**
	 * PPS Event Type constant indicating that a device has responded to a request for sessions with a list of available sessions
	 */
	public static final int T_RESPOND_REQUEST_SESSIONS = 113;
	
	
	private String recipient;
	private int type;
	private boolean isPpsEvent;
	private Serializable payload;
	private String session;
	
	/*
	 * Constructors
	 */
	
	
	/**
	 * Constructs a new application-triggered Event with the given recipient, type and payload.
	 * 
	 * @param recipient name of device or group of devices to be notified of the event
	 * @param type the type of event as specified by the developer
	 * @param payload information about the event
	 */
	public Event(String recipient, int type, Serializable payload) {
		this.recipient = recipient;
		this.type = type;
		this.payload = payload;
		this.isPpsEvent = false;
		session = ChordNetworkManager.getChordManager().getName();
		
		if (type == T_ADD_NEW_SESSION) {
			session = payload.toString();
		}
		else {
			session = ChordNetworkManager.getChordManager().getName();
		}
	}
	
	/**
	 * Constructs a new Event with the given recipient, type, payload, and origin (application-triggered or PPS API-triggered).
	 * 
	 * @param recipient name of device or group of devices to be notified of the event
	 * @param type the type of event as specified by the developer
	 * @param payload information about the event
	 * @param isPpsEvent true if this event was triggered by the PPS API itself, false if this event was triggered from the application level
	 */
	public Event(String recipient, int type, Serializable payload, boolean isPpsEvent) {
		this.recipient = recipient;
		this.type = type;
		this.payload = payload;
		this.isPpsEvent = isPpsEvent;
		session = ChordNetworkManager.getChordManager().getName();
		
		if (type == T_ADD_NEW_SESSION) {
			session = payload.toString();
		}
		else {
			session = ChordNetworkManager.getChordManager().getName();
		}
	}
	
	/**
	 * 
	 * @return this Event's type, which determines how it will be handled
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return the payload (contents) of this Event
	 */
	public Serializable getPayload() {
		return payload;
	}
	
	/**
	 * 
	 * @return this Event's recipient type
	 */
	public String getRecipient() {
		return recipient;
	}
	
	/**
	 * 
	 * @return true if this event was triggered by the PPS API itself, false if this event was triggered from the application level
	 */
	public boolean isPpsEvent() {
		return isPpsEvent;
	}
	
	/**
	 * Sets whether this Event will be handled as an application-triggered Event or a PPS API-triggered Event
	 * 
	 * @param isPpsEvent true if this event was triggered by the PPS API itself, false if this event was triggered from the application level
	 */
	public void setPpsEvent(boolean isPpsEvent) {
		this.isPpsEvent = isPpsEvent;
	}
	
	/**
	 * Get the session name of the session where this Event was triggered (?)
	 * @return the session name of the session where this Event was triggered (?)
	 */
	public String getSession() {
		return session;
	}
	
}
