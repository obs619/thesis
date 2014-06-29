package com.cardgame.host;

import java.util.List;

/**
 * This class will handle game logic and pass messages to clients informing them of changes in game state.
 * @author Andrew
 * 
 */
public class World {
	private Area rootArea;
	private List<User>players;
	private List<User>spectators;
	public World(List<User>players, List<User> spectators)
	{
		this.players=players;
		this.spectators=spectators;
		rootArea=new Area("rootArea");//not sure if I should pass all these players/spectators
		Area deck=new Area("deck");//all cards in deck should be face down regardless of who is viewing them
		Area discardPile=new Area("discardPile",players,spectators);//cards here are face up to everyone
		//TODO: populate deck and discard pile with cards
		rootArea.addSubArea(deck);
		rootArea.addSubArea(discardPile);
		for(User p: players)
		{
			Area newHand=new Area("handOf"+p.getUsername());
			//TODO: populate hand
			newHand.addPermittedPlayer(p);
			rootArea.addSubArea(newHand);
		}
		
		
	}
	
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
