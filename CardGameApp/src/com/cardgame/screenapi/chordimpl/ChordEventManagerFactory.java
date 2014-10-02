package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.event.EventManagerFactory;

public class ChordEventManagerFactory implements EventManagerFactory {

	@Override
	public EventManager createEventManager() {
		return new EventManager(new ChordMessageBuilder(),new ChordMessageDispatcher(new ChordTransportInterface()));
	}

}
