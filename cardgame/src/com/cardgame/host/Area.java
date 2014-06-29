package com.cardgame.host;

import java.util.List;

public class Area {
	private String name;
	private List<Area> subAreas;
	private List<Card> cards;
	
	/**
	 * Players (private screens) which can see the faces of cards in this area (but not in subareas).
	 */
	private List<User> permittedPlayers;
	/**
	 * Spectators (public screens) which can see the faces of cards in this area (but not in subareas).
	 */
	private List<User> permittedSpectators;
	
	public Area(String name)
	{
		this.name=name;
	}
	
	public Area(String name, List<User>permittedPlayers,List<User>permittedSpectators)
	{
		this.name=name;
		this.permittedPlayers=permittedPlayers;
		this.permittedSpectators=permittedSpectators;
	}
	
	public List<Area> getSubAreas() {
		return subAreas;
	}
	public void setSubAreas(List<Area> subAreas) {
		this.subAreas = subAreas;
	}
	public void addSubArea(Area subArea)
	{
		subAreas.add(subArea);
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
