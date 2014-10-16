package com.llsx.pps.messaging;

import com.llsx.pps.event.Event;

public interface MessageBuilder {

	public Message buildMessage(Event event);
	public Event unpackEvent(Message message);
	
}
