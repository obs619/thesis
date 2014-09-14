package com.cardgame.handlers;

import com.cardgame.screenapi.Event;

public class CardGameEvent extends Event {
	public CardGameEvent(String source, String recipient, int type,
			String payload) {
		super(source, recipient, type, payload);
		
	}
	
	public CardGameEvent(String source, int type, String payload)
	{
		//certain events seem to have pre-determined recipients, we could define them here based on type
	}
	
	/**for Monkey Monkey, draw card from adjacent player
	 * RECIPIENT: player on left; event must be triggered on remote screen before local screen
	 */
	public static final int CARD_DRAWN=0;
	public static final int CARD_PLAYED=1;
	public static final int REQUEST_WORLD=2;
	/**
	 * A delta containing all information about current state of game world. Sent on start of game or if a player desyncs.
	 */
	public static final int SEND_WORLDVIEW=4;

	public static final int TURN_OVER=5;
	/**
	 * A delta sent to a specific player to inform it of which cards are allowed to be played. One of these should be sent to all players after every turn(?)
	 */
	public static final int PLAYABLE_CARDS_CHANGED=6;
	
	public static final int GAME_OVER=7;
	
	/**
	 * A delta sent when the host has reshuffled the cards in the deck/discard pile. Not needed for Monkey Monkey
	 */
	public static final int DECK_RESHUFFLED=8;
	public static final int START_GAME=9;
	
}
