package com.cardgame.gameengine.host;

import java.util.List;

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
	private List<User> permittedPlayers;
	/**
	 * Spectators (public screens) which can see this card's face.
	 */
	private List<User> permittedSpectators;

	
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
	public List<User> getPermittedPlayers() {
		return permittedPlayers;
	}
	public void setPermittedPlayers(List<User> permittedPlayers) {
		this.permittedPlayers = permittedPlayers;
	}
	
	public void addPermittedPlayer(User p){
		permittedPlayers.add(p);
	}
	public void removePermittedPlayer(User p){
		permittedPlayers.remove(p);
	}
}
