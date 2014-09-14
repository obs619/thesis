package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	@Override
	public Message buildMessage(Event e) {
		return new ChordMessage(e.getPayload(),e.getRecipient(),e.getSource(),e.getType(),e.isAPIEvent());
	}

	@Override
	public Event unpackEvent(Message m) {
		return new Event(m.getSource(),m.getRecipient(),m.getType(),m.getContents(),m.isAPIEvent());
	}
	

}
