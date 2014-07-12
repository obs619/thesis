package com.cardgame.screenapi;

public interface MessageBuilder {

	public Message buildMessage(Event e) ;
	public Event unpackEvent(Message m);
}
