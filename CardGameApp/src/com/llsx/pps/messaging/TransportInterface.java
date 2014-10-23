package com.llsx.pps.messaging;


public interface TransportInterface {
	
	/**
	 * Actual sending of the message to its corresponding recipient channel.
	 * @param recipient recipient of the message
	 * @param message actual message
	 * @param isCustomChannel custom channel if it is <code>true</code>, else default channel
	 */
	public void send(String recipient, Message message, boolean isCustomChannel);
	
	/**
	 * Actual sending of message to all devices in the corresponding channel.
	 * @param message actual message
	 * @param isCustomChannel custom channel if it is <code>true</code>, else default channel
	 */
	public void sendToAll(Message message, boolean isCustomChannel);
	
	/**
	 * Sets the message dispatcher. In this case, is the ChordMessageDispatcher.
	 * @param dispatcher actual message to be used. 
	 */
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
