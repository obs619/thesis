package com.llsx.pps.event;

/**
 * Used to create an EventManager with certain MessageBuilder and MessageDispatcher implementations.
 * @author Andrew
 *
 */
public interface EventManagerFactory {
	
	/**
	 * 
	 * @return an instance of EventManager with network layer-specific MessageBuilder and MessageDispatcher
	 */
	public EventManager createEventManager();
	
}
