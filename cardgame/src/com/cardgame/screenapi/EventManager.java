package com.cardgame.screenapi;
/**
 * Builds and sends a message from an event
 * @author Andrew
 *
 */
public class EventManager {
	MessageBuilder messageBuilder;
	MessageDispatcher messageDispatcher;
	EventHandler eventHandler;
	static EventManagerFactory factory;
	
	public static EventManager getInstance()
	{
		return factory.createEventManager();
	}
	
	public static void setDefaultFactory(EventManagerFactory factory)
	{
		EventManager.factory=factory;
	}
	
	public EventManager(MessageBuilder messageBuilder, MessageDispatcher messageDispatcher)
	{
		this.messageBuilder=messageBuilder;
		this.messageDispatcher=messageDispatcher;
	}
	public void sendEvent(Event e)
	{
		Message m=messageBuilder.buildMessage(e);
		messageDispatcher.sendMessage(m);
	}
	public void unpackEvent(Message m)
	{
		Event e=messageBuilder.unpackEvent(m);
		applyEvent(e);
	}
	public void setEventHandler(EventHandler h)
	{
		this.eventHandler=h;
	}
	public void applyEvent(Event e)
	{
		eventHandler.handleEvent(e);
	}
	
}
