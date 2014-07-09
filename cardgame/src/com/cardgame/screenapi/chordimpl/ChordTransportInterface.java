package com.cardgame.screenapi.chordimpl;

import android.view.View;

import com.cardgame.chord.IChordChannelListenerAdapter;
import com.cardgame.screenapi.Message;
import com.cardgame.screenapi.MessageDispatcher;
import com.cardgame.screenapi.TransportInterface;
import com.cardgame.uiOLD.MainActivity;
import com.cardgame.uiOLD.SplashActivity;
import com.samsung.android.sdk.chord.SchordChannel;


public class ChordTransportInterface implements TransportInterface {

	private MessageDispatcher messageDispatcher;
	private static final String PAYLOAD_TYPE = "CHORD_SPS";
	private SchordChannel mChannel;
	private final SchordChannel.StatusListener mChordChannelListener = new IChordChannelListenerAdapter() {
		
		@Override
		public void onDataReceived(String arg0, String arg1, String arg2,
				byte[][] arg3) {
			if (PAYLOAD_TYPE.equals(arg2)) {
				final ChordMessage receivedMessage = ChordMessage.obtainChatMessage(arg3[0]);
				onMessageReceived(receivedMessage);
			}
			
			// adds the nodename + alias username of the node that joined to the spinner
			if ("user_details".equals(arg2)) {
				boolean exist = false;
				for(String username : MainActivity.listUsernames) {
					if(username.equals(new String(arg3[0])))
						exist = true;
				}
				if(!exist) {
					MainActivity.listUsernames.add(new String(arg3[0]));
					MainActivity.dataAdapter.notifyDataSetChanged();
				}
			}
		}
		
		@Override
		public void onNodeJoined(String fromNode, String fromChannel) {
			//TODO integrate with game-layer stuff
			/*if(SplashActivity.screenType == "Private")
				//sendDetailsMessage(MainActivity.currUserNodeName+ ":" + MainActivity.mUserNameView.getText().toString());
			else
				MainActivity.mInputContainer.setVisibility(View.GONE);*/
		}
		
		@Override
		public void onNodeLeft(String fromNode, String fromChannel) {
			for(String username : MainActivity.listUsernames) {
				if(username.contains(fromNode)) {
					MainActivity.listUsernames.remove(username);
		        	MainActivity.dataAdapter.notifyDataSetChanged();
				}	
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


	@Override
	public void onMessageReceived(Message receivedMessage) {
		messageDispatcher.receiveMessage(receivedMessage);
		
	}


	@Override
	public void setMessageDispatcher(MessageDispatcher dispatcher) {
		this.messageDispatcher=dispatcher;
		
	}


	
	
	
}
