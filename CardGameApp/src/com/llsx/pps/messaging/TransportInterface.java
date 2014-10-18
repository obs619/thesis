package com.llsx.pps.messaging;


public interface TransportInterface {
	
	public void send(String username, Message message, boolean isCustomChannel);
	public void sendToAll(Message message, boolean isCustomChannel);
	public void setMessageDispatcher(MessageDispatcher dispatcher);

}
