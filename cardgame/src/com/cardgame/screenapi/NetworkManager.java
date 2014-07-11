package com.cardgame.screenapi;

public class NetworkManager {
	
	private static NetworkManager networkInitializer = null;
	static NetworkManagerFactory factory;
	//TODO optional global event queue
	//TODO optional shared hash table w/ distributed mutex
	public static NetworkManager getInstance()
	{
		return factory.createNetworkManager();
	}
	
	public static void setDefaultFactory(NetworkManagerFactory factory)
	{
		NetworkManager.factory=factory;
	}
	/*public static NetworkManager getInstance() 
	{
	    if(networkInitializer == null) {
	    	networkInitializer = new NetworkManager();
	      }
	      return networkInitializer;
	}*/
}
