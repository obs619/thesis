package com.cardgame.handlers;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.PlaySharedActivity;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventHandler;
import com.cardgame.screenapi.session.SessionManager;

public class CardGameEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(final Event e) {

		Log.e("Handling CardGameEvent", "Type: "+e.getType() + "Recipient:" + e.getRecipient());
		switch(e.getType())
		{
		case CardGameEvent.CARD_DRAW_REQUEST:
			Log.e("card game event card drawn", "card drawn");
			PlayPersonalActivity.respondDrawRequest(e.getPayload().toString());
			break;
		case CardGameEvent.DRAW_RESPOND:
			Log.e("card game event draw respond", "draw respond");
			PlayPersonalActivity.addCard(((Card)e.getPayload()));
			Toast.makeText(PPSManager.getContext(), "Received:" + ((Card)e.getPayload()).toString() + "\r\n" +
					"From: " + PlayPersonalActivity.playerToDrawFromNumber + " = " + PlayPersonalActivity.playerToDrawFromAliasName, 
					Toast.LENGTH_LONG).show();
			break;
		case CardGameEvent.CARD_PLAYED:
			if(!SessionManager.getInstance().isPersonal())
				PlaySharedActivity.addCard(((Card)e.getPayload()));
			else
				PlayPersonalActivity.removeCard(((Card)e.getPayload()));
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
			PlayPersonalActivity.playerToDrawFromAliasName = adjplay[1];
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
			PlayPersonalActivity.playerToDrawFromAliasName = newPlayer[1];
			PlayPersonalActivity.txtPlayerToDrawFrom.setText("Player to draw from: " + newPlayer[0] + " - " + newPlayer[1]);
			if(newPlayer[2].equals("1"))
				setTurn(true);	
			else
				setTurn(false);
			break;
		case CardGameEvent.NOTIFY_PLAYER_TURN:
			Log.e("card game event notify player turn", "player: " + (Boolean)e.getPayload());
			// if true add a 3second delay before player would be able to draw
			setTurn((Boolean)e.getPayload());
			break;
		case CardGameEvent.LOSE_PLAYER:
			Log.e("card game event loseplayer", "player: " + e.getPayload().toString());
			PlayPersonalActivity.showLoseDialog();
			break;
		case Event.USER_JOIN_PRIVATE:
			Log.e("USER_JOIN_PRIVATE","pasok");
			break;
		case Event.USER_JOIN_PUBLIC:
			Log.e("USER_JOIN_PUBLIC","pasok");
			break;
		case Event.USER_LEFT_PRIVATE:
			Log.e("USER_LEFT_PRIVATE","pasok");
			break;
		case Event.USER_LEFT_PUBLIC:
			Log.e("USER_LEFT_PUBLIC","pasok");
			break;
		}
		
	}
	
	public void setTurn(final Boolean isTurn) {
		if(isTurn) {
			//buffer of 500miliseconds for the slight delay in receiving
			 new CountDownTimer(3500, 1000) {

			     public void onTick(long millisUntilFinished) {
			         PlayPersonalActivity.txtTurn.setText("Is it your turn? " + millisUntilFinished / 1000);
			     }

			     public void onFinish() {
			    	 PlayPersonalActivity.turn = isTurn;
			    	 PlayPersonalActivity.txtTurn.setText("Is it your turn? Yes");
			     }
			  }.start();
		}else {
			PlayPersonalActivity.turn = isTurn;
			PlayPersonalActivity.txtTurn.setText("Is it your turn? No");
		}
	}
	
}
