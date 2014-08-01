package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.EventManagerFactory;

public class ChordEventManagerFactory implements EventManagerFactory {


	@Override
	public EventManager createEventManager() {
	
		return new EventManager(new ChordMessageBuilder(),new ChordMessageDispatcher(new ChordTransportInterface()));
	}

}
