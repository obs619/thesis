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
	protected String mSource;
	protected boolean isAPIEvent;
	
	public String getRecipient(){
		return mRecipients;
	}

	public String getSource() {
		return mSource;
	}

	public Serializable getContents() {
		return mMessage;
	}
	public int getType() {
		return mMessageType;
	}
	
	public void setAPIEvent(boolean isAPIEvent) {
		this.isAPIEvent=isAPIEvent;
	}
	
	public boolean isAPIEvent() {
		return isAPIEvent;
	}
	
}
