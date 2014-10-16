package com.llsx.pps.messaging;


public interface MessageDispatcher {

	public void sendMessage(Message message);
	public void sendMessageOnDefaultChannel(Message message);
	public void receiveMessage(Message message);
	public void setTransportInterface(TransportInterface transportInterface);

}
