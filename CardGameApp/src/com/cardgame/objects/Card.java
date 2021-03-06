package com.cardgame.objects;

import java.io.Serializable;

public class Card implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;
	private int suit;
	public static final int DIAMONDS=4;
	public static final int HEARTS=3;
	public static final int SPADES=2;
	public static final int CLUBS=1;
	
	private boolean selected = false;
	
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
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setSuit(int suit) {
		this.suit = suit;
	}
	
	public String getSuitAsString() {
		String sSuit = null;
		switch (this.suit) {
			case 4:	sSuit = "Diamonds"; break;
			case 3:	sSuit = "Hearts"; break;
			case 2:	sSuit = "Spades"; break;
			case 1:	sSuit = "Clubs"; break;
			default: sSuit = "Unknown";
		}
		return sSuit;
	}
	
	@Override
	public String toString() {
		return this.number + " of " + getSuitAsString();
	}
}
