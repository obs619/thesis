package com.llsx.pps.internal.chord;

import java.util.Map;

import android.util.Log;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.messaging.Message;
import com.llsx.pps.messaging.MessageDispatcher;
import com.llsx.pps.messaging.TransportInterface;
import com.llsx.pps.session.SessionManager;
import com.samsung.android.sdk.chord.SchordChannel;
/**
 * Concrete implementation of TransportInterface using the Samsung Chord API.
 * @author Andrew
 *
 */
public class ChordTransportInterface implements TransportInterface {

	/**
	 * The MessageDispatcher to be used
	 */
	private static MessageDispatcher messageDispatcher;
	
	/**
	 * Payload type, used to identify messages originating from the SPS API
	 */
	private static final String PAYLOAD_TYPE = "CHORD_SPS"; 
	
	/**
	 * Currently joined custom channel/session
	 */
	public static SchordChannel mChannel;
	/**
	 * The default channel/session
	 */
	public static SchordChannel defaultChannel;
	/**
	 * Name of the default channel
	 */
	public static String channelName = "defaultchannel";
	
	/**
	 * Status listener for the current custom session
	 */
	private final static SchordChannel.StatusListener mChordChannelListener = new SPSChordChannelListenerAdapter();
	/**
	 * Status listener for the default session
	 */
	private final static SchordChannel.StatusListener defaultChannelListener = new SPSChordChannelListenerAdapter();
	
	/**
	 * default constructor
	 */
	//why is this even here
	public ChordTransportInterface() {}
	
