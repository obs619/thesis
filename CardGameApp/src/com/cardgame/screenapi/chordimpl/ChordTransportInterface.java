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
	public static String channelName = "defaultchannel";
	
	public ChordTransportInterface() {}
	
	public static void joinDefaultChannel() {

		try {
			mChannel = ChordNetworkManager.getChordManager().joinChannel(channelName, mChordChannelListener);		
		}catch(Exception e) {
			e.printStackTrace();
		}
		 
		 if(mChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to join default channel");
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
	
	private final static SchordChannel.StatusListener mChordChannelListener = new ChordChannelListenerAdapter() {
		
		@Override
		public void onDataReceived(String fromNode, String fromChannel, String payloadType,
				byte[][] payload) {
			if (PAYLOAD_TYPE.equals(payloadType)) {
				final ChordMessage receivedMessage = ChordMessage.obtainChatMessage(payload[0]);
				onMessageReceived(receivedMessage);
			}
		}
		
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			Log.e("JOINED", fromNode);
			if(SessionManager.getInstance().isPersonal()) {
				Event e=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PRIVATE
						,ChordNetworkManager.getChordManager().getName(),true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PRIVATE
						,ChordNetworkManager.getChordManager().getName(),false);
				EventManager.getInstance().sendEvent(e1);
			}
			else {
				Event e=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PUBLIC
						,ChordNetworkManager.getChordManager().getName(),true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(Event.R_ALL_SCREENS
						,Event.USER_JOIN_PUBLIC
						,ChordNetworkManager.getChordManager().getName(),false);
				EventManager.getInstance().sendEvent(e1);
			}
			
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			Log.e("LEFT", fromNode);
			if(SessionManager.getInstance().getPrivateScreenList().contains(fromNode)) {
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
		
	};
	
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

}
