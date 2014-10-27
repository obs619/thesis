package com.llsx.pps.internal.chord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Concrete implementation of Message using the Samsung Chord API. Implements <code>Serializable</code> to allow the message to contain Objects.
 * @author Andrew
 *
 */
public class ChordMessage extends com.llsx.pps.messaging.Message implements Serializable {

	/**Chord version number?
	 * 
	 */
	private static final long serialVersionUID = 20130219L;

	/**
	 * Constructs a new ChordMessage with the given parameters
	 * @param message the actual message being sent/received
	 * @param recipients the intended recipient of the event
	 * @param messageType the message type represented by an integer
	 * @param isPpsEvent boolean indicating whether the event is for the PPS or user
	 */
	ChordMessage(Serializable message, String recipients, int messageType, boolean isPpsEvent) {
		mMessage = message;
		mRecipients = recipients;
		mMessageType = messageType;
		this.isPpsEvent = isPpsEvent;
	}

	/**
	 * Returns {@link ChordMessage} in the form of byte array.
	 * @return byte array equivalent of the ChordMessage
	 */
	public byte[] getBytes() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectOutputStream os;

		try {
			os = new ObjectOutputStream(out);
			os.writeObject(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toByteArray();
	}

	/**
	 * Recreates {@link ChordMessage} from the byte array.
	 * 
	 * @param data
	 *      byte array representing {@link ChordMessage}
	 * @return the original ChordMessage in readable form
	 */
	public static ChordMessage obtainChatMessage(byte[] data) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final ObjectInputStream is;
		ChordMessage message = null;

		try {
			is = new ObjectInputStream(in);
			message = (ChordMessage) is.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}

}
