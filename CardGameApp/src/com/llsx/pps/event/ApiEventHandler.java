package com.llsx.pps.event;

import java.util.Map;

import android.util.Log;

import com.llsx.pps.PpsManager;
import com.llsx.pps.session.SessionManager;

// TODO consider renaming to PpsEventHandler
// TODO consider making accessible only to the package
public class ApiEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event event) {
		
		Log.e("APIEventHandler", event.getType() + "");
		String[] nodeAlias;
		String key;
		String alias;
		
		switch(event.getType()) {
			case Event.USER_JOIN_PRIVATE:
				Log.e("PERSONAL API", "pasok");
				
				nodeAlias = (String[]) event.getPayload();
				key = nodeAlias[0];
				alias = nodeAlias[1];
				
				if (!PpsManager.getInstance().getPrivateScreenList().contains(key)) {
					PpsManager.getInstance().addPrivateScreen(key, alias);
				}
				
				break;
				
			case Event.USER_JOIN_PUBLIC:
				Log.e("SHARED API", "pasok");
				
				nodeAlias = (String[]) event.getPayload();
				key = nodeAlias[0];
				alias = nodeAlias[1];
				
				if (!PpsManager.getInstance().getPublicScreenList().contains(key)) {
					PpsManager.getInstance().addPublicScreen(key, alias);
				}
				break;
				
			case Event.USER_LEFT_PRIVATE:
				Log.e("user left private", "pasok");
				
				PpsManager.getInstance().removeFromPrivateScreen(event.getPayload().toString());
				SessionManager.getInstance().removeDevice(event.getPayload().toString());
				break;
				
			case Event.USER_LEFT_PUBLIC:
				Log.e("user left public", "pasok");
				
				PpsManager.getInstance().removeFromPublicScreen(event.getPayload().toString());
				SessionManager.getInstance().removeDevice(event.getPayload().toString());
				break;
				
			case Event.ADD_NEW_SESSION:
				Log.e("new channel added", event.getPayload().toString());
				SessionManager.getInstance().addAvailableSession(event.getPayload().toString(), SessionManager.UNLOCK);
				break;
				
			case Event.LOCK_SESSION:
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
						entry.setValue(SessionManager.LOCK);
					}	
				}
				// in case the device has its chosen session set as the "just locked session"
				if(SessionManager.getInstance().getChosenSession().equalsIgnoreCase(event.getPayload().toString()))
					SessionManager.getInstance().setDefaultSession();
				break;
				
			case Event.UNLOCK_SESSION:
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
						entry.setValue(SessionManager.UNLOCK);
					}	
				}
				break;
				
			case Event.REQUEST_SESSIONS:
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(entry.getValue().equals(SessionManager.UNLOCK)) {
						Event e = new Event(event.getPayload().toString(),
											Event.RESPOND_REQUEST_SESSIONS,
											entry.getKey(),
											Event.API_EVENT);
						EventManager.getInstance().sendEvent(e);
					}
				}
				break;
				
			case Event.RESPOND_REQUEST_SESSIONS:
				SessionManager.getInstance().addAvailableSession(event.getPayload().toString(), SessionManager.UNLOCK);
				Log.i("SESSION NAME RECEIVED","Received session name:"+event.getPayload().toString());
				break;
			default:
				break;
		}

	}

}