package com.cardgame.screenapi;

import java.util.List;

public class SessionManager {
	private String sessionID;//auto-generate
	private boolean isOpen=true;
	
	private List<String>sharedScreens;
	private List<String>personalScreens;
	//TODO: set or initialize these lists
	
	public void joinSession(String sessionID)
	{
		this.sessionID=sessionID;
		//TODO broadcast new sessionID (?)
		//
	}
	public void createSession()
	{
		//generate new sessionID and broadcast to other devices
	}
	public void leaveSession()
	{
		//leave this session. similar to Screen.close()?
	}
	/**
	 * allow other devices to join this session
	 */
	public void openSession()
	{
		isOpen=true;
	}
	public void closeSession()
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