	/**
	 * Join the default channel/session
	 */
	public static void joinDefaultChannel() {

		try {
			defaultChannel = ChordNetworkManager.getChordManager().joinChannel(channelName, defaultChannelListener);
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("join default","error");		}
		//mChannel = ChordNetworkManager.getChordManager().joinChannel(channelName, mChordChannelListener);//not sure if this is right or if you can attach two listeners to the same channel; this is to ensure that the send() function will not cause an NPE
		 if (defaultChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to join default channel");
		/*if(mChannel == null)
			 Log.e("CHANNEL E"
			 		+ "RROR", "Failed to set mChannel to default channel");*/
	}
	
	/**
	 * Gets the custom session currently chosen from the SessionManager, then joins it
	 */
	public static void joinCustomChannel() {
		
		try {
			mChannel = ChordNetworkManager.getChordManager().joinChannel(SessionManager.getInstance().getChosenSession(), mChordChannelListener);
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		 if (mChannel == null)
			 Log.e("CHANNEL ERROR", "Failed to join custom channel");
		
	}

	/**
	 *
	 * Internal class for the chord channel listener
	 *
	 */
	private static class SPSChordChannelListenerAdapter extends ChordChannelListenerAdapter {
		
		/**
		 * Called when data is received over the network
		 * @param fromNode device/node that sent the data
		 * @param fromChannel the channel/session on which data was received
		 * @param payloadType identifies the data's origin
		 * @param payload byte array representing the data sent
		 */
		@Override
		public void onDataReceived(String fromNode, String fromChannel, String payloadType,
				byte[][] payload) {
			if (PAYLOAD_TYPE.equals(payloadType)) {
				final ChordMessage receivedMessage = ChordMessage.obtainChatMessage(payload[0]);
				onMessageReceived(receivedMessage);
			}
		}
		
		/**
		 * Called on all devices when a device joins a channel
		 * @param fromNode name of the newly joined device
		 * @param fromChannel channel that was joined
		 */
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			Log.e(fromNode, "New device has connected to the network");
			
			/*The ff. code assumes that onNodeJoined() is triggered on the device that was already in the session*/
			if(fromChannel == channelName&& SessionManager.getInstance().getChosenSession() != channelName) {
				
				//may change type to Event.LATE_JOIN_RESPONSE_SESSION
				Event e = null;
				/*String sessionID = SessionManager.getInstance().getChosenSession();*/
				
				for (Map.Entry<String, Boolean> entry : SessionManager.getInstance().getAvailableSessionsMap().entrySet()) {
					//if(entry.getValue().equals(false)) {
						String[] session={entry.getKey(),entry.getValue().toString()};
						Log.i("SENDING SESSIONS ", entry.getKey()+"fromNode: "+fromNode);
						 e = new Event(fromNode
								,Event.T_RESPOND_REQUEST_SESSIONS
								,session,true);
						 EventManager.getInstance().sendEventOnDefaultChannel(e);
					//}
				}
			
			}
			/*End code block*/
			String[] nodeAlias = getNodeAlias();
				
			
			if(PpsManager.getInstance().isPrivate()) {
				
				Event e = new Event(Event.R_ALL_SCREENS
						,Event.T_USER_JOIN_PRIVATE
						,nodeAlias,true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1 = new Event(Event.R_ALL_SCREENS
						,Event.T_USER_JOIN_PRIVATE
						,nodeAlias,false);
				EventManager.getInstance().sendEvent(e1);
			}
			else {
				Event e = new Event(Event.R_ALL_SCREENS
						,Event.T_USER_JOIN_PUBLIC
						,nodeAlias,true);
				EventManager.getInstance().sendEvent(e);
				
				Event e1 = new Event(Event.R_ALL_SCREENS
						,Event.T_USER_JOIN_PUBLIC
						,nodeAlias,false);
				EventManager.getInstance().sendEvent(e1);
			}
			
		}
		
		/**
		 * Called when a device leaves a channel/session
		 * @param fromNode node/device that left
		 * @param fromChannel the channel that was left
		 */
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			Log.e(fromNode, "The device has disconnected to the network");
			if(PpsManager.getInstance().getPrivateScreenList().contains(fromNode)) {

				//can be remove
				Event e = new Event(Event.R_LOCAL_SCREEN
						,Event.T_USER_LEFT_PRIVATE
						,fromNode,false);
				EventManager.getInstance().applyEvent(e);
				
				Event e1 = new Event(Event.R_LOCAL_SCREEN
						,Event.T_USER_LEFT_PRIVATE
						,fromNode,true);
				EventManager.getInstance().applyEvent(e1);
			}
			else {
				Event e = new Event(Event.R_LOCAL_SCREEN
						,Event.T_USER_LEFT_PUBLIC
						,fromNode,false);
				EventManager.getInstance().applyEvent(e);
				
				Event e1 = new Event(Event.R_LOCAL_SCREEN
						,Event.T_USER_LEFT_PUBLIC
						,fromNode,true);
				EventManager.getInstance().applyEvent(e1);
			}
			
		}
		
		/**
		 * Get the device ID and name
		 * @return array of Strings containing device ID and name
		 */
		public String[] getNodeAlias() {
			String[] nodeAlias = new String[2]; 
			nodeAlias[0] = ChordNetworkManager.getChordManager().getName();
			//Log.e("OnNodeJoin get Alias", SessionManager.getInstance().getOwnDeviceName());
			nodeAlias[1] = SessionManager.getInstance().getOwnDeviceName();
			return nodeAlias;
		}
	}
	
	/**
	 * @param message the actual message being sent
	 * @param isCustomChannel boolean stating whether the message being sent is to the custom channel
	 */
	@Override
	public void sendToAll(Message message, boolean isCustomChannel) {
		SchordChannel channel;
		if(isCustomChannel)
			channel=mChannel;
		else
			channel=defaultChannel;
		try{
			channel.sendDataToAll(PAYLOAD_TYPE, new byte[][] {((ChordMessage) message).getBytes() });
		} catch(Exception e) {
		    Log.e("ChordTransportInterface", "sendToAll failed");
		    return;
		}
	}
	
	/**
	 * Sets the MessageDispatcher to be used
	 * @param the message dispatcher to be used
	 */
	@Override
	public void setMessageDispatcher(MessageDispatcher dispatcher) {
		ChordTransportInterface.messageDispatcher = dispatcher;
	}
	
	/**
	 * Called when a message is received over the network
	 * @param receivedMessage the message being received
	 */
	public static void onMessageReceived(Message receivedMessage) {
		messageDispatcher.receiveMessage(receivedMessage);
	}
	
	/**
	 * @param userToSend the recipient of the message being sent
	 * @param message the actual message being sent
	 * @param isCustomChannel boolean stating whether the message being sent is to the custom channel
	 */
	@Override
	public void send(String userToSend,Message message, boolean isCustomChannel) {
		SchordChannel channel;
		if(isCustomChannel)
			channel=mChannel;
		else
			channel=defaultChannel;
		try{
			channel.sendData(userToSend, PAYLOAD_TYPE, new byte[][] { ((ChordMessage) message).getBytes() });
		} catch(Exception e) {
			Log.e("ChordTransportInterface", "send failed");
			return;
		}
	}

}
