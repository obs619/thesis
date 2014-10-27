package com.llsx.pps.internal.chord;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.TransportInterface;

/**
 * Concrete implementation of the MessageDispatcher using the Samsung Chord API
 * @author Andrew
 *
 */
public class ChordMessageDispatcher implements com.llsx.pps.messaging.MessageDispatcher{
	
	/**
	 * TransportInterface to be interacted with by the ChordMessageDispatcher
	 */
	private TransportInterface transportInterface;
	
	/**
	 * Construct a ChordMessageDispatcher with the given ChordTransportInterface
	 * @param transportInterface ChordTransportInterface to be used
	 */
	public ChordMessageDispatcher(ChordTransportInterface transportInterface) {
		setTransportInterface(transportInterface);
	}
	
	/**
	 * @param message the message to be sent
	 * @param isCustomChannel true if the message will be sent over a custom channel, false if it will be sent over the default channel
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
