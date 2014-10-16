package com.llsx.pps.internal.chord;

import com.llsx.pps.network.NetworkManager;
import com.llsx.pps.network.NetworkManagerFactory;

public class ChordNetworkManagerFactory implements NetworkManagerFactory {

	@Override
	public NetworkManager createNetworkManager() {
		return new ChordNetworkManager();
	}

}