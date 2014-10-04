package com.cardgame.screenapi.chordimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChordMessage extends com.cardgame.screenapi.messaging.Message implements Serializable {

	private static final long serialVersionUID = 20130219L;

	ChordMessage(Serializable message, String recipients, int messageType) {
		mMessage = message;
		mRecipients = recipients;
		mMessageType = messageType;
	}
	
	ChordMessage(Serializable message, String recipients, int messageType, int teamNo) {
		mMessage = message;
		mRecipients = recipients;
		mMessageType = messageType;
		this.teamNo = teamNo;
	}
	
	ChordMessage(Serializable message, String recipients, int messageType, boolean isAPIEvent) {
		mMessage = message;
		mRecipients = recipients;
		mMessageType = messageType;
		this.isAPIEvent = isAPIEvent;
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
