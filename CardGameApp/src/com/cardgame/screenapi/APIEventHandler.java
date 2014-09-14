package com.cardgame.screenapi;

import android.util.Log;

public class APIEventHandler implements EventHandler {
	
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
		case Event.USER_JOIN_PRIVATE:
			Log.e("PERSONAL API", "pasok");
			SessionManager.getInstance().addPrivateScreen(e.getPayload().toString());
			break;
		case Event.USER_JOIN_PUBLIC:
			Log.e("SHARED API", "pasok");
			SessionManager.getInstance().addPublicScreen(e.getPayload().toString());
			break;
		case Event.USER_LEFT_PRIVATE:
			Log.e("user left private", "pasok");
			for(String node : SessionManager.getInstance().getPrivateScreenList())
				if(e.getPayload().toString().equals(node))
					SessionManager.getInstance().removePrivateScreen(node);
			break;
		case Event.USER_LEFT_PUBLIC:
			Log.e("user left public", "pasok");
			for(String node : SessionManager.getInstance().getPublicScreenList())
				if(e.getPayload().toString().equals(node))
					SessionManager.getInstance().removePublicScreen(node);
			break;
		case Event.ADD_NEW_SESSION:
			Log.e("new channel added", e.getPayload().toString());
			SessionManager.getInstance().addAvailableSession(e.getPayload().toString());
			break;
		default:
			break;
		}

	}

}