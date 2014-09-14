package com.cardgame.screenapi;

import java.io.Serializable;

/**A change in application state. Can be global or local.
 * Global events will send messages
 * @author Andrew
 *
 */
public class Event {
	
	public static final String R_SHARED_SCREENS="shared";
	public static final String R_PERSONAL_SCREENS="personal";
	public static final String R_ALL_SCREENS="all";
	public static final String R_LOCAL_SCREEN="onlyme";
	
	public static final int T_SCREEN_TYPE_CHANGED=0;
	public static final int T_JOIN_SESSION=1;
	public static final int T_LOCK_SESSION=2;
	public static final int T_UNLOCK_SESSION=3;
	public static final int T_LEAVE_SESSION=4;
	public static final int T_JOIN_NETWORK=5;
	public static final int T_SEND_CURRENT_STATE=6;	
	
	public static final int USER_JOIN_PRIVATE=7;
	public static final int USER_JOIN_PUBLIC=8;
	public static final int USER_LEFT_PRIVATE=9;
	public static final int USER_LEFT_PUBLIC=10;
	
	public static final int ADD_NEW_SESSION=11;
	
	private String recipient;//who does this event affect?
	private int type;
	private boolean isAPIEvent;
	private Serializable payload;
	
	public Event() {}
	
	/**
	 * @param source name of device which triggered the event
	 * @param recipient name of device or group of devices to be notified of the event
	 * @param type the type of event as specified by the developer
	 * @param payload information about the event
	 */
	public Event(String recipient, int type, Serializable payload) {
		this.recipient=recipient;
		this.type=type;
		this.payload=payload;
		this.setAPIEvent(false);
	}
	
	public Event(String recipient, int type, Serializable payload, boolean isAPIEvent) {
		this.recipient=recipient;
		this.type=type;
		this.payload=payload;
		this.setAPIEvent(isAPIEvent);
	}
	
	public int getType() {
		return type;
	}

	public Serializable getPayload() {
		return payload;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public boolean isAPIEvent() {
		return isAPIEvent;
	}
	
	public void setAPIEvent(boolean isAPIEvent) {
		this.isAPIEvent = isAPIEvent;
	}

}
