package com.cardgame.screenapi;
/**A change in application state. Can be global or local.
 * Global events will send messages
 * @author Andrew
 *
 */
public class Event {//can developer subclass using reflection?
	
	
	public static final String R_SHARED_SCREENS="shared";
	public static final String  R_PERSONAL_SCREENS="personal";
	public static final String R_ALL_SCREENS="all";
	public static final String R_LOCAL_SCREEN="onlyme";
	
	public static final int T_SCREEN_TYPE_CHANGED=0;
	public static final int T_JOIN_SESSION=1;
	public static final int T_LOCK_SESSION=2;
	public static final int T_UNLOCK_SESSION=3;
	public static final int T_LEAVE_SESSION=4;
	
	private String source;//or Screen
	private String recipient;//who does this event affect?
	private int type;
	private String payload;
	/**
	 * 
	 * @param source name of device which triggered the event
	 * @param recipient name of device or group of devices to be notified of the event
	 * @param type the type of event as specified by the developer
	 * @param payload information about the event
	 */
	public Event()
	{
		
	}
	public Event(String source, String recipient, int type, String payload)
	{
		this.source=source;
		this.recipient=recipient;
		this.type=type;
		this.payload=payload;
	}
	

	public int getType() {
		return type;
	}

	public String getPayload() {
		return payload;
	}
	
	public String getSource() {
		return source;
	}

	public String getRecipient() {
		return recipient;
	}
	
	

}
