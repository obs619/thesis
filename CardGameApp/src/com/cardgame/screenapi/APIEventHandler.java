package com.cardgame.screenapi;

public class APIEventHandler implements EventHandler {
//for now, assume that these events were received, not sent
	@Override
	public void handleEvent(Event e) {
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
			break;
		default:
			break;
		}

	}

}