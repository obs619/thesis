package com.llsx.pps.internal.chord;

import com.llsx.pps.event.EventManager;
import com.llsx.pps.event.EventManagerFactory;

public class ChordEventManagerFactory implements EventManagerFactory {

	@Override
	public EventManager createEventManager() {
		return new EventManager(new ChordMessageBuilder(),new ChordMessageDispatcher(new ChordTransportInterface()));
	}

}
