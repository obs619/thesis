package com.cardgame.screenapi;

import com.cardgame.activities.PlayPersonalActivity;

import android.util.Log;
import android.widget.Toast;

public class APIEventHandler implements EventHandler {
//for now, assume that these events were received, not sent
	@Override
	public void handleEvent(Event e) {
		Log.e("APIEventHandler", e.getType() + "");
		switch(e.getType())
		{
		case Event.T_SCREEN_TYPE_CHANGED:
			break;

		case Event.T_JOIN_SESSION:
			//TODO add screen to list of private or public screens
			break;
		case Event.T_LOCK_SESSION:
			break;
		case Event.T_UNLOCK_SESSION:
			break;
		case Event.T_LEAVE_SESSION:
			break;
		case Event.T_JOIN_NETWORK:
			//TODO check message data for node sessionID
			break;
		case Event.USER_OWNNODE:
			Log.e("PASOK", "pasok");
			SessionManager.getInstance().addPrivateScreen(e.getSource());//temporary
			//TODO if necessary: pass your own name back to the other nodes?
			//EventManager.getInstance().triggerEvent(e)
			break;
		case Event.USER_PERSONAL:
			Log.e("PERSONAL API", "pasok");
			SessionManager.getInstance().addPrivateScreen(e.getSource());
			break;
		case Event.USER_SHARED:
			Log.e("SHARED API", "pasok");
			SessionManager.getInstance().addPublicScreen(e.getSource());
			break;
		default:
			break;
		}

	}

}