package com.cardgame.screenapi;

import android.util.Log;

import com.cardgame.screenapi.chordimpl.ChordEventManagerFactory;

/**
 * Builds and sends a message from an event
 * @author Andrew
 *
 */
public class EventManager {
	MessageBuilder messageBuilder;
	MessageDispatcher messageDispatcher;
	EventHandler eventHandler;
	APIEventHandler apiEventHandler=new APIEventHandler();
	public static EventManagerFactory factory;
	public static EventManager instance;
	//TODO optional global event queue
	//TODO optional shared hash table w/ distributed mutex
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
	public void triggerEvent(Event e)
	{
		if(e.getRecipient()!=Event.R_LOCAL_SCREEN)
			sendEvent(e);
		applyEvent(e);//apply event to yourself (if it affects you?)
	}
	public void applyEvent(Event e)
	{
		if(e.isAPIEvent())
			apiEventHandler.handleEvent(e);
		else
			eventHandler.handleEvent(e);
	}
	
}
