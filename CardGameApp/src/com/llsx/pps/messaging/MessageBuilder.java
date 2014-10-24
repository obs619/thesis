package com.llsx.pps.messaging;

import com.llsx.pps.event.Event;


/**
 * Handles the building and composition of the message.
 * @author Amanda
 */
public interface MessageBuilder {

	/**
	 * Builds the message based from the given event. The
	 * message is used by the transport interface and is
	 * sent to other devices.
	 * @param event event to be converted into a message
	 * and sent to other devices 
	 * @return The message produced from an event.
	 */
	public Message buildMessage(Event event);
	
	/**
	 * Builds the event based from the given message.
	 * The event is applied by event handlers (which is
	 * either the built-in PPS event handler or your custom
	 * event handler, depending on the event type declared
	 * in the message) when the device receives it.
	 * @param message message to be converted to an event
	 * after being received from any device
	 * @return The original event before it was turned into
	 * a message.
	 */
	public Event unpackEvent(Message message);
	
}
