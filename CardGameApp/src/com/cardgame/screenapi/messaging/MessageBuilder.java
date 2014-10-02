package com.cardgame.screenapi.messaging;

import com.cardgame.screenapi.event.Event;

public interface MessageBuilder {

	public Message buildMessage(Event e);
	public Event unpackEvent(Message m);
	
}
