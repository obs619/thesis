package com.cardgame.screenapi;

public abstract class Message {
	/**Username of sending screen
	 * 
	 */
	//private String sender;
	
	/**
	 * Message type
	 */
	protected String mMessageType;
	
	/**Message contents
	 * 
	 */
	protected String mMessage;
	
	/**
	 * Not sure if recipient or sender
	 */
	protected String mUserName;
	
	//private MessageOwner mOwner;
	public String getRecipient()
	{
		return mUserName;
	}
}
