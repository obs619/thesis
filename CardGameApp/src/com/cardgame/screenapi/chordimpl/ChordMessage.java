/*
 ********************************************************************************
 * Copyright (c) 2013 Samsung Electronics, Inc.
 * All rights reserved.
 *
 * This software is a confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung Electronics.
 ********************************************************************************
 */
package com.cardgame.screenapi.chordimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.util.Log;

/**
 * Holder for chat message. Consists of message and owner variable.
 */
public class ChordMessage extends com.cardgame.screenapi.Message implements Serializable {

	private static final long serialVersionUID = 20130219L;

	//private final String mMessage;
	//private final String mUserName;
	//private MessageOwner mOwner;
	//private String mMessageType;
	
	
	ChordMessage(String message, String recipients, String source, int messageType) {
		mMessage = message;
		mRecipients = recipients;
		mSource=source;
		mMessageType = messageType;
	}
	ChordMessage(String message, String recipients, String source, int messageType, boolean isAPIEvent) {
		mMessage = message;
		mRecipients = recipients;
		mSource=source;
		mMessageType = messageType;
		this.isAPIEvent=isAPIEvent;
	}

	/**
	 * Returns a new chat message instance.
	 * 
	 * @param message
	 * @param owner
	 * @return {@link ChordMessage} instance
	 */
	public static ChordMessage obtain(String message, String userName, String source, int messageType) {
		return new ChordMessage(message, userName, source, messageType);
	}

	/**
	 * Copy constructor for a chat message.
	 * 
	 * @param chatMessage
	 * @return new {@link ChordMessage} instance.
	 */
	public static ChordMessage obtain(ChordMessage chatMessage) {
		return new ChordMessage(chatMessage.mMessage, chatMessage.mRecipients, chatMessage.getSource(), chatMessage.mMessageType);
	}

	public String getMessage() {
		return mMessage;
	}

	

	public String getUserName() {
		return mRecipients;
	}
	
	public int getMessageType() {
		return mMessageType;
	}

	/**
	 * Swaps the owner of the message.
	 */
	/*public void changeOwner() {
		mOwner = mOwner == MessageOwner.YOU ? MessageOwner.STRANGER : MessageOwner.YOU;
	}*/

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
			//throw new RuntimeException(e);
		}
		
		Log.e("obtain", message.mSource + "");
		return message;
	}

	@Override
	public String toString() {
		return "ChatMessage [mMessage=" + mMessage + ", mUserName=" + mRecipients + ", mOwner=" + getSource() + "]";
	}

	/**
	 * Indicates the owner of the message.
	 */
	public static enum MessageOwner {
		YOU, STRANGER;
	}

}
