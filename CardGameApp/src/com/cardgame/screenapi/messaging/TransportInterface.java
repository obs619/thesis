package com.cardgame.screenapi.messaging;


public interface TransportInterface {
	
	public void send(String username, Message message);
	public void sendOnDefaultChannel(String username, Message message);
	public void sendToAllOnDefaultChannel(Message message);
	public void sendToAll(Message message);
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
