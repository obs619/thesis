package com.llsx.pps.messaging;


public interface TransportInterface {
	
	/**
	 * Actual sending of the message to its corresponding
	 * recipient device.
	 * @param recipient intended recipient of the message
	 * @param message the message to be sent
	 * @param isCustomChannel whether the message should be
	 * sent to a custom channel (<code>true</code>), or to
	 * the default channel (<code>false</code>)
	 */
	public void send(String recipient, Message message, boolean isCustomChannel);
	
	/**
	 * Actual sending of the message to all devices in
	 * the same network/channel/session.
	 * @param message the message to be sent
	 * @param isCustomChannel whether the message should be
	 * sent to a custom channel (<code>true</code>), or to
	 * the default channel (<code>false</code>)
	 */
	public void sendToAll(Message message, boolean isCustomChannel);
	
	/**
	 * Sets the message dispatcher. In this case, is the
	 * ChordMessageDispatcher.
	 * @param dispatcher the message dispatcher to be used 
	 */
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
