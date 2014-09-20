package com.cardgame.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.handlers.CardGameEvent;
import com.cardgame.handlers.CardGameEventHandler;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;

public class PlayPersonalActivity extends Activity{
	
	// UI variables
	private ListView listCards;
	private TextView txtError;
	private LinearLayout layoutPassTo;
	private Spinner spinRecipient;
	private Button btnDone;
	
	// Adapter variables
	private static HandAdapter handAdapter;
	
	// Shared/Personal screen variables
	private PPSManager spsManager;
	
	public static List<String> listNodes;
	public static ArrayAdapter<String> dataAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_personal);
		
		// Link UI variables to UI
		listCards = (ListView) findViewById(R.id.listPersonalCards);
		txtError = (TextView) findViewById(R.id.txtPersonalError);
		layoutPassTo = (LinearLayout) findViewById(R.id.layoutPersonalPassTo);
		spinRecipient = (Spinner) findViewById(R.id.spinPersonalRecipient);
		btnDone = (Button) findViewById(R.id.btnPersonalDone);
		
		spsManager = new PPSManager(this, true, false);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler());
		
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
	
	public static void removeCard(Card card) {
		handAdapter.removeCard(card);
	}
	
	public static void addCard(Card c) {
		c.setSelected(false);
		handAdapter.addCard(c);
	}
	
	public void clickPlay(View v) {
		
	    List<Card> cardsToPlay = new ArrayList<Card>();
	    for( int i = 0; i < handAdapter.getCount(); i++ ){
	        Card item = (Card) handAdapter.getItem(i);
	        if(item.isSelected())
	        	cardsToPlay.add(item);
	    }
	    if(SessionManager.getInstance().getPublicScreenList().size() > 0) {
	    	if(cardsToPlay.size() == 2) {
	    		if(cardsToPlay.get(0).getNumber() == cardsToPlay.get(1).getNumber()) {
	    			for(Card card: cardsToPlay)
				    {
				    	Event e=new Event(Event.R_SHARED_SCREENS,CardGameEvent.CARD_PLAYED,card);
						EventManager.getInstance().triggerEvent(e);
				    }
			    	
			    	txtError.setVisibility(View.GONE);
	    		}
	    		else {
	    			txtError.setText("The 2 cards must have the same value!");
					txtError.setVisibility(View.VISIBLE);
			    }
		    }
		    else {
		    	txtError.setText("Please select 2 same value cards");
				txtError.setVisibility(View.VISIBLE);
		    }
	    }
	    else {
	    	txtError.setText("No active shared device!");
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
	    for( int i = 0; i < handAdapter.getCount(); i++ ) {
	        Card item = (Card) handAdapter.getItem(i);
	        if(item.isSelected())
	        	cardsToPlay.add(item);
	    }
	    if(SessionManager.getInstance().getPrivateScreenList().size() > 0) {
		    if(cardsToPlay.size() > 0) {
		    	for(Card card: cardsToPlay)
			    {
		    		removeCard(card);
			    	Event e2=new Event(spinRecipient.getSelectedItem().toString(), CardGameEvent.TURN_OVER, card);
					EventManager.getInstance().sendEvent(e2);
			    }
		    	
		    	txtError.setVisibility(View.GONE);
				layoutPassTo.setVisibility(View.GONE);
				btnDone.setVisibility(View.GONE);
		    }
		    else {
		    	txtError.setText("Please select card/s");
				txtError.setVisibility(View.VISIBLE);
		    }
	    }
	    else {
	    	txtError.setText("No active personal device!");
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
	
	public void clickCheckSession(View v) {
		Toast.makeText(this, spsManager.getCurrentSessionName(), Toast.LENGTH_LONG).show();
	}
	
}
