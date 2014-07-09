package com.cardgame.screenapi;

import android.app.Application;

public class SPSApplication extends Application {
	private Screen screen;
	private EventManager eventManager;
	private NetworkInitializer networkInitializer; 
	
	public void setNetworkInitializer() {
		this.networkInitializer = NetworkInitializer.getInstance();
	}
	
	// getters and setters
	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}

	public NetworkInitializer getNetworkInitializer() {
		return networkInitializer;
	}

	
}
