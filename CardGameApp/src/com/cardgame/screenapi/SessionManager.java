package com.cardgame.screenapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SessionManager {
	private String sessionID;//auto-generate
	private String sessionKey;//auto-generate?
	private boolean isOpen=true;
	private static List<String>availableSessions = new ArrayList<String>();
	private static List<String>publicScreenList = new ArrayList<String>();//<name, sessionID>
	private static List<String>privateScreenList = new ArrayList<String>();
	private Map<String,String>deviceNameIDMap=new HashMap<String,String>();
	private static SessionManager instance = null;
	//TODO: set or initialize these lists
	
	public static SessionManager getInstance()
	{
		if(instance==null)
			instance=new SessionManager();
		return instance;
	}
	
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
		getPublicScreenList().add(screenName);
	}
	public void addPrivateScreen(String screenName)
	{
		getPrivateScreenList().add(screenName);
	}
	public void removePublicScreen(String screenName)
	{
		getPublicScreenList().remove(screenName);
	}
	public void removePrivateScreen(String screenName)
	{
		getPrivateScreenList().remove(screenName);
	}
	public void removeAvailableSession(String sessionID)
	{
		availableSessions.remove(sessionID);
	}
	public void addAvailableSession(String sessionID)
	{
		availableSessions.add(sessionID);
	}

	public List<String> getPublicScreenList() {
		return publicScreenList;
	}

	public void setPublicScreenList(List<String> publicScreenList) {
		this.publicScreenList = publicScreenList;
	}

	public List<String> getPrivateScreenList() {
		return privateScreenList;
	}

	public void setPrivateScreenList(List<String> privateScreenList) {
		this.privateScreenList = privateScreenList;
	}
}
