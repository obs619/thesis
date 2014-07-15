package com.cardgame.activities;

//import android.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.gameengine.Card;
import com.cardgame.gameengine.transport.CardGameEvent;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.Screen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
	
	// Adapter variables
	private HandAdapter handAdapter;
	
	// Cards variables
	private List<Card> deckCards;
	private List<Card> handCards;
	
	// Shared/Personal screen variables
	//private PPSManager spsManager;
	private boolean isPublic;
	private String name;
	
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
		
		// Initialize SPS variables
		isPublic = false;
		name = null;
		//spsManager = new PPSManager(this);
		
		// TODO send and receive join game message
		
		deckCards = new ArrayList<Card>(); 
		handCards = new ArrayList<Card>(); 
		
		// Initialize adapters
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
		
		initializeDeck();
		initializeHand();
		
		
		// TODO receive list of players
		// TODO populate spinner with players
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
	    
	    
		// TODO if did NOT receive a reply
		txtError.setText("ERROR: ");
		txtError.setVisibility(View.VISIBLE);
		
		// TODO if did NOT select a card
		txtError.setText("Please select a card");
		txtError.setVisibility(View.VISIBLE);
	}
	
	public void clickPass(View v) {
		// TODO check if a card is selected, if true
		txtError.setVisibility(View.GONE);
		layoutPassTo.setVisibility(View.VISIBLE);
		btnDone.setVisibility(View.VISIBLE);
		
		// TODO else
		txtError.setText("Please select a card");
		txtError.setVisibility(View.VISIBLE);
	}
	
	public void clickDone(View v) {
		String recipient = null;
		String cardName = null;
		// TODO check if a recipient is selected
		// TODO send card to selected recipient
		
		//not sure if these all go here or if they go in clickPlay() and clickPass()
		Event e=new Event(getName(),recipient,CardGameEvent.CARD_PLAYED,cardName);
		EventManager.getInstance().applyEvent(e);
		
		Event e2=new Event(getName(),recipient,CardGameEvent.TURN_OVER,"");
		EventManager.getInstance().applyEvent(e2);
		
		// TODO check if a reply was received, if true
		txtError.setVisibility(View.GONE);
		layoutPassTo.setVisibility(View.GONE);
		btnDone.setVisibility(View.GONE);
		
		// TODO else
		txtError.setText("ERROR: ");
		txtError.setVisibility(View.VISIBLE);
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
		this.name = name;
	}
	
}
