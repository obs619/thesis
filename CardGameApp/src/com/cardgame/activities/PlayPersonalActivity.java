package com.cardgame.activities;

//import android.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.gameengine.Card;
import com.cardgame.gameengine.transport.CardGameEvent;
import com.cardgame.gameengine.transport.CardGameEventHandler;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.NetworkManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.Screen;
import com.cardgame.screenapi.SessionManager;
import com.cardgame.screenapi.chordimpl.ChordEventManagerFactory;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManagerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Sharmaine
 * 
 *		This is the game screen when the personal screen is
 *		selected.
 */

public class PlayPersonalActivity extends Activity implements Screen {
	
	// UI variables
	private ListView listCards;
	private TextView txtError;
	//private Button btnPlay;
	//private Button btnPass;
	private LinearLayout layoutPassTo;
	private Spinner spinRecipient;
	private Button btnDone;
	
	private TextView txtMyHand;
	
	// Adapter variables
	private HandAdapter handAdapter;
	
	// Cards variables
	private List<Card> deckCards;
	
	// Shared/Personal screen variables
	private PPSManager spsManager;
	private boolean isPublic;
	private String name;
	
	public static List<String> listNodes;
	public static ArrayAdapter<String> dataAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_personal);
		
		// Link UI variables to UI
		listCards = (ListView) findViewById(R.id.listPersonalCards);
		txtError = (TextView) findViewById(R.id.txtPersonalError);
		//btnPlay = (Button) findViewById(R.id.btnPersonalPlay);
		//btnPass = (Button) findViewById(R.id.btnPersonalPass);
		layoutPassTo = (LinearLayout) findViewById(R.id.layoutPersonalPassTo);
		spinRecipient = (Spinner) findViewById(R.id.spinPersonalRecipient);
		btnDone = (Button) findViewById(R.id.btnPersonalDone);
		
		txtMyHand = (TextView) findViewById(R.id.myHand);
		
		// Initialize SPS variables
		isPublic = false;
		
		spsManager = new PPSManager(this, true);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler(this));

		name = ChordNetworkManager.mChordManager.getName();
		txtMyHand.setText(name);
		deckCards = new ArrayList<Card>(); 
		
		// Initialize adapters
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
		

		listNodes = new ArrayList<String>();
		listNodes.addAll(SessionManager.getInstance().getPrivateScreenList());
		
		for(String name: SessionManager.getInstance().getPrivateScreenList())
			Log.e("listnodes", name);
		
		dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_list_item_1, listNodes);
		
		spinRecipient.setAdapter(dataAdapter);
		dataAdapter.notifyDataSetChanged();
		
		initializeDeck();
		initializeHand();
	}
	
	private void initializeDeck() {
		for (int i = 1; i <= 13; i++) {
			for (int j = 1; j <= 4; j++) {
				deckCards.add(new Card(i, j));
		    }
		}
		// shuffle deck
		Collections.shuffle(deckCards);
	}
	
	private void initializeHand() {
		List<Card>handCards=new ArrayList<Card>();
		for(int i = 0; i <= 4; i++) {
			handCards.add(deckCards.get(i));
		}
		handAdapter.addCards(handCards);
	}
	
	public void clickPlay(View v) {
		// TODO check if a card is selected, if true
		// TODO send the selected card
		
	    List<Card> cardsToPlay = new ArrayList<Card>();
	    for( int i = 0; i < handAdapter.getCount(); i++ ){
	        Card item = (Card) handAdapter.getItem(i);
	        if(item.isSelected())
	        	cardsToPlay.add(item);
	    }
	    
	    for(Card c: cardsToPlay)
	    {
	    	Event e=new Event(ChordNetworkManager.mChordManager.getName(),Event.R_SHARED_SCREENS,CardGameEvent.CARD_PLAYED,c);
			EventManager.getInstance().triggerEvent(e);
	    }
	    if(false)
	    {
		// TODO if did NOT receive a reply
		txtError.setText("ERROR: ");
		txtError.setVisibility(View.VISIBLE);
		
		// TODO if did NOT select a card
		txtError.setText("Please select a card");
		txtError.setVisibility(View.VISIBLE);
	    }
	}
	
	public void clickPass(View v) {
		txtError.setVisibility(View.GONE);
		layoutPassTo.setVisibility(View.VISIBLE);
		btnDone.setVisibility(View.VISIBLE);
	}
	
	public void clickDone(View v) {
		
		List<Card> cardsToPlay = new ArrayList<Card>();
	    for( int i = 0; i < handAdapter.getCount(); i++ ){
	        Card item = (Card) handAdapter.getItem(i);
	        
	        Log.e("showlog", item.isSelected()+" :" + item.toString());
	        if(item.isSelected())
	        	cardsToPlay.add(item);
	    }
	    
	    if(cardsToPlay.size() > 0) {
	    	for(Card c: cardsToPlay)
		    {
		    	Event e2=new Event(ChordNetworkManager.mChordManager.getName(),spinRecipient.getSelectedItem().toString(),CardGameEvent.TURN_OVER,c);
				EventManager.getInstance().triggerEvent(e2);
		    }
	    	
	    	txtError.setVisibility(View.GONE);
			layoutPassTo.setVisibility(View.GONE);
			btnDone.setVisibility(View.GONE);
	    }
	    else {
	    	txtError.setText("ERROR: ");
			txtError.setVisibility(View.VISIBLE);
	    }
	    
	}

	public void clickPersonal(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getPrivateScreenList())
			nodes += node + ",";
		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void clickShared(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getPublicScreenList())
			nodes += node + ",";
		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onPause() {
		ChordNetworkManager.getChordManager().stop();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		ChordNetworkManager.initializeChordManager();
		super.onResume();
	}

	public void removeCard(Card card) {
		handAdapter.removeCard(card);
	}
	
	public void addCard(Card c) {
		c.setSelected(false);
		handAdapter.addCard(c);
	}

	@Override
	public boolean isShared() {
		return isPublic;
	}

	@Override
	public void setAsShared() {
		isPublic = true;
	}

	@Override
	public void setAsPersonal() {
		isPublic = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = ChordNetworkManager.mChordManager.getName();
	}
	
}
