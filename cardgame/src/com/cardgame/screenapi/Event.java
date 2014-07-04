package com.cardgame.screenapi;
/**A change in application state. Can be global or local.
 * Global events will send messages
 * @author Andrew
 *
 */
public class Event {//can developer subclass using reflection?
	
	
	public static final String SHARED_SCREENS="shared";
	public static final String  PERSONAL_SCREENS="personal";
	public static final String ALL_SCREENS="all";
	public static final String LOCAL_SCREEN="onlyme";
	
	String source;//or Screen
	String recipient;//who does this event affect?
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
		
	}
	
	/**
	 * Create a message and send it to the particular recipients
	 */
	public void sendMessage()
	{
		
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	

}
