package com.cardgame.gameengine;

import java.util.List;

import com.cardgame.screenapi.Screen;

public class Area {
	private String name;
	private List<Area> subAreas;
	private List<Card> cards;
	
	/**
	 * Players (private screens) which can see the faces of cards in this area (but not in subareas).
	 */
	private List<Screen> permittedPlayers;
	/**
	 * Spectators (public screens) which can see the faces of cards in this area (but not in subareas).
	 */
	private List<Screen> permittedSpectators;
	
	public Area(String name)
	{
		this.name=name;
	}
	
	public Area(String name, List<Screen>permittedPlayers,List<Screen>permittedSpectators)
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
	public void addCard(Card card)
	{
		this.cards.add(card);
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
