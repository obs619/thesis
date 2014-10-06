package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	@Override
	public Message buildMessage(Event event) {
		return new ChordMessage(event.getPayload(), event.getRecipient(), event.getType(), event.isAPIEvent());
	}

	@Override
	public Event unpackEvent(Message message) {
		return new Event(message.getRecipient(), message.getType(), message.getContents(), message.isAPIEvent());
	}

}
