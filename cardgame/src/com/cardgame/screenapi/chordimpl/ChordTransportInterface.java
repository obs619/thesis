package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.TransportInterface;
import com.samsung.android.sdk.chord.SchordChannel;


public class ChordTransportInterface implements TransportInterface {

	
	private static final String PAYLOAD_TYPE = "CHORD_SPS";
	private SchordChannel mChannel;
	/**
	 * Sends message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 */
	@Override
	public void sendToAll(Message message) {
		mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] {((ChordMessage) message).getBytes() });
	}
	
	
	/**
	 * Sends private message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 * @param userToSend
	 * 			  nodename of the user which the message will be sent
	 */
	public void send(String userToSend,Message message) {
		mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  ((ChordMessage) message).getBytes() });
	}


	
	
	
}
