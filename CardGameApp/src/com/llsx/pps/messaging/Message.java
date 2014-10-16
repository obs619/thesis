package com.llsx.pps.messaging;

import java.io.Serializable;

public abstract class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int mMessageType;
	protected int teamNo;
	protected Serializable mMessage;
	protected String mRecipients;
	protected boolean isAPIEvent;

	public int getType() {
		return mMessageType;
	}
	
	public int getTeamNo() {
		return teamNo;
	}
	
	public Serializable getContents() {
		return mMessage;
	}
	
	public String getRecipient() {
		return mRecipients;
	}
	
	public boolean isAPIEvent() {
		return isAPIEvent;
	}

}
