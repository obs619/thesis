package com.cardgame.transport;

public class Delta {
	public static final int DELTA_CARD_DRAWN=0;
	public static final int DELTA_CARD_PLAYED=1;
	public static final int DELTA_CHANGE_TURN=2;
	public static final int DELTA_GAME_OVER=3;
	/**
	 * A delta sent to a specific player to inform it of which cards are allowed to be played. One of these should be sent to all players after every turn(?)
	 */
	public static final int DELTA_PLAYABLE_CARDS=4;
	
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
