package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.NetworkManager;
import com.cardgame.screenapi.NetworkManagerFactory;

public class ChordNetworkManagerFactory implements NetworkManagerFactory {

	@Override
	public NetworkManager createNetworkManager() {
		return new ChordNetworkManager();
	}

}
