package com.cardgame.screenapi;

public interface TransportInterface {
	public void send(String username, Message message);
	public void sendToAll(Message message);
	

}
