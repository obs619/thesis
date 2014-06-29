package com.cardgame.host;

import java.util.List;

public class Area {
	private List<Area> subAreas;
	private List<Card> cards;
	/**
	 * Players (private screens) which can see the faces of cards in this area.
	 */
	private List<User> permittedPlayers;
	/**
	 * Spectators (public screens) which can see the faces of cards in this area.
	 */
	private List<User> permittedSpectators;
	
	public List<Area> getSubAreas() {
		return subAreas;
	}
	public void setSubAreas(List<Area> subAreas) {
		this.subAreas = subAreas;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
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
