package com.cardgame.screenapi;
/**Takes a message, converts it to some form recognizable by underlying network 
 * 
 * @author Andrew
 *
 */
public interface MessageDispatcher {

	public void sendMessage(Message m);

}
