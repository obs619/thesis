package com.cardgame.screenapi;

import java.io.Serializable;

public abstract class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Message type
	 */
	protected int mMessageType;
	
	/**Message contents
	 * 
	 */
	protected Serializable mMessage;
	
	protected String sessionID;
	protected String mRecipients;
	protected boolean isAPIEvent;

	public int getType() {
		return mMessageType;
	}
	
	public Serializable getContents() {
		return mMessage;
	}
	
	public String getRecipient(){
		return mRecipients;
	}
	
	public boolean isAPIEvent() {
		return isAPIEvent;
	}
	
	public void setAPIEvent(boolean isAPIEvent) {
		this.isAPIEvent=isAPIEvent;
	}
	

	
}
