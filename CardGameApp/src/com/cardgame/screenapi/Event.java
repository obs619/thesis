package com.cardgame.screenapi;

import java.io.Serializable;

public class Event {
	
	public static final String R_SHARED_SCREENS="shared";
	public static final String R_PERSONAL_SCREENS="personal";
	public static final String R_ALL_SCREENS="all";
	public static final String R_LOCAL_SCREEN="onlyme";
	
	public static final int USER_JOIN_PRIVATE=100;
	public static final int USER_JOIN_PUBLIC=101;
	public static final int USER_LEFT_PRIVATE=102;
	public static final int USER_LEFT_PUBLIC=103;
	
	public static final int T_SCREEN_TYPE_CHANGED=104;
	public static final int T_JOIN_SESSION=105;
	public static final int LOCK_SESSION=106;
	public static final int UNLOCK_SESSION=107;
	public static final int T_LEAVE_SESSION=108;
	public static final int T_JOIN_NETWORK=109;
	public static final int T_SEND_CURRENT_STATE=110;	
	
	public static final int ADD_NEW_SESSION=111;
	
	private String recipient;//who does this event affect?
	private int type;
	private boolean isAPIEvent;
	private Serializable payload;
	
	public Event() {}
	
	/**
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
