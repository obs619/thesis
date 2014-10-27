package com.llsx.pps.internal.chord;

import com.llsx.pps.event.Event;
import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.MessageBuilder;

/**
 * Concrete implementation of MessageBuilder using the Samsung Chord API
 * @author Andrew
 *
 */
public class ChordMessageBuilder implements MessageBuilder {

	/**
	 * @return ChordMessage equivalent of the Event
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
