package com.llsx.pps.network;

public class NetworkManager {
	
	private static NetworkManager instance = null;
	private static NetworkManagerFactory factory;

	public static NetworkManager getInstance() {
		if(instance == null)
			instance = factory.createNetworkManager();
		return instance;
	}
	
	public static void setDefaultFactory(NetworkManagerFactory factory) {
		NetworkManager.factory = factory;
	}
	
}
