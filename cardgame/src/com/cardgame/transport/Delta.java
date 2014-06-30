package com.cardgame.transport;

public class Delta {
	/**
	 * A delta containing all information about current state of game world. Sent on start of game or if a player desyncs.
	 */
	public static final int DELTA_WORLDVIEW=0;
	public static final int DELTA_CARD_DRAWN=1;
	public static final int DELTA_CARD_PLAYED=2;
	public static final int DELTA_CHANGE_TURN=3;
	/**
	 * A delta sent to a specific player to inform it of which cards are allowed to be played. One of these should be sent to all players after every turn(?)
	 */
	public static final int DELTA_PLAYABLE_CARDS=4;
	/**
	 * A delta sent when the host has reshuffled the cards in the deck/discard pile.
	 */
	public static final int DELTA_DECK_RESHUFFLED=5;
	public static final int DELTA_GAME_OVER=6;

	
	private int type;
	private String details;
	public Delta(int type, String details)
	{
		this.setType(type);
		this.setDetails(details);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
