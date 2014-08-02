package com.cardgame.gameengine.transport;


import android.util.Log;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.EventHandler;
import com.cardgame.screenapi.TransportInterface;
import com.cardgame.screenapi.chordimpl.ChordTransportInterface;

/**
 * Wraps deltas and events into messages that can be sent by the API layer. Also receives messages from the API layer and passes them to the World for processing.
 * @author Andrew
 *
 */
public class CardGameEventHandler implements EventHandler{
	TransportInterface transportInterface=new ChordTransportInterface();//TODO pass the ChordTransportImpl so that game does not know that it's dealing with a ChordTransportIMpl specifically

	public void sendEvent(CardGameEvent event)
	{
		//TODO:convert event to screenapi.Message
	}
	


	@Override
	public void handleEvent(Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "");
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAWN:
			//update local world accordingly
			break;
		case CardGameEvent.CARD_PLAYED:
			int suit=Integer.parseInt(e.getPayload().split(",")[0]);
			int number=Integer.parseInt(e.getPayload().split(",")[1]);
			//TODO update public screen UI accordingly?
			//update card position
			break;
		}
	}
}
