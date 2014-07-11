package com.cardgame.screenapi;

public class NetworkManager {
	
	private static NetworkManager networkInitializer = null;
	public static NetworkManager getInstance() 
	{
	    if(networkInitializer == null) {
	    	networkInitializer = new NetworkManager();
	      }
	      return networkInitializer;
	}
}
