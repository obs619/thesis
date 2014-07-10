package com.cardgame.gameengine;


import com.cardgame.gameengine.transport.CardGameEvent;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Screen;

/**
 * Container for objects as seen by client.
 * @author Andrew
 *
 */
public class World {
	protected Area rootArea;
	protected Screen screen;
	public Area getRootArea() {
		return rootArea;
	}

	public void setRootArea(Area rootArea) {
		this.rootArea = rootArea;
	}
	public void triggerEvent(int eventType, String eventDetails)
	{
		//TODO: get username
		CardGameEvent e=new CardGameEvent(screen.getName(),Event.ALL_SCREENS,eventType, eventDetails);
		screen.triggerEvent(e);
		//send a message to the server containing the event type and event details (e.g. for DRAW_CARD: name of card drawn)
		
	}

	
	
	
		
	
}
