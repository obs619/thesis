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

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "");
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAWN:
			//update local world accordingly
			break;
		case CardGameEvent.CARD_PLAYED:
			int suit=Integer.parseInt(e.getPayload().split(",")[0]);
			int number=Integer.parseInt(e.getPayload().split(",")[1]);
			if(screen.isShared())
			{
				((PlaySharedActivity)screen).addCard(new Card(suit, number));
				//TODO if message was received on public screen, add card to UI
			}
			else
			{
				Log.e("suit and number", suit + ":" + number);
				Card card = new Card(number, suit);
				//if(screen.getName()==e.getSource())//this may not work yet, as screen name!=source; source is generated string
				((PlayPersonalActivity)screen).removeCard(card);
			//TODO: if local device (private screen), remove card from UI 
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
