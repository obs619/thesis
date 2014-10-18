package com.llsx.pps.messaging;


public interface MessageDispatcher {

	public void sendMessage(Message message, boolean isCustomChannel);
	public void receiveMessage(Message message);
	public void setTransportInterface(TransportInterface transportInterface);

}
