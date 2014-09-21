package com.cardgame.handlers;

import android.util.Log;
import android.widget.Toast;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.PlaySharedActivity;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventHandler;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;

public class CardGameEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "Recipient:" + e.getRecipient());
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAWN:
			Log.e("card game event card drawn", "card drawn");
			PlayPersonalActivity.respondDrawRequest(e.getPayload().toString());
			break;
		case CardGameEvent.DRAW_RESPOND:
			Log.e("card game event draw respond", "draw respond");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			Toast.makeText(PPSManager.getContext(), "Received:" + ((Card)e.getPayload()).toString(), Toast.LENGTH_LONG).show();
			break;
		case CardGameEvent.CARD_PLAYED:
			if(!SessionManager.getInstance().isPersonal())
				PlaySharedActivity.addCard(((Card)e.getPayload()));
			else
				PlayPersonalActivity.removeCard(((Card)e.getPayload()));
			break;
		case CardGameEvent.TURN_OVER:
			Log.e("card game event turn over", "turn over");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			break;
		case CardGameEvent.DECK_DISTRIBUTE:
			//Log.e("card game event deck distrubute", "distrubute");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			break;
		case CardGameEvent.PLAYER_NUM:
			Log.e("card game event playernum", (Integer)e.getPayload() + "");
			PlayPersonalActivity.playerNum = (Integer)e.getPayload();
			PlayPersonalActivity.txtPlayerNum.setText("Player Number: " + (Integer)e.getPayload());
			break;
		case CardGameEvent.ADJACENT_PLAYER:
			String[] adjplay = e.getPayload().toString().split(":");
			Log.e("card game event playernum", adjplay[0] + " with node value of " + adjplay[1]);
			PlayPersonalActivity.playerToDrawFromNumber = adjplay[0];
			PlayPersonalActivity.playerToDrawFromNodeName = adjplay[1];
			PlayPersonalActivity.txtPlayerToDrawFrom.setText("Player to draw from: " + adjplay[0] + " - " + adjplay[1]);
			break;
		case CardGameEvent.OUT_OF_CARDS:
			Log.e("card game event out of cards", "out of cards player :" + (Integer)e.getPayload());
			PlaySharedActivity.notifyPlayers((Integer)e.getPayload());
			break;
		case CardGameEvent.CHANGE_NUM_PLAYERS:
			Log.e("card game event change num players", "new player to draw" + e.getPayload().toString());
			String[] newPlayer = e.getPayload().toString().split(":");
			PlayPersonalActivity.playerToDrawFromNumber = newPlayer[0];
			PlayPersonalActivity.playerToDrawFromNodeName = newPlayer[1];
			PlayPersonalActivity.txtPlayerToDrawFrom.setText("Player to draw from: " + newPlayer[0] + " - " + newPlayer[1]);
			break;
		case CardGameEvent.NOTIFY_HOST:
			Log.e("card game event notify turn", "turn of player: " + e.getPayload().toString());
			PlaySharedActivity.notifyTurn(e.getPayload().toString());
			break;
		case CardGameEvent.NOTIFY_PLAYER_TURN:
			Log.e("card game event notify player turn", "player: " + (Boolean)e.getPayload());
			PlayPersonalActivity.turn = (Boolean)e.getPayload();
			PlayPersonalActivity.txtTurn.setText("Is it your turn? " + (Boolean)e.getPayload());
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
