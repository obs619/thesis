package com.llsx.pps.network;

/**
 * Takes care of the link between the app and
 * the available network connections.
 * 
 * @author Amanda
 */
public class NetworkManager {
	
	private static NetworkManager instance = null;
	private static NetworkManagerFactory factory;

	/**
	 * @return The current instance of <code>NetworkManager</code>
	 * if it exists; otherwise a new instance of
	 * <code>NetworkManager</code>.
	 */
	public static NetworkManager getInstance() {
		if(instance == null)
			instance = factory.createNetworkManager();
		return instance;
	}
	
	/**
	 * Sets the given factory as the new default factory.
	 * @param factory the factory to be used
	 */
	public static void setDefaultFactory(NetworkManagerFactory factory) {
		NetworkManager.factory = factory;
	}
	
}
