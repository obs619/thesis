package com.cardgame.gameengine.transport;

import android.util.Log;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.activities.SessionActivity;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventHandler;

public class SessionEventHandler implements EventHandler{

	@Override
	public void handleEvent(Event e) {
		Log.e("Handling SessionEvent", "Type: "+e.getType() + "Source:" + e.getSource() + "Payload: " + e.getPayload().toString());
		switch(e.getType())
		{
		case Event.NEW_CHANNEL_ADD:
			SessionActivity.listChannels.add(e.getPayload().toString());
			SessionActivity.channelsAdapter.notifyDataSetChanged();
			break;
		}
	}

}
