package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	@Override
	public Message buildMessage(Event e) {
		return new ChordMessage(e.getPayload(), e.getRecipient(), e.getType(), e.isAPIEvent());
	}

	@Override
	public Event unpackEvent(Message m) {
		return new Event(m.getRecipient(), m.getType(), m.getContents(), m.isAPIEvent());
	}

}
