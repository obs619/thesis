package com.cardgame.screenapi;

public abstract class Message {
	/**Username of sending screen
	 * 
	 */
	//private String sender;
	
	/**
	 * Message type
	 */
	protected int mMessageType;
	
	/**Message contents
	 * 
	 */
	protected String mMessage;
	
	/**
	 * Not sure if recipient or sender
	 */
	protected String mRecipients;
	private String mSource;
	
	//private MessageOwner mOwner;
	public String getRecipient()
	{
		return mRecipients;
	}

	public String getSource() {
		return mSource;
	}

	public String getContents()
	{
		return mMessage;
	}
	public int getType()
	{
		return mMessageType;
	}
	
}
