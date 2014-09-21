package com.cardgame.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.handlers.CardGameEvent;
import com.cardgame.handlers.CardGameEventHandler;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;

public class PlaySharedActivity extends Activity {
	
	private ListView listCards;
	private Button btnStartGame;
	private TextView txtGameStarted;
	
	private static HandAdapter handAdapter;
	private PPSManager spsManager;
	
	private Card monkeyCard;
	private Map<Integer, String> playerMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_shared);
		
		listCards = (ListView) findViewById(R.id.listSharedCardsPlayed);
		btnStartGame = (Button) findViewById(R.id.btnStartGame);
		txtGameStarted = (TextView) findViewById(R.id.txtGameStarted);
		
		
		spsManager = new PPSManager(this, false, false);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler());
		
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		spsManager.stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		spsManager.start();
	}
	
	public static void addCard(Card c) {
		handAdapter.addCard(c);
	}
	
	public void clickMonkey(View v) {
		if(monkeyCard != null)
			Toast.makeText(this, monkeyCard.toString(), Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "No monkey card yet!", Toast.LENGTH_LONG).show();
	}
	
	public void clickPlayersList(View v) {
		String nodes = "";

		if(playerMap != null) {
			for (Map.Entry<Integer, String> entry : playerMap.entrySet()) {
		    	nodes += entry.getKey() + " - "  + entry.getValue() + "\r\n";
			}

			Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(this, "No players!", Toast.LENGTH_LONG).show();
		
	}
	
	public void clickCheckSession(View v) {
		Toast.makeText(this, spsManager.getCurrentSessionName(), Toast.LENGTH_LONG).show();
	}
	
	public void clickStart(View v) {
		//check if there is at least 1 player - should be change to 2 later on
		if(SessionManager.getInstance().getPrivateScreenList().size() > 0) {
			//initialize cards
			List<Card> deckCards = new ArrayList<Card>();
			
			for (int i = 1; i <= 13; i++) {
				for (int j = 1; j <= 4; j++) {
					deckCards.add(new Card(i, j));
			    }
			}
			// shuffle deck
			Collections.shuffle(deckCards);
			
			//get and remove last card
			monkeyCard = new Card(deckCards.get(deckCards.size() - 1).getNumber(), deckCards.get(deckCards.size() - 1).getSuit());
			deckCards.remove(deckCards.size() - 1);
			
			Log.e("Size of deck", deckCards.size() + "");
			Log.e("Monkey Card", monkeyCard.toString());
			
			//get number of players

			int numPlayers = SessionManager.getInstance().getPrivateScreenList().size();
			Log.e("Number of Players",SessionManager.getInstance().getPrivateScreenList().size() + "");
			
		    int totalCardsPerPlayer = deckCards.size() / numPlayers;
			
		    playerMap = new TreeMap<Integer, String>();
		    
		    for(int i = 0; i < numPlayers; i++)
		    	playerMap.put(i, SessionManager.getInstance().getPrivateScreenList().get(i));
		    
		    for (Map.Entry<Integer, String> entry : playerMap.entrySet()) {
		    	Log.e("Map Player", entry.getKey() + ":"  + entry.getValue());
			}
		    
		    /* 
		     * will work until 4 players maximum
		     * if 2 players, 26 - 25
		     * if 3 players, 17 - 17 - 17
		     * if 4 players, 13 - 13 - 13 - 12
		     * if 5 players, 11 - 11 - 11 - 11 - 7 - wrong
		     */
			if(totalCardsPerPlayer * numPlayers != deckCards.size())
				totalCardsPerPlayer += 1;
			
			List<List<Card>> subCards = new ArrayList<List<Card>>();
		    
		    for (int i = 0; i < deckCards.size(); i+=totalCardsPerPlayer) {
		    	subCards.add(new ArrayList<Card>(deckCards.subList(i, Math.min(deckCards.size(), i + totalCardsPerPlayer))));
		    }
		    
		    for(int i = 0; i < numPlayers; i++) {
		    	for(int j = 0 ; j < subCards.get(i).size(); j++) {
		    		Event e= new Event(playerMap.get(i),CardGameEvent.DECK_DISTRIBUTE, subCards.get(i).get(j));
					EventManager.getInstance().sendEvent(e);
		    	}
		    	
		    	// send own player number
		    	Event e= new Event(playerMap.get(i),CardGameEvent.PLAYER_NUM, i);
				EventManager.getInstance().sendEvent(e);
				
				if(i != numPlayers - 1) {
					Log.e("Adjacent", "not last player");
					int nxtPlayer = i + 1;
					Event e1= new Event(playerMap.get(i),CardGameEvent.ADJACENT_PLAYER, nxtPlayer + ":" + playerMap.get(i + 1));
					EventManager.getInstance().sendEvent(e1);
				}
				else if(i == numPlayers - 1) {
					Log.e("Adjacent", "last player");
					Event e1= new Event(playerMap.get(i),CardGameEvent.ADJACENT_PLAYER, 0 + ":" + playerMap.get(0));
					EventManager.getInstance().sendEvent(e1);
				}
								
		    	Log.e("Sub size", "Player's hand size: " + i + " = " + subCards.get(i).size() + "");
		    }
		    
		    
		    btnStartGame.setVisibility(View.GONE);
		    txtGameStarted.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(this, "Can't start game with no players!", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
