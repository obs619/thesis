package com.llsx.pps.messaging;

import com.llsx.pps.event.Event;

public interface MessageBuilder {

	/**
	 * Builds the message from the given event. The message is used by transport interface to send between devices.
	 * @param event event to be converted into message and send 
	 * @return message converted message of an event
	 */
	public Message buildMessage(Event event);
	
	/**
	 * Builds the event from the given message. The event is applied by event handlers(build-in API event handler or custom event handler, depending on event type) when the device receives it.
	 * @param message message received and going to convert into event
	 * @return event converted event of a message
	 */
	public Event unpackEvent(Message message);
	
}
