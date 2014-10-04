package com.cardgame.screenapi.event;

import java.util.Iterator;
import java.util.Map;

import com.cardgame.screenapi.session.SessionManager;

import android.util.Log;

public class APIEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event event) {
		
		Log.e("APIEventHandler", event.getType() + "");
		String[] nodeAlias;
		String key;
		String alias;
		
		switch(event.getType())
		{
		case Event.USER_JOIN_PRIVATE:
			Log.e("PERSONAL API", "pasok");
			boolean exist = false;
			
			nodeAlias = (String[]) event.getPayload();
			key = nodeAlias[0];
			alias = nodeAlias[1];
			
			for(String username : SessionManager.getInstance().getPrivateScreenList()) {
				if(username.equals(nodeAlias[0]))
					exist = true;
			}
			if(!exist) {
				SessionManager.getInstance().addPrivateScreen(key, alias);
			}
			break;
			
		case Event.USER_JOIN_PUBLIC:
			Log.e("SHARED API", "pasok");
			boolean exist1 = false;
			
			nodeAlias = (String[]) event.getPayload();
			key = nodeAlias[0];
			alias = nodeAlias[1];
			
			for(String username : SessionManager.getInstance().getPublicScreenList()) {
				if(username.equals(nodeAlias[0]))
					exist1 = true;
			}
			if(!exist1) {
				SessionManager.getInstance().addPublicScreen(key, alias);
			}
			break;
			
		case Event.USER_LEFT_PRIVATE:
			Log.e("user left private", "pasok");
			
			SessionManager.getInstance().removeFromPrivateScreen(event.getPayload().toString());
			SessionManager.getInstance().removeAlias(event.getPayload().toString());
			Log.e("private list size", Integer.toString(SessionManager.getInstance().getPrivateScreenList().size()));
			Log.e("private aliaslist size", Integer.toString(SessionManager.getInstance().getPrivateScreenAliasList().size()));
			break;
			
		case Event.USER_LEFT_PUBLIC:
			Log.e("user left public", "pasok");
			
			SessionManager.getInstance().removeFromPublicScreen(event.getPayload().toString());
			SessionManager.getInstance().removeAlias(event.getPayload().toString());

			Log.e("public list size", Integer.toString(SessionManager.getInstance().getPublicScreenList().size()));
			Log.e("public aliaslist size", Integer.toString(SessionManager.getInstance().getPublicScreenAliasList().size()));
			break;
			
		case Event.ADD_NEW_SESSION:
			Log.e("new channel added", event.getPayload().toString());
			SessionManager.getInstance().addAvailableSession(event.getPayload().toString(), false);
			break;
			
		case Event.LOCK_SESSION:
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
					entry.setValue(true);
				}	
			}
			// in case the device has its chosen session set as the "just locked session"
			if(SessionManager.getInstance().getChosenSession().equalsIgnoreCase(event.getPayload().toString()))
				SessionManager.getInstance().setDefaultSession();
			break;
			
		case Event.UNLOCK_SESSION:
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(event.getPayload().toString().equalsIgnoreCase(entry.getKey())) {
					entry.setValue(false);
				}	
			}
			break;
		case Event.REQUEST_SESSIONS:
			for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
				if(entry.getValue().equals(false)) {
					Event e=new Event(event.getPayload().toString()
							,Event.RESPOND_REQUEST_SESSIONS
							,entry.getKey(),true);
					EventManager.getInstance().sendEvent(e);
				}
			}
			break;
		case Event.RESPOND_REQUEST_SESSIONS:
			SessionManager.getInstance().addAvailableSession(event.getPayload().toString(), false);
			break;
		default:
			break;
		}

	}

}