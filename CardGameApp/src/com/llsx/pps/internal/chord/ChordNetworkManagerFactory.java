package com.llsx.pps.internal.chord;

import com.llsx.pps.network.NetworkManager;
import com.llsx.pps.network.NetworkManagerFactory;

/**
 * Concrete implementation of NetworkManagerFactory using Samsung Chord API
 * @author Andrew
 *
 */
public class ChordNetworkManagerFactory implements NetworkManagerFactory {

	/**
	 * @return a new instance of ChordNetworkManager
	 */
	@Override
	public NetworkManager createNetworkManager() {
		return new ChordNetworkManager();
	}

}
