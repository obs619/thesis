package com.llsx.pps.event;

import java.io.Serializable;

import com.llsx.pps.internal.chord.ChordNetworkManager;

public class Event {
	public static final boolean APP_EVENT = false;
	public static final boolean API_EVENT = true;
	
	public static final String R_PUBLIC_SCREENS="shared";
	public static final String R_TEAM_SHARED_SCREENS="teamshared";
	public static final String R_TEAM_PERSONAL_SCREENS="teampersonal";
	public static final String R_TEAM_SCREENS="team";
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
	public static final int REQUEST_SESSIONS=112;
	public static final int RESPOND_REQUEST_SESSIONS=113;
	
	
	private String recipient;//who does this event affect?
	private int type;
	private int teamNo;
	private boolean isApiEvent;
	private Serializable payload;
	private String session;
	
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
		isApiEvent = false;
		
		if (type == ADD_NEW_SESSION) {
			session = payload.toString();
		}
		else {
			session = ChordNetworkManager.getChordManager().getName();
		}
	}
	
	public Event(String recipient, int type, Serializable payload, int teamNo) {
		this.recipient=recipient;
		this.type=type;
		this.teamNo=teamNo;
		this.payload=payload;
		isApiEvent = false;
		
		if (type == ADD_NEW_SESSION) {
			session = payload.toString();
		}
		else {
			session = ChordNetworkManager.getChordManager().getName();
		}
	}
	
	public Event(String recipient, int type, Serializable payload, boolean isApiEvent) {
		this.recipient=recipient;
		this.type=type;
		this.payload=payload;
		this.isApiEvent = isApiEvent;
		session = ChordNetworkManager.getChordManager().getName();
		
		if (type == ADD_NEW_SESSION) {
			session = payload.toString();
		}
		else {
			session = ChordNetworkManager.getChordManager().getName();
		}
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
		return isApiEvent;
	}
	
	public void setApiEvent(boolean isApiEvent) {
		this.isApiEvent = isApiEvent;
	}
	
	public String getSession() {
		return session;
	}
	
	public int getTeamNo() {
		return teamNo;
	}

}
