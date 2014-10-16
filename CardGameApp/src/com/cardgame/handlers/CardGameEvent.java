package com.cardgame.handlers;

import com.llsx.pps.event.Event;

public class CardGameEvent extends Event {
	public CardGameEvent(String recipient, int type, String payload) {
		super(recipient, type, payload);
	}
	
	public static final int CARD_DRAW_REQUEST=0;
	public static final int CARD_PLAYED=1;

	public static final int START_GAME=2;
	public static final int DECK_DISTRIBUTE=3;
	public static final int PLAYER_NUM=4;
	public static final int ADJACENT_PLAYER=5;
	
	public static final int DRAW_RESPOND=6;
	public static final int OUT_OF_CARDS=7;
	public static final int CHANGE_NUM_PLAYERS=8;
	public static final int NOTIFY_HOST=9;
	public static final int NOTIFY_PLAYER_TURN=10;
	public static final int LOSE_PLAYER=11;
	
	
}
