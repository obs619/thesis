package com.llsx.pps.internal.chord;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.TransportInterface;

public class ChordMessageDispatcher implements com.llsx.pps.messaging.MessageDispatcher{
	
	private TransportInterface transportInterface;
	
	/**
	 * 
	 * @param transportInterface Chord transport interface
	 */
	public ChordMessageDispatcher(ChordTransportInterface transportInterface) {
		setTransportInterface(transportInterface);
	}
	
	/**
	 * @param message the message being sent
	 * @param isCustomChannel boolean stating whether the message being sent is to the custom channel
	 */
	@Override
	public void sendMessage(Message message, boolean isCustomChannel) {
		String recipient = message.getRecipient();
		
		if(recipient.equals(Event.R_ALL_SCREENS))
			transportInterface.sendToAll(message, isCustomChannel);
		
		else if(recipient.equals(Event.R_PUBLIC_SCREENS))
			for(String node: PpsManager.getInstance().getPublicScreenList())
				transportInterface.send(node, message, isCustomChannel);
		
		else if(recipient.equals(Event.R_PERSONAL_SCREENS))
			for(String node: PpsManager.getInstance().getPrivateScreenList())
				transportInterface.send(node, message, isCustomChannel);
		
		else
			transportInterface.send(recipient, message, isCustomChannel);
	}

	/**
	 * @param message the message being received
	 */
	@Override
	public void receiveMessage(Message message) {
		EventManager.getInstance().unpackEvent(message);
	}
	
	/**
	 * @param transportInterface the transport interface for the dispatcher
	 */
	@Override
	public void setTransportInterface(TransportInterface transportInterface) {
		this.transportInterface = transportInterface;
		transportInterface.setMessageDispatcher(this);
	}

}
