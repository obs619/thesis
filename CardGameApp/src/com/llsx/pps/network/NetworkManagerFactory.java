package com.llsx.pps.network;

/**
 * Used to create a NetworkManager.
 * 
 * @author Amanda
 */
public interface NetworkManagerFactory {
	
	/**
	 * Creates and initializes a new <code>NetworkManager</code>.
	 * @return The initialized <code>NetworkManager</code>.
	 */
	public NetworkManager createNetworkManager();
	
}
