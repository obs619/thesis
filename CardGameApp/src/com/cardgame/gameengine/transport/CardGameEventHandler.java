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
			int suit=Integer.parseInt(e.getPayload().split(",")[0]);
			int number=Integer.parseInt(e.getPayload().split(",")[1]);
			Log.e("is screen shared", screen.isShared() + ":" );
			if(screen.isShared()) {
				((PlaySharedActivity)screen).addCard(new Card(number, suit));
			}
			else {
				((PlayPersonalActivity)screen).removeCard(new Card(number, suit));
			}
			break;
		case CardGameEvent.TURN_OVER:
			Log.e("card game event turn over", "Chordname: "+ChordNetworkManager.mChordManager.getName() + "Source:" + e.getSource());
			int suit2=Integer.parseInt(e.getPayload().split(",")[0]);
			int number2=Integer.parseInt(e.getPayload().split(",")[1]);
			if(e.getSource() == ChordNetworkManager.mChordManager.getName()) {
				((PlayPersonalActivity)screen).removeCard(new Card(number2, suit2));
			}
			else {
				((PlayPersonalActivity)screen).addCard(new Card(number2, suit2));
			}
				
			break;
		case 30:
			Log.e("pasok 30 enter personal","pasok 30");
			if(!screen.isShared()) {
				PlayPersonalActivity.listNodes.add(e.getSource());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			
			break;
		case 31:
			Log.e("pasok 31 enter shared","pasok 31");
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