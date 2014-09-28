package com.cardgame.screenapi;

import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public class APIEventHandler implements EventHandler {
	
	@Override
	public void handleEvent(Event e) {
		
		Log.e("APIEventHandler", e.getType() + "");
		String[] nodeAlias;
		
		switch(e.getType())
		{
		case Event.USER_JOIN_PRIVATE:
			Log.e("PERSONAL API", "pasok");
			boolean exist = false;
			
			//nodeAlias
			nodeAlias = (String[]) e.getPayload();
			System.out.println(nodeAlias[0] + " " + nodeAlias[1]);
			
			
			for(String username : SessionManager.getInstance().getPrivateScreenList()) {
				if(username.equals(nodeAlias[0]))
					exist = true;
			}
			if(!exist) {
				SessionManager.getInstance().addPrivateScreen(nodeAlias);
			}
			break;
			
		case Event.USER_JOIN_PUBLIC:
			Log.e("SHARED API", "pasok");
			boolean exist1 = false;
			
			nodeAlias = (String[]) e.getPayload();
			System.out.println(nodeAlias[0] + " " + nodeAlias[1]);
			Log.e("join", "join private");
			for(String username : SessionManager.getInstance().getPublicScreenList()) {
				if(username.equals(nodeAlias[0]))
					exist1 = true;
			}
			if(!exist1) {
				SessionManager.getInstance().addPublicScreen(nodeAlias);
			}
			break;
			
		case Event.USER_LEFT_PRIVATE:
			Log.e("user left private", "pasok");
			
			//can be try to optimize
			Iterator<String> it = SessionManager.getInstance().getPrivateScreenList().iterator();
			while(it.hasNext()){
				String value = it.next();
				if(value.equals(e.getPayload().toString())){
					it.remove();
				}
			}
			SessionManager.getInstance().removeNodeAlias(e.getPayload().toString());
			break;
			
		case Event.USER_LEFT_PUBLIC:
			Log.e("user left public", "pasok");
			//can be try to optimize
			Iterator<String> it2 = SessionManager.getInstance().getPublicScreenList().iterator();
			while(it2.hasNext()){
				String value = it2.next();
				if(value.equals(e.getPayload().toString())){
					it2.remove();
				}
			}
			SessionManager.getInstance().removeNodeAlias(e.getPayload().toString());
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