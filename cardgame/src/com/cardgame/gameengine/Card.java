package com.cardgame.gameengine;

import java.util.List;

import com.cardgame.screenapi.Screen;

public class Card {
	private int number;
	private int suit;
	public static final int DIAMONDS=4;
	public static final int HEARTS=3;
	public static final int SPADES=2;
	public static final int CLUBS=1;
	/**
	 * Players (private screens) which can see this card's face.
	 */
	private List<Screen> permittedPlayers;
	/**
	 * Spectators (public screens) which can see this card's face.
	 */
	private List<Screen> permittedSpectators;

	
	public Card (int number, int suit){
		setNumber(number);
		setSuit(suit);
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getSuit() {
		return suit;
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}
	public List<Screen> getPermittedPlayers() {
		return permittedPlayers;
	}
	public void setPermittedPlayers(List<Screen> permittedPlayers) {
		this.permittedPlayers = permittedPlayers;
	}
	
	public void addPermittedPlayer(Screen p){
		permittedPlayers.add(p);
	}
	public void removePermittedPlayer(Screen p){
		permittedPlayers.remove(p);
	}
}
