package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.network.NetworkManager;
import com.cardgame.screenapi.network.NetworkManagerFactory;

public class ChordNetworkManagerFactory implements NetworkManagerFactory {

	@Override
	public NetworkManager createNetworkManager() {
		return new ChordNetworkManager();
	}

}
