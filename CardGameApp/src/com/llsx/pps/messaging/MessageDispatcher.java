package com.llsx.pps.messaging;


public interface MessageDispatcher {

	/**
	 * Sends out the message through transport interface.
	 * @param message actual message
	 * @param isCustomChannel sends the message to custom channel if it is <code>true</code>, else sends to default channel
	 */
	public void sendMessage(Message message, boolean isCustomChannel);
	
	/**
	 * Triggers when the device receives a message.
	 * @param message received message
	 */
	public void receiveMessage(Message message);
	
	/**
	 * Sets the transport interface that will be used for message transferring. In this case, is the ChordTransportInterface.
	 * @param transportInterface actual transport interface to be used.
	 */
	public void setTransportInterface(TransportInterface transportInterface);

}
