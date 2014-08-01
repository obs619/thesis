package com.cardgame.screenapi;

import android.util.Log;

import com.cardgame.screenapi.chordimpl.ChordNetworkManagerFactory;

public class NetworkManager {
	
	private static NetworkManager networkInitializer = null;
	static NetworkManagerFactory factory;
	static NetworkManager instance;
	//TODO optional global event queue
	//TODO optional shared hash table w/ distributed mutex
	public static NetworkManager getInstance()
	{
		if(networkInitializer==null)
			networkInitializer= factory.createNetworkManager();
		return networkInitializer;
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
