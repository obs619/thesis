package com.cardgame.screenapi;

/**
 * Called by the API to send messages without directly looking at the network layer
 * @author Andrew
 *
 */
public interface TransportInterface {
	
	public void send(String username, Message message);
	public void sendToAll(Message message);
//	public void onMessageReceived(Message message);
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
