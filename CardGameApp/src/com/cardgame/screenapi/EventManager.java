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
	static EventHandler eventHandler;
	static APIEventHandler apiEventHandler=new APIEventHandler();
	public static EventManagerFactory factory;
	public static EventManager instance=null;
	//TODO optional global event queue
	//TODO optional shared hash table w/ distributed mutex
	public static EventManager getInstance()
	{
		if (instance==null)
			instance= factory.createEventManager();
		return instance;
	}

	
	public static void setDefaultFactory(EventManagerFactory factory)
	{
		EventManager.factory=factory;
	}
	
	public EventManager(MessageBuilder messageBuilder, MessageDispatcher messageDispatcher/*, EventHandler eventHandler*/) //temporary fix
	{
		this.messageBuilder=messageBuilder;
		this.messageDispatcher=messageDispatcher;
		//this.eventHandler=eventHandler;
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
	public static void setEventHandler(EventHandler h)
	{
		eventHandler=h;
	}
	public void triggerEvent(Event e)
	{
		if(e.getRecipient()!=Event.R_LOCAL_SCREEN)
			sendEvent(e);
		applyEvent(e);//apply event to yourself (if it affects you?)
	}
	public void applyEvent(Event e)
	{
		Log.e("applyevent","applying event...");
		if(e.isAPIEvent()) {
			Log.e("apply apievent", e.isAPIEvent() + e.getPayload().toString());
			apiEventHandler.handleEvent(e);
		}
		else {
			Log.e("apply not apievent", e.isAPIEvent() + e.getPayload().toString());
			eventHandler.handleEvent(e);
		}
			
	}
	
}
