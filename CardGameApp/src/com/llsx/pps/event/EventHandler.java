package com.llsx.pps.event;

/**
 * Performs actions based on a received Event and its type
 * @author Andrew
 *
 */
public interface EventHandler {
	
	/**
	 * Handles an Event
	 * @param event Event to be handled
	 */
	public void handleEvent(Event event);

}
