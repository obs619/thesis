package com.thsst3.snakesandladders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;
import com.llsx.pps.session.SessionManager;

public class PlayerActivity extends Activity{

	public class PlayerEventHandler implements EventHandler{
		@Override
		public void handleEvent(Event event) {
			Log.e("Handling PlayerEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
			switch(event.getType())
			{
			case EventConstants.NOTIFY_PLAYER_NUM:
				/**
				 * get player number from event payload. Assign to playerNumber
				 * if playerNumber is 0, set turn, disable btnrolldice and change txtTurn
				 * set txtPlayer to playernumber + own device name
				 */
				
				break;
			case EventConstants.NOTIFY_PLAYER_TURN:
				// 
				/**
				 * player is notified by board that it is now device's turn, get boolean true from event payload, txtTurn to yes
				 */
				
				break;
			case EventConstants.NOTIFY_WINNER:
				// get winner name
				String winnername = (String)event.getPayload();
				
				// show dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
				builder.setTitle("Result");
			    builder.setCancelable(false);
			    builder.setMessage(winnername + " wins the game!") ;
			    builder.setPositiveButton("ok", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
			    
			    AlertDialog alert = builder.create();
                alert.show();
				
				break;
			case EventConstants.NOTIFY_PLAYER_LEFT:
				String nameLeft = (String) event.getPayload();
				layoutPause.setVisibility(View.VISIBLE);
				txtPlayerWhoLeft.setText(nameLeft + " left the game. Game is paused. Please wait for player to rejoin.");
				break;
			case EventConstants.NOTIFY_PLAYER_REJOIN:
				String nameJoin = (String) event.getPayload();			
				layoutPause.setVisibility(View.GONE);
				break;
			case EventConstants.NOTIFY_REMIND_PLAYERNUM:
				playerNumber = (Integer)event.getPayload();
				txtPlayer.setText(playerNumber + "-" + SessionManager.getInstance().getOwnDeviceName());
				
				turn = false;
				btnRollDice.setEnabled(false);
				txtTurn.setText("Is it your turn? No");
				
				break;
			}
		}
	}
	
	
	Button btnRollDice;
	Boolean turn;
	TextView txtTurn;
	TextView txtPlayer;
	int playerNumber;
	TextView txtPlayerName;
	
	RelativeLayout layoutPause;
	TextView txtPlayerWhoLeft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		btnRollDice = (Button) findViewById(R.id.btnRollDice);
		txtTurn = (TextView) findViewById(R.id.txtTurn);
		txtPlayer = (TextView) findViewById(R.id.txtPlayer);
		txtPlayerName = (TextView) findViewById(R.id.txtPlayerName);
		layoutPause = (RelativeLayout) findViewById(R.id.layoutPause);
		txtPlayerWhoLeft = (TextView) findViewById(R.id.txtGamePause);
		
		btnRollDice.setEnabled(false);
		txtPlayerName.setText("Hi, " + SessionManager.getInstance().getOwnDeviceName() + "!");
		
		/**
		 * set playerevent handler
		 */
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * stop ppsmanager
		 */
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * start ppsmanager
		 */
	}
	
	public void clickRollDice(View v) {
		/**
		 * disable btn, txtTurn to no
		 */
		
		/**
		 * send to all public screens(board) that you rolled along with your player number (PLAYER_ROLL_DICE)
		 */
		
	}
	
	public void clickCurrentSession(View v) {
		/**
		 * toast current session name from ppsmanager
		 */
		
	}
	
}
