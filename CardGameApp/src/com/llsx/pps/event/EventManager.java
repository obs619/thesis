package com.llsx.pps.event;

import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.MessageBuilder;
import com.llsx.pps.messaging.MessageDispatcher;

/**
 * Manages processing and distribution of Events
 * @author Andrew
 *
 */
public class EventManager {
	/**
	 * EventManager's MessageBuilder, required for converting between Events and Messages
	 */
	private MessageBuilder messageBuilder;
	/**
	 * EventManager's MessageDispatcher, for sending Messages to other devices on the network
	 */
	private MessageDispatcher messageDispatcher;
	/**
	 * Handles application-triggered Events
	 */
	private static EventHandler eventHandler;
	/**
	 * Handles API-triggered Events
	 */
	private static PpsEventHandler ppsEventHandler = new PpsEventHandler();
	/**
	 * Used to create an instance of EventManager
	 */
	private static EventManagerFactory factory;
	
	/**
	 * The actual instance of EventManager
	 */
	private static EventManager instance = null;

	/**
	 * Used to get the current (singleton) instance of EventManager
	 * @return the current instance of EventManager
	 */
	public static EventManager getInstance() {
		if (instance == null)
			instance = factory.createEventManager();
		return instance;
	}

	/**
	 * Sets the type of EventManagerFactory to be used, and thus the type of EventManager that will be created
	 * @param factory
	 */
	public static void setDefaultFactory(EventManagerFactory factory) {
		EventManager.factory = factory;
	}
	
	/**
	 * 
	 * @param messageBuilder the EventManager's message builder
	 * @param messageDispatcher the EventManager's message dispatcher
	 */
	public EventManager(MessageBuilder messageBuilder, MessageDispatcher messageDispatcher) {
		this.messageBuilder = messageBuilder;
		this.messageDispatcher = messageDispatcher;
	}
	
	/**
	 * Converts an Event into a Message and sends it to its recipients using the current session/channel
	 * @param event Event to be sent
	 */
	public void sendEvent(Event event) {
		Message message = messageBuilder.buildMessage(event);
		messageDispatcher.sendMessage(message,true);
	}
	
	/**
	 * Converts an Event into a Message and sends it to its recipients using the default session/channel
	 * @param event Event to be sent
	 */
	public void sendEventOnDefaultChannel(Event event) {
		Message message = messageBuilder.buildMessage(event);
		messageDispatcher.sendMessage(message,false);
	}
	
	/**
	 * Converts a received Message into an Event and applies it
	 * @param message the Message to be unpacked into an Event
	 */
	public void unpackEvent(Message message) {
		Event event = messageBuilder.unpackEvent(message);
		applyEvent(event);
	}
	/**
	 * Sets the application event handler of the EventManager
	 * @param handler EventHandler to be used
	 */
	public void setEventHandler(EventHandler handler) {
		eventHandler = handler;
	}
	/**
	 * Sends an event to all recipients and/or applies it on the local device
	 * @param event Event to be triggered
	 */
	public void triggerEvent(Event event) {
		if(event.getRecipient() != Event.R_LOCAL_SCREEN)
			sendEvent(event);
		applyEvent(event);//apply event to yourself (if it affects you)
	}
	/**
	 * Applies an Event to the local device using the appropriate EventHandler
	 * @param event Event to be applied
	 */
	public void applyEvent(Event event) {
		if(event.isPpsEvent()) {
			ppsEventHandler.handleEvent(event);
		}
		else {
			eventHandler.handleEvent(event);
		}
	}
	
}
