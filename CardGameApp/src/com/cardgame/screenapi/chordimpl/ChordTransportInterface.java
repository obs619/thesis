package com.cardgame.screenapi.chordimpl;

import android.util.Log;

import com.cardgame.activities.PlayPersonalActivity;
import com.cardgame.chord.IChordChannelListenerAdapter;
import com.cardgame.screenapi.Event;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.MessageDispatcher;
import com.cardgame.screenapi.SessionManager;
import com.cardgame.screenapi.TransportInterface;
import com.samsung.android.sdk.chord.SchordChannel;

/**
 * Calls/is called by Chord to send and receive messages
 * @author Andrew
 *
 */
public class ChordTransportInterface implements TransportInterface {

	private static MessageDispatcher messageDispatcher;
	private static final String PAYLOAD_TYPE = "CHORD_SPS"; //
	
	public static SchordChannel mChannel;
	
	public static String channelName = "defaultchannel";
	
	public ChordTransportInterface()
	{
		//joinChannel();
	}
	
	public static void joinChannel() {
		//Joins to the channel with the specified name.
		try {
			mChannel = ChordNetworkManager.getChordManager().joinChannel(channelName, mChordChannelListener);			 
			Log.e("ChordTransport", "successful");
		}catch(Exception e) {
			Log.e("ChordTransport", "not succesful");
			e.printStackTrace();
		}
		 
		 if(mChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to join channel");
	}
	
	private final static SchordChannel.StatusListener mChordChannelListener = new IChordChannelListenerAdapter() {
		
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
				Event e=new Event(ChordNetworkManager.getChordManager().getName()
						,Event.R_ALL_SCREENS
						,Event.USER_PERSONAL
						,ChordNetworkManager.getChordManager().getName(),true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(ChordNetworkManager.getChordManager().getName()
						,Event.R_ALL_SCREENS
						,30
						,ChordNetworkManager.getChordManager().getName(),false);
				EventManager.getInstance().sendEvent(e1);
			}
			else {
				Event e=new Event(ChordNetworkManager.getChordManager().getName()
						,Event.R_ALL_SCREENS
						,Event.USER_SHARED
						,ChordNetworkManager.getChordManager().getName(),true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1=new Event(fromNode
						,Event.R_ALL_SCREENS
						,31
						,ChordNetworkManager.getChordManager().getName(),false);
				EventManager.getInstance().sendEvent(e1);
			}
			
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			Log.e("left", fromNode);
			if(SessionManager.getInstance().isPersonal()) {
				Log.e("personal onnodeleft", "d");
				SessionManager.getInstance().removePrivateScreen(fromNode);
			}
			else {
				Log.e("shared onnodeleft", "d");
				SessionManager.getInstance().removePublicScreen(fromNode);
			}
		}
		
	};
	
	/**
	 * Sends message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 */
	@Override
	public void sendToAll(Message message) {
		mChannel.sendDataToAll(PAYLOAD_TYPE, new byte[][] {((ChordMessage) message).getBytes() });
	}
	
	
	/**
	 * Sends private message over the channel.
	 * 
	 * @param message
	 *            message to be sent
	 * @param userToSend
	 * 			  nodename of the user which the message will be sent
	 */
	public void send(String userToSend,Message message) {
		mChannel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] {  ((ChordMessage) message).getBytes() });
	}


	public static void onMessageReceived(Message receivedMessage) {
		messageDispatcher.receiveMessage(receivedMessage);
		
	}


	@Override
	public void setMessageDispatcher(MessageDispatcher dispatcher) {
		this.messageDispatcher=dispatcher;
		//dispatcher.setTransportInterface(this);
		
	}
	
}