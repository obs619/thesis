package com.cardgame.screenapi.messaging;


public interface TransportInterface {
	
	public void send(String username, Message message);
	public void sendToAll(Message message);
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
