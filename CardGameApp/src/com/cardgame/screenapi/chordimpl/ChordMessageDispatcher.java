package com.cardgame.screenapi.chordimpl;

import android.util.Log;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.SessionManager;
import com.cardgame.screenapi.TransportInterface;

public class ChordMessageDispatcher implements com.cardgame.screenapi.MessageDispatcher{
	
	private TransportInterface transportInterface;
	private EventManager eventManager;
	
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

	@Override
	public void setEventManager(EventManager eventManager) {
		this.eventManager=eventManager;
	}

}
