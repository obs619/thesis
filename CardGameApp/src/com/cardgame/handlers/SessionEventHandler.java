package com.cardgame.handlers;

import android.util.Log;

import com.cardgame.activities.SessionActivity;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventHandler;

public class SessionEventHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) {
		Log.e("Handling SessionEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
		switch(event.getType())
		{
		case Event.ADD_NEW_SESSION:
			SessionActivity.listChannels.add(event.getSession());
			SessionActivity.channelsAdapter.notifyDataSetChanged();
			break;
		}
	}

}
