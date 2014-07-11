package com.cardgame.screenapi;

import java.util.List;

public class SessionManager {
	private String sessionID;//auto-generate
	private String sessionKey;//auto-generate?
	private boolean isOpen=true;
	
	private List<String>sharedScreens;
	private List<String>personalScreens;
	//TODO: set or initialize these lists
	
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
	public boolean sessionIsOpen()
	{
		return isOpen;
	}
	public void addSharedScreen(String screenName)
	{
		sharedScreens.add(screenName);
	}
	public void addPersonalScreen(String screenName)
	{
		personalScreens.add(screenName);
	}
	public void removeSharedScreen(String screenName)
	{
		sharedScreens.remove(screenName);
	}
	public void removePersonalScreen(String screenName)
	{
		personalScreens.remove(screenName);
	}
}
