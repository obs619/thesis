package com.llsx.pps.event;

import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.MessageBuilder;
import com.llsx.pps.messaging.MessageDispatcher;

public class EventManager {
	
	private MessageBuilder messageBuilder;
	private MessageDispatcher messageDispatcher;
	private static EventHandler eventHandler;
	private static ApiEventHandler apiEventHandler = new ApiEventHandler();
	private static EventManagerFactory factory;
	private static EventManager instance = null;

	public static EventManager getInstance() {
		if (instance == null)
			instance = factory.createEventManager();
		return instance;
	}

	public static void setDefaultFactory(EventManagerFactory factory) {
		EventManager.factory = factory;
	}
	
	public EventManager(MessageBuilder messageBuilder, MessageDispatcher messageDispatcher) {
		this.messageBuilder = messageBuilder;
		this.messageDispatcher = messageDispatcher;
	}
	
	public void sendEvent(Event event) {
		Message message = messageBuilder.buildMessage(event);
		messageDispatcher.sendMessage(message,true);
	}
	
	public void sendEventOnDefaultChannel(Event event) {
		Message message = messageBuilder.buildMessage(event);
		messageDispatcher.sendMessage(message,false);
	}
	
	public void unpackEvent(Message message) {
		Event event = messageBuilder.unpackEvent(message);
		applyEvent(event);
	}
	
	public void setEventHandler(EventHandler handler) {
		eventHandler = handler;
	}
	
	public void triggerEvent(Event event) {
		if(event.getRecipient() != Event.R_LOCAL_SCREEN)
			sendEvent(event);
		applyEvent(event);//apply event to yourself (if it affects you)
	}
	
	public void applyEvent(Event event) {
		if(event.isApiEvent()) {
			apiEventHandler.handleEvent(event);
		}
		else {
			eventHandler.handleEvent(event);
		}
	}
	
}
