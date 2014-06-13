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
package com.srpol.chordchat.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Holder for chat message. Consists of message and owner variable.
 */
public class ChatMessage implements Serializable {

	private static final long serialVersionUID = 20130219L;

	private final String mMessage;
	private final String mUserName;
	private MessageOwner mOwner;

	private ChatMessage(String message, String userName, MessageOwner owner) {
		mMessage = message;
		mUserName = userName;
		mOwner = owner;
	}

	/**
	 * Returns a new chat message instance.
	 * 
	 * @param message
	 * @param owner
	 * @return {@link ChatMessage} instance
	 */
	public static ChatMessage obtain(String message, String userName, MessageOwner owner) {
		return new ChatMessage(message, userName, owner);
	}

	/**
	 * Copy constructor for a chat message.
	 * 
	 * @param chatMessage
	 * @return new {@link ChatMessage} instance.
	 */
	public static ChatMessage obtain(ChatMessage chatMessage) {
		return new ChatMessage(chatMessage.mMessage, chatMessage.mUserName, chatMessage.mOwner);
	}

	public String getMessage() {
		return mMessage;
	}

	public MessageOwner getOwner() {
		return mOwner;
	}

	public String getUserName() {
		return mUserName;
	}

	/**
	 * Swaps the owner of the message.
	 */
	public void changeOwner() {
		mOwner = mOwner == MessageOwner.YOU ? MessageOwner.STRANGER : MessageOwner.YOU;
	}

	/**
	 * Returns {@link ChatMessage} in the form of byte array.
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
	 * Recreates {@link ChatMessage} from the byte array.
	 * 
	 * @param data
	 *            byte array representing {@link ChatMessage}
	 * @return
	 */
	public static ChatMessage obtainChatMessage(byte[] data) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final ObjectInputStream is;
		ChatMessage message = null;

		try {
			is = new ObjectInputStream(in);
			message = (ChatMessage) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return message;
	}

	@Override
	public String toString() {
		return "ChatMessage [mMessage=" + mMessage + ", mUserName=" + mUserName + ", mOwner=" + mOwner + "]";
	}

	/**
	 * Indicates the owner of the message.
	 */
	public static enum MessageOwner {
		YOU, STRANGER;
	}

}
