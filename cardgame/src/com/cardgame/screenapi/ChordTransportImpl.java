package com.cardgame.screenapi;


public class ChordTransportImpl implements TransportInterface {

	/**
	 * Sends message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 */
	@Override
	public void sendToAll(Message message) {
		//mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] { message.getBytes() });
	}
	
	
	/**
	 * Sends private message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 * @param userToSend
	 * 			  nodename of the user which the message will be sent
	 */
	public void send(Message message, String userToSend) {
		//mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  message.getBytes() });
	}


	
	
	
}
