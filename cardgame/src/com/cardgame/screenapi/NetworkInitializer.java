package com.cardgame.screenapi;

public class NetworkInitializer {
	
	private static NetworkInitializer networkInitializer = null;
	
	public static NetworkInitializer getInstance() 
	{
	    if(networkInitializer == null) {
	    	networkInitializer = new NetworkInitializer();
	      }
	      return networkInitializer;
	}
}
