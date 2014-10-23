package com.llsx.pps.messaging;

import java.io.Serializable;

public abstract class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;

	protected int mMessageType;
	protected Serializable mMessage;
	protected String mRecipients;
	protected boolean isAPIEvent;

	/**
	 * 
	 * @return type of message corresponds to type of event
	 */
	public int getType() {
		return mMessageType;
	}
	
	/**
	 * 
	 * @return serializable actual message
	 */
	public Serializable getContents() {
		return mMessage;
	}
	
	/**
	 * 
	 * @return recipient of the message
	 */
	public String getRecipient() {
		return mRecipients;
	}
	
	/**
	 * 
	 * @return Return <code>true</code> if the message is for API event, <code>false</code> if the message is for Application event. 
	 */
	public boolean isAPIEvent() {
		return isAPIEvent;
	}

}
