package com.cardgame.screenapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {
	
	private String sessionID;//auto-generate
	private String sessionKey;//auto-generate?
	private boolean isOpen=true;
	
	private static List<String>availableSessions = new ArrayList<String>();
	private static List<String>publicScreenList = new ArrayList<String>();//<name, sessionID>
	private static List<String>privateScreenList = new ArrayList<String>();
	
	private Map<String,String>deviceNameIDMap=new HashMap<String,String>();
	
	private static boolean isPersonal;
	private static boolean sessionMode = true;
	
	private String chosenSession = "";
	
	private static SessionManager instance = null;

	public static SessionManager getInstance() {
		if(instance==null) {
			instance=new SessionManager();
		}
		return instance;
	}
	
	public void requestToJoin() {
		
	}
	
	public void joinSession(String sessionID) {
		this.sessionID=sessionID;
		broadcastSessionID();
		
	}
	
	/**Called on receive of the message...
	 * 
	 * @param screenName
	 */
	
	public void onScreenJoined(String screenName) {
		//TODO: refine parameters?
		//add screen to list of either shared or private screens
		//send message to screen containing the session key
	}
	
	public void createSession() {
		//generate new sessionID/key and broadcast to other devices
	}
	
	public void leaveSession() {
		//leave this session. similar to Screen.close()?
	}
	
	private void broadcastSessionID() {
		//TODO broadcast new sessionID
	}
	
	/**
	 * allow other devices to join this session
	 */
	public void openSession() {
		isOpen=true;
	}
	/**prevent other devices from joining this session
	 * 
	 */
	public void lockSession() {
		isOpen=false;
	}
	
	public boolean IsSessionOpen() {
		return isOpen;
	}
	// add functions
	public void addPublicScreen(String screenName){
		publicScreenList.add(screenName);
	}
	
	public void addPrivateScreen(String screenName){
		privateScreenList.add(screenName);
	}
	
	public void addAvailableSession(String sessionID){
		availableSessions.add(sessionID);
	}
	
	//remove functions
	public void removePublicScreen(String screenName){
		publicScreenList.remove(screenName);
	}
	
	public void removePrivateScreen(String screenName){
		privateScreenList.remove(screenName);
	}
	
	public void removeAvailableSession(String sessionID){
		availableSessions.remove(sessionID);
	}
	
	//getters
	public List<String> getPublicScreenList() {
		return publicScreenList;
	}
	
	public List<String> getPrivateScreenList() {
		return privateScreenList;
	}
	
	public List<String> getAvailableSessionsList() {
		return availableSessions;
	}
	
	public boolean isPersonal() {
		return isPersonal;
	}
	
	public boolean isSessionMode() {
		return sessionMode;
	}
	
	public String getChosenSession() {
		return chosenSession;
	}
	
	//setters
	public void setPublicScreenList(List<String> publicScreenList) {
		this.publicScreenList = publicScreenList;
	}
	
	public void setPrivateScreenList(List<String> privateScreenList) {
		this.privateScreenList = privateScreenList;
	}
	
	public void setScreenType(boolean screenType) {
		this.isPersonal = screenType;
	}
	
	public void setSessionMode(boolean sessionMode) {
		this.sessionMode = sessionMode;
	}
	
	public void setChosenSession(String session) {
		this.chosenSession = session;
	}
	
	//clear functions
	public void clearPrivateScreenList(){
		privateScreenList.clear();
	}
	
	public void clearPublicScreenList(){
		publicScreenList.clear();
	}
	
	public void clearAvailableSessionsList(){
		availableSessions.clear();
	}
	
	public void sendNewSessionNotification(String sessionID) {
		
		addAvailableSession(sessionID);
		Event e=new Event(Event.R_ALL_SCREENS
				,Event.ADD_NEW_SESSION
				,sessionID,true);
		EventManager.getInstance().sendEvent(e);
		
		Event e1=new Event(Event.R_ALL_SCREENS
				,Event.ADD_NEW_SESSION
				,sessionID,false);
		EventManager.getInstance().sendEvent(e1);
	}

}
