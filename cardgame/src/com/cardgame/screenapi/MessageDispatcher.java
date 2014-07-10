package com.cardgame.screenapi;
/**Takes a message, converts it to some form recognizable by underlying network and sends it to recipients noted in message
 * 
 * @author Andrew
 *
 */
public interface MessageDispatcher {

	public void sendMessage(Message m);
	public void receiveMessage(Message m);
	public void setTransportInterface(TransportInterface transportInterface);
	public void setEventManager(EventManager eventManager);

}