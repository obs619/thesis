package com.cardgame.gameengine.client;

import com.cardgame.gameengine.host.Area;
import com.cardgame.gameengine.transport.Delta;
import com.cardgame.gameengine.transport.Event;

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
		applyDelta(delta);
	}

	private void applyDelta(Delta delta) {
		// TODO apply changes to WorldView; update UI accordingly
		switch(delta.getType())
		{
		case Delta.DELTA_CARD_DRAWN:
			//TODO: read delta contents
			break;
		case Delta.DELTA_CARD_PLAYED:
			break;
		case Delta.DELTA_CHANGE_TURN:
			break;
		/*case Delta.DELTA_DECK_RESHUFFLED:
			break;*/
		case Delta.DELTA_PLAYABLE_CARDS:
			break;
		case Delta.DELTA_WORLDVIEW:
			break;
		case Delta.DELTA_GAME_OVER:
			break;
		}
		
	}
}
