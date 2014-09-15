package com.cardgame.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.handlers.CardGameEventHandler;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.chordimpl.ChordTransportInterface;

public class PlaySharedActivity extends Activity {
	
	private ListView listCards;
	
	private static HandAdapter handAdapter;
	private PPSManager spsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_shared);
		
		listCards = (ListView) findViewById(R.id.listSharedCardsPlayed);
		
		spsManager = new PPSManager(this, false, false);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler());
		
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
	}
	
	public static void addCard(Card c) {
		handAdapter.addCard(c);
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
		Toast.makeText(this, ChordTransportInterface.mChannel.getName(), Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		ChordNetworkManager.getChordManager().stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		ChordNetworkManager.initializeChordManager();
	}
	
}
