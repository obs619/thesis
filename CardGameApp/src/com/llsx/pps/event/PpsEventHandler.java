package com.llsx.pps.event;

import java.util.Map;

import android.util.Log;

import com.llsx.pps.PpsManager;
import com.llsx.pps.session.SessionManager;


// TODO consider making accessible only to the package
/**
 * 
 * Used to handle common (non-application-specific) public/private screen events.
 * 
 * @author Andrew
 *
 */
public class PpsEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event event) {
		
		Log.e("PPS event type", event.getType() + "");
		String[] nodeAlias;
		String key;
		String alias;
		
		switch(event.getType()) {
			case Event.T_USER_JOIN_PRIVATE:
				Log.e("PRIVATE USER JOIN", "");
				nodeAlias = (String[]) event.getPayload();
				key = nodeAlias[0];
				alias = nodeAlias[1];
				
				if (!PpsManager.getInstance().getPrivateScreenList().contains(key)) {
					PpsManager.getInstance().addPrivateScreen(key, alias);
				}
				
				break;
				
			case Event.T_USER_JOIN_PUBLIC:
				Log.e("PUBLIC USER JOIN", "");
				
				nodeAlias = (String[]) event.getPayload();
				key = nodeAlias[0];
				alias = nodeAlias[1];
				
				if (!PpsManager.getInstance().getPublicScreenList().contains(key)) {
					PpsManager.getInstance().addPublicScreen(key, alias);
				}
				break;
				
			case Event.T_USER_LEFT_PRIVATE:
				Log.e("PRIVATE USER LEFT", "");
				
				PpsManager.getInstance().removeFromPrivateScreen(event.getPayload().toString());
				SessionManager.getInstance().removeDevice(event.getPayload().toString());
				break;
				
			case Event.T_USER_LEFT_PUBLIC:
				Log.e("PUBLIC USER LEFT", "");
				
				PpsManager.getInstance().removeFromPublicScreen(event.getPayload().toString());
				SessionManager.getInstance().removeDevice(event.getPayload().toString());
				break;
				
			case Event.T_ADD_NEW_SESSION:
				Log.e("NEW SESSION ADDED", event.getPayload().toString());
				SessionManager.getInstance().addAvailableSession(event.getPayload().toString(), SessionManager.UNLOCK);
				break;
				
			case Event.T_LOCK_SESSION:
				Log.e("LOCK SESSION", event.getPayload().toString());
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
						entry.setValue(SessionManager.LOCK);
					}	
				}
				// in case the device has its chosen session set as the "just locked session"
				if(SessionManager.getInstance().getSessionToJoin().equalsIgnoreCase(event.getPayload().toString()))
					SessionManager.getInstance().setDefaultCustomSession();
				break;
				
			case Event.T_UNLOCK_SESSION:
				Log.e("UNLOCK SESSION", event.getPayload().toString());
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
						entry.setValue(SessionManager.UNLOCK);
					}	
				}
				break;
				
			case Event.T_REQUEST_SESSIONS:
				Log.e("REQUEST SESSIONS", event.getPayload().toString());
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					if(entry.getValue().equals(SessionManager.UNLOCK)) {
						Event e = new Event(event.getPayload().toString(),
											Event.T_RESPOND_REQUEST_SESSIONS,
											entry.getKey(),
											Event.PPS_EVENT);
						EventManager.getInstance().sendEvent(e);
					}
				}
				break;
				
			case Event.T_RESPOND_REQUEST_SESSIONS:
				Log.e("RESPOND REQUEST SESSIONS", event.getPayload().toString());
				String[] sessionInfo=(String [])event.getPayload();
				SessionManager.getInstance().addAvailableSession(sessionInfo[0], Boolean.parseBoolean(sessionInfo[1]));
				Log.i("SESSION NAME RECEIVED","Received session name:"+sessionInfo[0]);
				Log.i("SESSION LOCK STATUS","Session locked: "+sessionInfo[1]);
				
				break;
			default:
				break;
		}

	}

}