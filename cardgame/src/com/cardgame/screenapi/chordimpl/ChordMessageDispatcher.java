package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.TransportInterface;

public class ChordMessageDispatcher implements com.cardgame.screenapi.MessageDispatcher{
	
	
	private TransportInterface transportInterface=new ChordTransportInterface();
	public ChordMessageDispatcher(ChordTransportInterface transportInterface)
	{
		setTransportInterface(transportInterface);
	}
	
	@Override
	public void sendMessage(Message m) {
		String recipient=m.getRecipient();
		if(recipient.equals(Event.ALL_SCREENS))
		{
			transportInterface.sendToAll(m);
		}
		else if(recipient.equals(Event.SHARED_SCREENS))
		{
			//TODO: for each shared screen in list, send the message
		}
		else if(recipient.equals(Event.PERSONAL_SCREENS))
		{
			
		}
		else
		{
		
			transportInterface.send(recipient,m);
		}
		
	}
	@Override
	public void receiveMessage(Message m)
	{
		
	}
	@Override
	public void setTransportInterface(TransportInterface transportInterface) {
		// TODO Auto-generated method stub
		this.transportInterface=transportInterface;
	}

}
