package com.cardgame.activities;

import com.cardgame.R;
import com.cardgame.screenapi.Screen;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_shared);
		
		listCards = (ListView) findViewById(R.id.listSharedCardsPlayed);
		isPublic = false;
		name = null;
	}
	
	/** On update, call this function, feel free to edit
	 * 	as necessary
	 */
	public void update() {
		// TODO receive card
		// TODO add card to listview
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
