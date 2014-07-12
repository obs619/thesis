package com.cardgame.activities;

import com.cardgame.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Sharmaine
 * 
 *		This is the game screen when the shared screen is
 *		selected.
 */

public class PlaySharedActivity extends Activity {
	
	private ListView listCards;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_shared);
		
		listCards = (ListView) findViewById(R.id.listSharedCardsPlayed);
	}
	
	/** On update, call this function, feel free to edit
	 * 	as necessary
	 */
	public void update() {
		// TODO receive card
		// TODO add card to listview
	}
	
}
