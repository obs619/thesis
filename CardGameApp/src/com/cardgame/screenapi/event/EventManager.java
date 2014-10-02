package com.cardgame.screenapi.event;

import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.MessageBuilder;
import com.cardgame.screenapi.messaging.MessageDispatcher;

public class EventManager {
	
	MessageBuilder messageBuilder;
	MessageDispatcher messageDispatcher;
	static EventHandler eventHandler;
	static APIEventHandler apiEventHandler=new APIEventHandler();
	public static EventManagerFactory factory;
	public static EventManager instance=null;

	public static EventManager getInstance() {
		if (instance==null)
			instance= factory.createEventManager();
		return instance;
	}

	public static void setDefaultFactory(EventManagerFactory factory) {
		EventManager.factory=factory;
	}
	
	public EventManager(MessageBuilder messageBuilder, MessageDispatcher messageDispatcher) {
		this.messageBuilder=messageBuilder;
		this.messageDispatcher=messageDispatcher;
	}
	
	public void sendEvent(Event e) {
		Message m=messageBuilder.buildMessage(e);
		messageDispatcher.sendMessage(m);
	}
	
	public void unpackEvent(Message m) {
		Event e=messageBuilder.unpackEvent(m);
		applyEvent(e);
	}
	
	public void setEventHandler(EventHandler h) {
		eventHandler=h;
	}
	
	public void triggerEvent(Event e) {
		if(e.getRecipient()!=Event.R_LOCAL_SCREEN)
			sendEvent(e);
		applyEvent(e);//apply event to yourself (if it affects you)
	}
	
	public void applyEvent(Event e) {
		if(e.isAPIEvent()) {
			apiEventHandler.handleEvent(e);
		}
		else {
			eventHandler.handleEvent(e);
		}
	}
	
}
