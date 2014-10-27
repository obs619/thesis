package com.llsx.pps.messaging;

import java.io.Serializable;

/**
 * Represents the actual message being sent
 * between devices.
 * @author Amanda
 */
public abstract class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;

	protected int mMessageType;
	protected Serializable mMessage;
	protected String mRecipients;
	protected boolean isPpsEvent;

	/**
	 * @return The type of message corresponding
	 * to the type of event.
	 */
	public int getType() {
		return mMessageType;
	}
	
	/**
	 * @return A <code>Serializable</code> containing
	 * the actual contents of the message.
	 */
	public Serializable getContents() {
		return mMessage;
	}
	
	/**
	 * @return The intended recipient of
	 * the message.
	 */
	public String getRecipient() {
		return mRecipients;
	}
	
	/**
	 * @return <code>true</code> if the message
	 * is for PPS API Events; <code>false</code> if
	 * the message is for Application Events. 
	 */
	public boolean isPpsEvent() {
		return isPpsEvent;
	}

}
