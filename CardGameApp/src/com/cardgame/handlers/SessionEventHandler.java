package com.cardgame.handlers;

import android.util.Log;

import com.cardgame.activities.SessionActivity;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;

public class SessionEventHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) {
		Log.e("Handling SessionEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
		switch(event.getType())
		{
		case Event.T_ADD_NEW_SESSION:
			SessionActivity.listChannels.add(event.getSession());
			SessionActivity.channelsAdapter.notifyDataSetChanged();
			break;
		}
	}

}
