package com.cardgame.screenapi.chordimpl;

import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.MessageBuilder;

public class ChordMessageBuilder implements MessageBuilder {

	@Override
	public Message buildMessage(Event e) {
		// TODO Auto-generated method stub
		return new ChordMessage(e.getPayload(),e.getRecipient(),e.getSource(),e.getType());
	}

	@Override
	public Event unpackEvent(Message m) {
		// TODO Auto-generated method stub
		return new Event(m.getSource(),m.getRecipient(),m.getType(),m.getContents());
	}
	

}
