package com.cardgame.screenapi;



public class SPSManager {
	private Screen screen;
	private EventManager eventManager;
	private NetworkInitializer networkInitializer; 

	/**
	 * Select which implementation you want (Chord in this case)
	 */
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

	public void setEventManager() {
		this.eventManager = EventManager.getInstance();
	}

	public NetworkInitializer getNetworkInitializer() {
		return networkInitializer;
	}


	
}