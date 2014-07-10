package com.cardgame.screenapi;

import java.util.List;

import android.content.Context;



public class SPSManager {
	/**
	 * screen of this device
	 */
	private Screen screen;
	
	private List<String>sharedScreens;
	private List<String>personalScreens;
	//TODO: set or initialize these lists
	
	private EventManager eventManager;
	private NetworkManager networkInitializer; 

	private static Context mContext;
	
	public SPSManager(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * Select which implementation you want (Chord in this case)
	 */
	public void setNetworkInitializer() {
		this.networkInitializer = NetworkManager.getInstance();
	}
	
	// getters and setters
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
	public static Context getContext() {
		return mContext;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager() {
		this.eventManager = EventManager.getInstance();
	}

	public NetworkManager getNetworkInitializer() {
		return networkInitializer;
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
