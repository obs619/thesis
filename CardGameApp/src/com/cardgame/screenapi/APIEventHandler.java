package com.cardgame.screenapi;

import java.util.Map;

import android.util.Log;

public class APIEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event e) {
		
		Log.e("APIEventHandler", e.getType() + "");
		
		switch(e.getType())
		{
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
			SessionManager.getInstance().addAvailableSession(e.getPayload().toString(), false);
			break;
		case Event.LOCK_SESSION:
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(e.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
					entry.setValue(true);
				}	
			}
			// in case the device has its chosen session set as the "just locked session"
			if(SessionManager.getInstance().getChosenSession().equalsIgnoreCase(e.getPayload().toString()))
				SessionManager.getInstance().setDefaultSession();
			break;
		case Event.UNLOCK_SESSION:
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(e.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
					entry.setValue(false);
				}	
			}
			break;
		default:
			break;
		}

	}

}