package com.cardgame.activities;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.gameengine.Card;
import com.cardgame.gameengine.transport.CardGameEventHandler;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.Screen;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * @author Sharmaine
 * 
 *		This is the game screen when the shared screen is
 *		selected.
 */

public class PlaySharedActivity extends Activity implements Screen {
	
	private ListView listCards;
	private boolean isPublic;
	private String name;
	
	private HandAdapter handAdapter;
	private PPSManager spsManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_shared);
		
		listCards = (ListView) findViewById(R.id.listSharedCardsPlayed);
		isPublic = true;
		name = null;
		
		spsManager = new PPSManager(this, false);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler(this));
		
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
	}
	
	public void addCard(Card c) {
		handAdapter.addCard(c);
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
