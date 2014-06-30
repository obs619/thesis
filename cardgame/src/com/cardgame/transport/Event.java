package com.cardgame.transport;

public class Event {
	public static final int EVENT_DRAW_CARD=0;
	public static final int EVENT_PLAY_CARD=1;
	public static final int EVENT_PASS=2;
	
	private int type;
	private String sender;
	private String details;
	
	public Event(String sender, int type, String details)
	{
		this.sender=sender;
		this.type=type;
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

	public String getSenderName() {
		// TODO Auto-generated method stub
		return sender;
	}
}
