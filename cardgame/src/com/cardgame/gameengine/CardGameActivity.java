package com.cardgame.gameengine;

import android.app.Activity;
import android.os.Bundle;

import com.cardgame.gameengine.transport.CardGameEvent;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.Screen;
import com.srpol.chordchat.R;

public class CardGameActivity extends Activity implements Screen {
	PPSManager spsManager;//should really be instantiated in application, not activity
	String name;
	boolean isPublic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		spsManager=new PPSManager(null);
		
		//On initialize, choose whether public or private screen
		//if public screen and game has not started, show Start Game button
		//if private screen and game has not started, display cards but do not display Play Card button
		//if public screen and game has started, display cards that have been played <--consider late-joining public screens
		
	}
	
	public void playCard(String cardName)
	{
		Event e=new Event(this.getName(),Event.R_ALL_SCREENS,CardGameEvent.CARD_PLAYED,cardName);
		EventManager.getInstance().triggerEvent(e);//this will already be applied on this device thanks to the contents of Screen.triggerEvent()
		
	}
	@Override
	public boolean isShared() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setAsShared() {
		isPublic=true;
		
	}
	@Override
	public void setAsPersonal() {
		isPublic=false;
		
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name=name;
		
	}

}
