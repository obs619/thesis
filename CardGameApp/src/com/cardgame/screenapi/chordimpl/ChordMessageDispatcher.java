package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.TransportInterface;
import com.cardgame.screenapi.session.SessionManager;

public class ChordMessageDispatcher implements com.cardgame.screenapi.messaging.MessageDispatcher{
	
	private TransportInterface transportInterface;
	
	public ChordMessageDispatcher(ChordTransportInterface transportInterface) {
		setTransportInterface(transportInterface);
	}
	
	@Override
	public void sendMessage(Message m) {
		String recipient=m.getRecipient();
		
		if(recipient.equals(Event.R_ALL_SCREENS))
		{
			transportInterface.sendToAll(m);
		}
		else if(recipient.equals(Event.R_SHARED_SCREENS))
		{
			for(String node: SessionManager.getInstance().getPublicScreenList())
				transportInterface.send(node,m);
		}
		else if(recipient.equals(Event.R_PERSONAL_SCREENS))
		{
			for(String node: SessionManager.getInstance().getPrivateScreenList())
				transportInterface.send(node,m);
		}
		else
		{
			transportInterface.send(recipient,m);
		}
		
	}
	@Override
	public void receiveMessage(Message m) {
		EventManager.getInstance().unpackEvent(m);
	}
	
	@Override
	public void setTransportInterface(TransportInterface transportInterface) {
		this.transportInterface=transportInterface;
		transportInterface.setMessageDispatcher(this);
	}

}
