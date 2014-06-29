package com.cardgame.host;
/**
 * This class will handle game logic and pass messages to clients informing them of changes in game state.
 * @author Andrew
 * 
 */
public class World {
	private Area rootArea;
	
	/**Called when a client sends a message informing the host that it has performed some action, such as drawing or playing a card
	 * 
	 * @param event
	 */
	private void onEventReceived(String event)
	{
		sendDelta();
	}
	/**
	 * Broadcasts a message to all clients informing them of a change in world state. If the change was caused by a player action, there is no need to send a delta to the player responsible.
	 */
	private void sendDelta()
	{
		
	}
	/**
	 * Sends a view of the world to a client.
	 */
	private void sendWorld()
	{
		
	}

}
