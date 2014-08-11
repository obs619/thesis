package com.cardgame.gameengine.transport;


import android.util.Log;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.PlaySharedActivity;
import com.cardgame.gameengine.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
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
public class CardGameEventHandler implements EventHandler{
	TransportInterface transportInterface=new ChordTransportInterface();//TODO pass the ChordTransportImpl so that game does not know that it's dealing with a ChordTransportIMpl specifically

	
	private Screen screen;
	

	public CardGameEventHandler(Screen screen)
	{
		setScreen(screen);
	}

	@Override
	public void handleEvent(Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "Source:" + e.getSource());
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
			Log.e("card game event turn over", "Chordname: "+ChordNetworkManager.mChordManager.getName() + "Source:" + e.getSource());
			if(e.getSource() == ChordNetworkManager.mChordManager.getName()) {
				((PlayPersonalActivity)screen).removeCard(((Card)e.getPayload()));
			}
			else {
				((PlayPersonalActivity)screen).addCard(((Card)e.getPayload()));
			}
				
			break;
		case 30:
			Log.e("pasok 30 enter personal","pasok 30");
			if(!screen.isShared()) {
				PlayPersonalActivity.listNodes.add(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			
			break;
		case 31:
			Log.e("pasok 31 enter shared","pasok 31");
			break;
		case Event.USER_LEFT:
			if(!screen.isShared()) {
				PlayPersonalActivity.listNodes.remove(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
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
