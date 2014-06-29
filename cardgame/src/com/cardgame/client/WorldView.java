package com.cardgame.client;

import com.cardgame.host.Area;

/**
 * Container for objects as seen by client.
 * @author Andrew
 *
 */
public class WorldView {
	private Area rootArea;

	public Area getRootArea() {
		return rootArea;
	}

	public void setRootArea(Area rootArea) {
		this.rootArea = rootArea;
	}
}
