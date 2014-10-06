package com.cardgame.screenapi.messaging;


public interface MessageDispatcher {

	public void sendMessage(Message message);
	public void receiveMessage(Message message);
	public void setTransportInterface(TransportInterface transportInterface);

}
