
package com.cardgame.screenapi.chordimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.util.Log;

public class ChordMessage extends com.cardgame.screenapi.Message implements Serializable {

	private static final long serialVersionUID = 20130219L;

	ChordMessage(Serializable message, String recipients, int messageType) {
		mMessage = message;
		mRecipients = recipients;
		mMessageType = messageType;
	}
	
	ChordMessage(Serializable object, String recipients, int messageType, boolean isAPIEvent) {
		mMessage = object;
		mRecipients = recipients;
		mMessageType = messageType;
		this.isAPIEvent=isAPIEvent;
	}

	public static ChordMessage obtain(String message, String userName, int messageType) {
		return new ChordMessage(message, userName, messageType);
	}

	public static ChordMessage obtain(ChordMessage chordMessage) {
		return new ChordMessage(chordMessage.mMessage, chordMessage.mRecipients, chordMessage.mMessageType);
	}

	public Serializable getMessage() {
		return mMessage;
	}

	public String getUserName() {
		return mRecipients;
	}
	
	public int getMessageType() {
		return mMessageType;
	}

	/**
	 * Returns {@link ChordMessage} in the form of byte array.
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
	 *            byte array representing {@link ChordMessage}
	 * @return
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
