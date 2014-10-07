package com.cardgame.screenapi.chordimpl;

import android.util.Log;

import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.messaging.Message;
import com.cardgame.screenapi.messaging.MessageDispatcher;
import com.cardgame.screenapi.messaging.TransportInterface;
import com.cardgame.screenapi.session.SessionManager;
import com.samsung.android.sdk.chord.SchordChannel;

public class ChordTransportInterface implements TransportInterface {

	private static MessageDispatcher messageDispatcher;
	private static final String PAYLOAD_TYPE = "CHORD_SPS"; 
	
	public static SchordChannel mChannel;
	public static SchordChannel defaultChannel;
	public static String channelName = "defaultchannel";
	
	public ChordTransportInterface() {}
	
	public static void joinDefaultChannel() {

		try {
			defaultChannel = ChordNetworkManager.getChordManager().joinChannel(channelName, defaultChannelListener);
			mChannel=ChordNetworkManager.getChordManager().joinChannel(channelName, mChordChannelListener);//not sure if this is right or if you can attach two listeners to the same channel; this is to ensure that the send() function will not cause an NPE
		}catch(Exception e) {
			e.printStackTrace();
		}
		 if(defaultChannel==null)
			 Log.e("CHANNEL ERROR", "Failed to join default channel");
		 if(mChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to set mChannel to default channel");
	}
	
	public static void joinCustomChannel() {
		
		try {
			mChannel = ChordNetworkManager.getChordManager().joinChannel(SessionManager.getInstance().getChosenSession(), mChordChannelListener);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		 if(mChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to join custom channel");
		
	}
	
	private final static SchordChannel.StatusListener mChordChannelListener = new SPSChordChannelListenerAdapter();
	private final static SchordChannel.StatusListener defaultChannelListener = new SPSChordChannelListenerAdapter();
	
	
	
	@Override
	public void sendToAll(Message message) {
		mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] {((ChordMessage) message).getBytes() });
	}

	@Override
	public void setMessageDispatcher(MessageDispatcher dispatcher) {
		ChordTransportInterface.messageDispatcher=dispatcher;
	}
	
	public static void onMessageReceived(Message receivedMessage) {
		messageDispatcher.receiveMessage(receivedMessage);
	}
	
	public void send(String userToSend,Message message) {
		mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  ((ChordMessage) message).getBytes() });
	}
	public void sendOnDefaultChannel(String userToSend,Message message){
		defaultChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  ((ChordMessage) message).getBytes() });
	}

	private static class SPSChordChannelListenerAdapter extends ChordChannelListenerAdapter{
		@Override
		public void onDataReceived(String fromNode, String fromChannel, String payloadType,
				byte[][] payload) {
			if (PAYLOAD_TYPE.equals(payloadType)) {
				final ChordMessage receivedMessage = ChordMessage.obtainChatMessage(payload[0]);
				onMessageReceived(receivedMessage);
			}
		}
		
		//device
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			Log.e("JOINED", fromNode);
			
			if(fromChannel==channelName&& SessionManager.getInstance().getChosenSession()!=channelName)
			{
				//sendOnDefaultChannel()
				//TODO get name of newly joined node
				//TODO send SessionManager.getInstance().getChosenSession() as a message
			}
			
			String[] nodeAlias = getNodeAlias();
				
			
			if(SessionManager.getInstance().isPersonal()) {
				
				Event e=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PRIVATE
						,nodeAlias,true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PRIVATE
						,nodeAlias,false);
				EventManager.getInstance().sendEvent(e1);
			}
			else {
				Event e=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PUBLIC
						,nodeAlias,true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PUBLIC
						,nodeAlias,false);
				EventManager.getInstance().sendEvent(e1);
			}
			
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			Log.e("LEFT", fromNode);
			if(SessionManager.getInstance().getPrivateScreenList().contains(fromNode)) {

				//can be remove
				Event e=new Event(Event.R_LOCAL_SCREEN
						,Event.USER_LEFT_PRIVATE
						,fromNode,false);
				EventManager.getInstance().applyEvent(e);
				
				Event e1=new Event(Event.R_LOCAL_SCREEN
						,Event.USER_LEFT_PRIVATE
						,fromNode,true);
				EventManager.getInstance().applyEvent(e1);
			}
			else {
				Event e=new Event(Event.R_LOCAL_SCREEN
						,Event.USER_LEFT_PUBLIC
						,fromNode,false);
				EventManager.getInstance().applyEvent(e);
				
				Event e1=new Event(Event.R_LOCAL_SCREEN
						,Event.USER_LEFT_PUBLIC
						,fromNode,true);
				EventManager.getInstance().applyEvent(e1);
			}
			
		}
		
		//get nodeName and it's alias if exist
		public String[] getNodeAlias(){
			String[] nodeAlias = new String[2]; 
			nodeAlias[0] = ChordNetworkManager.getChordManager().getName();
			Log.e("OnNodeJoin get Alias", SessionManager.getInstance().getOwnAlias());
			nodeAlias[1] = SessionManager.getInstance().getOwnAlias();
			return nodeAlias;
		}
		
	};
	
	@Override
	public void sendToAll(Message message) {
		try{
			mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] {((ChordMessage) message).getBytes() });
		}catch(Exception e) {
            Log.e("ChordTransportInterface", "sendToAll failed");
            return;
        }
	}

	@Override
	public void setMessageDispatcher(MessageDispatcher dispatcher) {
		ChordTransportInterface.messageDispatcher=dispatcher;
	}
	
	public static void onMessageReceived(Message receivedMessage) {
		messageDispatcher.receiveMessage(receivedMessage);
	}
	
	public void send(String userToSend,Message message) {
		try{
			mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  ((ChordMessage) message).getBytes() });
		}catch(Exception e){
			Log.e("ChordTransportInterface", "send failed");
			return;
		}
	}

}
