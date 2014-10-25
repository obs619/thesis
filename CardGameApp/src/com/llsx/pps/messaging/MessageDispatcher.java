package com.llsx.pps.messaging;


/**
 * Handles sending and receiving messages.
 * @author Amanda
 */
public interface MessageDispatcher {

	/**
	 * Sends out the message through the transport interface.
	 * @param message the message to be sent
	 * @param isCustomChannel whether the message should be
	 * sent to a custom channel (<code>true</code>), or to
	 * the default channel (<code>false</code>)
	 */
	public void sendMessage(Message message, boolean isCustomChannel);
	
	/**
	 * Triggers when the device receives a message.
	 * @param message the message that was received
	 */
	public void receiveMessage(Message message);
	
	/**
	 * Sets the transport interface that will be used for
	 * transferring messages. In this case, it is the
	 * ChordTransportInterface.
	 * @param transportInterface transport interface to be used
	 */
	public void setTransportInterface(TransportInterface transportInterface);

}
