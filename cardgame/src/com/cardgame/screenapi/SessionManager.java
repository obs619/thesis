package com.cardgame.screenapi;

import java.util.List;
import java.util.Map;

public class SessionManager {
	private String sessionID;//auto-generate
	private String sessionKey;//auto-generate?
	private boolean isOpen=true;
	private List<String>availableSessions;
	private List<String>publicScreenList;//<name, sessionID>
	private List<String>privateScreenList;
	//TODO: set or initialize these lists
	
	public void requestToJoin()
	{
		
	}
	public void joinSession(String sessionID)
	{
		this.sessionID=sessionID;
		broadcastSessionID();
		//
	}
	/**Called on receive of the message...
	 * 
	 * @param screenName
	 */
	public void onScreenJoined(String screenName)
	{
		//TODO: refine parameters?
		//add screen to list of either shared or private screens
		//send message to screen containing the session key
	}
	public void createSession()
	{
		//generate new sessionID/key and broadcast to other devices
	}
	public void leaveSession()
	{
		//leave this session. similar to Screen.close()?
	}
	private void broadcastSessionID()
	{
		//TODO broadcast new sessionID
	}
	/**
	 * allow other devices to join this session
	 */
	public void openSession()
	{
		isOpen=true;
	}
	/**prevent other devices from joining this session
	 * 
	 */
	public void lockSession()
	{
		isOpen=false;
	}
	public boolean IsSessionOpen()
	{
		return isOpen;
	}
	public void addPublicScreen(String screenName)
	{
		publicScreenList.add(screenName);
	}
	public void addPrivateScreen(String screenName)
	{
		privateScreenList.add(screenName);
	}
	public void removePublicScreen(String screenName)
	{
		publicScreenList.remove(screenName);
	}
	public void removePrivateScreen(String screenName)
	{
		privateScreenList.remove(screenName);
	}
	public void removeAvailableSession(String sessionID)
	{
		availableSessions.remove(sessionID);
	}
	public void addAvailableSession(String sessionID)
	{
		availableSessions.add(sessionID);
	}
}
