package com.llsx.pps.network;

public interface NetworkManagerFactory {
	
	/**
	 * Creates and initializes a new <code>NetworkManager</code>.
	 * @return The initialized <code>NetworkManager</code>.
	 */
	public NetworkManager createNetworkManager();
	
}
