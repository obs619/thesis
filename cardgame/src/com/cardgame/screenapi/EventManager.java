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
	static final EventManager instance=factory.createEventManager();
	//TODO optional global event queue
	//TODO optional shared hash table w/ distributed mutex
	public static EventManager getInstance()
	{
		return instance;//factory.createEventManager();
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
	public void triggerEvent(Event e)
	{
		if(e.getRecipient()!=Event.R_LOCAL_SCREEN)
			sendEvent(e);
		applyEvent(e);//apply event to yourself (if it affects you?)
	}
	public void applyEvent(Event e)
	{
		eventHandler.handleEvent(e);
	}
	
}
