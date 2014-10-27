package com.llsx.pps.internal.chord;

import com.llsx.pps.event.EventManager;
import com.llsx.pps.event.EventManagerFactory;

/**
 * Concrete implementation of EventManagerFactory for Samsung Chord API
 * @author Andrew
 *
 */
public class ChordEventManagerFactory implements EventManagerFactory {

	
	/**
	 * @return An event manager for the chord transport interface.
	 */
	@Override
	public EventManager createEventManager() {
		return new EventManager(new ChordMessageBuilder(),new ChordMessageDispatcher(new ChordTransportInterface()));
	}

}
