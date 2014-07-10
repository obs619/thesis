package com.cardgame.gameengine;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;


import com.cardgame.gameengine.transport.CardGameEvent;
import com.cardgame.gameengine.transport.CardGameEventHandler;
import com.cardgame.screenapi.Screen;

/**
 * This class will handle game logic and pass messages to clients informing them of changes in game state.
 * @author Andrew
 * 
 */
public class HostWorld extends World{
	private List<Screen>players;
	private List<Screen>spectators;
	private List<Card>deck;
	//private GameTransportManager transportManager=new GameTransportManager();
	
	
	
	
	
	public HostWorld(List<Screen>players, List<Screen> spectators)
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
		for(Screen p: players)
		{
			Area newHand=new Area("handOf"+p.getName());
			populateHand(newHand, handSize);
			newHand.addPermittedPlayer(p);
			rootArea.addSubArea(newHand);
		}
		
		
	}
	

	

	/**
	 * Broadcasts a message to all clients informing them of a change in world state. If the change was caused by a player action, there is no need to send a delta to the player responsible.
	 */
	/*private void sendDelta(Delta delta, User p)
	{
		transportManager.sendDelta(delta, p);
	}*/
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
	public void startGame()
	{
		
		triggerEvent(CardGameEvent.START_GAME,"1");//this will already be applied on this device thanks to the contents of Screen.triggerEvent()
		
	}
}
