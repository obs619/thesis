package com.cardgame.handlers;


import android.util.Log;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.PlaySharedActivity;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventHandler;
import com.cardgame.screenapi.Screen;
import com.cardgame.screenapi.TransportInterface;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;
import com.cardgame.screenapi.chordimpl.ChordTransportInterface;

/**
 * Wraps deltas and events into messages that can be sent by the API layer. Also receives messages from the API layer and passes them to the World for processing.
 * @author Andrew
 *
 */
public class CardGameEventHandler implements EventHandler {
	
	private Screen screen;

	public CardGameEventHandler(Screen screen)
	{
		setScreen(screen);
	}

	@Override
	public void handleEvent(Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "Recipient:" + e.getRecipient());
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAWN:
			//update local world accordingly
			break;
		case CardGameEvent.CARD_PLAYED:
			Log.e("is screen shared", screen.isShared() + ":" );
			if(screen.isShared()) {
				((PlaySharedActivity)screen).addCard(((Card)e.getPayload()));
			}
			else {
				((PlayPersonalActivity)screen).removeCard(((Card)e.getPayload()));
			}
			break;
		case CardGameEvent.TURN_OVER:
			Log.e("card game event turn over", "turn over");
			((PlayPersonalActivity)screen).addCard(((Card)e.getPayload()));
			break;
		case Event.USER_JOIN_PRIVATE:
			Log.e("USER_JOIN_PRIVATE","pasok");
			if(!screen.isShared()) {
				PlayPersonalActivity.listNodes.add(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			break;
		case Event.USER_JOIN_PUBLIC:
			Log.e("USER_JOIN_PUBLIC","pasok");
			break;
		case Event.USER_LEFT_PRIVATE:
			Log.e("USER_LEFT_PRIVATE","pasok");
			if(!screen.isShared()) {
				PlayPersonalActivity.listNodes.remove(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			break;
		case Event.USER_LEFT_PUBLIC:
			Log.e("USER_LEFT_PUBLIC","pasok");
			break;
		}
		
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
}
