package com.cardgame.screenapi;
/**A change in application state. Can be global or local.
 * Global events will send messages
 * @author Andrew
 *
 */
public class Event {//can developer subclass using reflection?
	String source;//or Screen
	String recipient;//who does this event affect?
	int type;
	String payload;
	public Event(String source, String recipient, int type, String payload)
	{
		
	}
	
	/**
	 * Create a message and send it to the particular recipients
	 */
	public void sendMessage()
	{
		
	}
	

}
