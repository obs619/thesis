package com.cardgame.handlers;

import android.util.Log;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.PlaySharedActivity;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventHandler;
import com.cardgame.screenapi.SessionManager;

public class CardGameEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "Recipient:" + e.getRecipient());
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAWN:
			//update local world accordingly
			break;
		case CardGameEvent.CARD_PLAYED:
			if(!SessionManager.getInstance().isPersonal()) {
				PlaySharedActivity.addCard(((Card)e.getPayload()));
			}
			else {
				PlayPersonalActivity.removeCard(((Card)e.getPayload()));
			}
			break;
		case CardGameEvent.TURN_OVER:
			Log.e("card game event turn over", "turn over");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			break;
		case CardGameEvent.DECK_DISTRIBUTE:
			Log.e("card game event deck distrubute", "distrubute");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			break;
		case Event.USER_JOIN_PRIVATE:
			Log.e("USER_JOIN_PRIVATE","pasok");
			if(SessionManager.getInstance().isPersonal()) {
				PlayPersonalActivity.listNodes.add(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			break;
		case Event.USER_JOIN_PUBLIC:
			Log.e("USER_JOIN_PUBLIC","pasok");
			break;
		case Event.USER_LEFT_PRIVATE:
			Log.e("USER_LEFT_PRIVATE","pasok");
			if(SessionManager.getInstance().isPersonal()) {
				PlayPersonalActivity.listNodes.remove(e.getPayload().toString());
				PlayPersonalActivity.dataAdapter.notifyDataSetChanged();
			}
			break;
		case Event.USER_LEFT_PUBLIC:
			Log.e("USER_LEFT_PUBLIC","pasok");
			break;
		}
		
	}
	
}
