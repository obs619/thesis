package com.cardgame.handlers;

import android.util.Log;

import com.cardgame.activities.SessionActivity;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventHandler;

public class SessionEventHandler implements EventHandler{

	@Override
	public void handleEvent(Event e) {
		Log.e("Handling SessionEvent", "Type: "+e.getType() + "Payload: " + e.getPayload().toString());
		switch(e.getType())
		{
		case Event.ADD_NEW_SESSION:
			SessionActivity.listChannels.add(e.getSession());
			SessionActivity.channelsAdapter.notifyDataSetChanged();
			break;
		}
	}

}
