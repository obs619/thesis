package com.cardgame.gameengine.transport;

import com.cardgame.gameengine.host.User;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.EventHandler;
import com.cardgame.screenapi.TransportInterface;
import com.cardgame.screenapi.chordimpl.ChordTransportImpl;

/**
 * Wraps deltas and events into messages that can be sent by the API layer. Also receives messages from the API layer and passes them to the World for processing.
 * @author Andrew
 *
 */
public class GameTransportManager implements EventHandler{
	TransportInterface transportInterface=new ChordTransportImpl();//TODO pass the ChordTransportImpl so that game does not know that it's dealing with a ChordTransportIMpl specifically
	public void sendDelta(Delta delta, User p)
	{
		//TODO:convert delta to screenapi.Message
	}
	public void sendEvent(Event event)
	{
		//TODO:convert event to screenapi.Message
	}
	
	@Override
	public void handleMessage(Message message)
	{
		//TODO: identify message type and pass it back to the world
	}
}
