package com.cardgame.screenapi;

public interface MessageDispatcher {

	public void sendMessage(Message m);
	public void receiveMessage(Message m);
	public void setTransportInterface(TransportInterface transportInterface);
	public void setEventManager(EventManager eventManager);

}
