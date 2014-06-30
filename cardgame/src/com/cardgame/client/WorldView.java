package com.cardgame.client;

import com.cardgame.host.Area;
import com.cardgame.transport.Delta;
import com.cardgame.transport.Event;

/**
 * Container for objects as seen by client.
 * @author Andrew
 *
 */
public class WorldView {
	private Area rootArea;

	public Area getRootArea() {
		return rootArea;
	}

	public void setRootArea(Area rootArea) {
		this.rootArea = rootArea;
	}
	public void sendEvent(int eventType, String eventDetails)
	{
		//TODO: get username
		Event e=new Event("placeholder username",eventType, eventDetails);
		
		//send a message to the server containing the event type and event details (e.g. for DRAW_CARD: name of card drawn)
		
	}
	public void onDeltaReceived(Delta delta)
	{
		
	}
}
