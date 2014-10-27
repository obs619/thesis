package com.llsx.pps.internal.chord;

import com.llsx.pps.event.Event;
import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	/**
	 * @return Chord Message equivalent of the event
	 */
	@Override
	public Message buildMessage(Event event) {
		return new ChordMessage(event.getPayload(), event.getRecipient(), event.getType(), event.isPpsEvent());
	}

	/**
	 * @return Event equivalent of the message
	 */
	@Override
	public Event unpackEvent(Message message) {
		return new Event(message.getRecipient(), message.getType(), message.getContents(), message.isPpsEvent());
	}

}
