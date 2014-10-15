package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.PpsManager;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.TransportInterface;

public class ChordMessageDispatcher implements com.cardgame.screenapi.messaging.MessageDispatcher{
	
	private TransportInterface transportInterface;
	
	public ChordMessageDispatcher(ChordTransportInterface transportInterface) {
		setTransportInterface(transportInterface);
	}
	
	@Override
	public void sendMessage(Message message) {
		String recipient=message.getRecipient();
		
		if(recipient.equals(Event.R_ALL_SCREENS))
		{
			transportInterface.sendToAll(message);
		}
		else if(recipient.equals(Event.R_PUBLIC_SCREENS))
		{
			for(String node: PpsManager.getInstance().getPublicScreenList())
				transportInterface.send(node,message);
		}
		else if(recipient.equals(Event.R_PERSONAL_SCREENS))
		{
			for(String node: PpsManager.getInstance().getPrivateScreenList())
				transportInterface.send(node,message);
		}
		else if(recipient.equals(Event.R_TEAM_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamScreenList(message.getTeamNo()))
				transportInterface.send(node,message);
				
		}
		else if(recipient.equals(Event.R_TEAM_SHARED_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamPublicScreenList(message.getTeamNo()))
				transportInterface.send(node,message);
			
		}
		else if(recipient.equals(Event.R_TEAM_PERSONAL_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamPrivateScreenList(message.getTeamNo()))
				transportInterface.send(node,message);
			
		}
		else
		{
			transportInterface.send(recipient,message);
		}
		
	}
	
	@Override
	public void sendMessageOnDefaultChannel(Message message)
	{
		String recipient=message.getRecipient();
		if(recipient.equals(Event.R_ALL_SCREENS))
		{
			transportInterface.sendToAllOnDefaultChannel(message);
		}
		else if(recipient.equals(Event.R_PUBLIC_SCREENS))
		{
			for(String node: PpsManager.getInstance().getPublicScreenList())
				transportInterface.sendOnDefaultChannel(node,message);
		}
		else if(recipient.equals(Event.R_PERSONAL_SCREENS))
		{
			for(String node: PpsManager.getInstance().getPrivateScreenList())
				transportInterface.sendOnDefaultChannel(node,message);
		}
		else if(recipient.equals(Event.R_TEAM_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamScreenList(message.getTeamNo()))
				transportInterface.sendOnDefaultChannel(node,message);
				
		}
		else if(recipient.equals(Event.R_TEAM_SHARED_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamPublicScreenList(message.getTeamNo()))
				transportInterface.sendOnDefaultChannel(node,message);
			
		}
		else if(recipient.equals(Event.R_TEAM_PERSONAL_SCREENS))
		{
			for(String node: PpsManager.getInstance().getTeamPrivateScreenList(message.getTeamNo()))
				transportInterface.sendOnDefaultChannel(node,message);
			
		}
		else
		{
			//What is this for?
			transportInterface.sendOnDefaultChannel(recipient, message);
		}
		
		
	}
	@Override
	public void receiveMessage(Message message) {
		EventManager.getInstance().unpackEvent(message);
	}
	
	@Override
	public void setTransportInterface(TransportInterface transportInterface) {
		this.transportInterface=transportInterface;
		transportInterface.setMessageDispatcher(this);
	}

}
