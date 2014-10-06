package com.cardgame.screenapi.messaging;

import com.cardgame.screenapi.event.Event;

public interface MessageBuilder {

	public Message buildMessage(Event event);
	public Event unpackEvent(Message message);
	
}
