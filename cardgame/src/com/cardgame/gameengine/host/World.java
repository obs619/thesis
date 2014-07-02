package com.cardgame.gameengine.host;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import com.cardgame.gameengine.transport.Delta;
import com.cardgame.gameengine.transport.Event;
import com.cardgame.gameengine.transport.GameTransportManager;

/**
 * This class will handle game logic and pass messages to clients informing them of changes in game state.
 * @author Andrew
 * 
 */
public class World {
	private Area rootArea;
	private List<User>players;
	private List<User>spectators;
	private List<Card>deck;
	private GameTransportManager transportManager=new GameTransportManager();
	
	
	
	
	
	public World(List<User>players, List<User> spectators)
	{
		this.players=players;
		this.spectators=spectators;
		initDeck();
		
		rootArea=new Area("rootArea");//not sure if I should pass all these players/spectators
		Area luckyCard=new Area("luckyCard");//all cards in deck should be face down regardless of who is viewing them
		Area discardPile=new Area("discardPile",players,spectators);//cards here are face up to everyone
		luckyCard.addCard(drawFromDeck());
		rootArea.addSubArea(luckyCard);
		rootArea.addSubArea(discardPile);
		int handSize=deck.size()/players.size();
		for(User p: players)
		{
			Area newHand=new Area("handOf"+p.getUsername());
			populateHand(newHand, handSize);
			newHand.addPermittedPlayer(p);
			rootArea.addSubArea(newHand);
		}
		
		
	}
	
	/**Called when a client sends a message informing the host that it has performed some action, such as drawing or playing a card
	 * 
	 * @param event
	 */
	private void onEventReceived(Event event)
	{
		applyEvent(event);
		broadcastDelta(event);
	}
	private void applyEvent(Event event) {
		//TODO: do some mumbo jumbo to the host's representation of the game world
		
	}
	private void broadcastDelta(Event event)
	{
		List<User>players;
		players=new ArrayList<User>();
		players.addAll(this.players);
		players.remove(event.getSenderName());
		for(User p:players )
		{
		Delta delta=buildDelta(event);
		sendDelta(delta, p);
		}
	}
	/**
	 * Builds a delta based on an event(for now, one delta for all. will formulate unique delta per player later on if necessary)
	 * @param event
	 * @return
	 */
	private Delta buildDelta(Event event) {
		// TODO Auto-generated method stub
		Delta delta;
		switch(event.getType())
		{
		case Event.EVENT_DRAW_CARD:
			delta=new Delta(Delta.DELTA_CARD_DRAWN, "placeholder");
			//TODO: read event contents to find which card was drawn, and place it in the delta
			break;
		case Event.EVENT_PLAY_CARD:
			delta=new Delta(Delta.DELTA_CARD_PLAYED, "placeholder");
			break;
		case Event.EVENT_REQUEST_WORLD:
			delta=new Delta(Delta.DELTA_WORLDVIEW, "placeholder");
			break;
		}
		return null;
	}

	/**
	 * Broadcasts a message to all clients informing them of a change in world state. If the change was caused by a player action, there is no need to send a delta to the player responsible.
	 */
	private void sendDelta(Delta delta, User p)
	{
		transportManager.sendDelta(delta, p);
	}
	/**
	 * Sends a view of the world to a client.
	 */
	private void sendWorld()
	{
		
	}
	private void initDeck()
	{
		deck=new ArrayList<Card>();
		for(int i=1;i<=4;i++)
		{
			for(int j=1;j<=12;j++)
				deck.add(new Card(j,i));
		}
		shuffleDeck();
	}
	private void shuffleDeck()//super-lazy inefficient shuffling algorithm
	{
		if(deck!=null)
		{
			Random rand = new Random();
			for (int i = deck.size() - 1; i > 0; i--)
			{
			int n = rand.nextInt(i+1);
			Card temp = deck.get(i);
			deck.add(i,deck.get(n));
			deck.add(n,temp);
			}
		}
	}
	private Card drawFromDeck()
	{
		return deck.remove(0);
	}
	private void populateHand(Area hand, int handSize)
	{
		
		for(int i=0;i<handSize;i++)
		{
			hand.addCard(drawFromDeck());
		}
	}

}
