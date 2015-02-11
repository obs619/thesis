package com.thsst3.snakesandladders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.session.SessionManager;

public class PlayerActivity extends Activity{

	public class PlayerEventHandler implements EventHandler{
		@Override
		public void handleEvent(Event event) {
			Log.e("Handling PlayerEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
			switch(event.getType())
			{
			case EventConstants.NOTIFY_PLAYER_NUM:
				playerNumber = (Integer)event.getPayload();
				
				if(playerNumber == 0) {
					turn = true;
					btnRollDice.setEnabled(true);
					txtTurn.setText("Is it your turn? Yes");
				}
				else {
					turn = false;
					btnRollDice.setEnabled(false);
					txtTurn.setText("Is it your turn? No");
				}
					
				txtPlayer.setText(playerNumber + "-" + SessionManager.getInstance().getOwnDeviceName());
				break;
			case EventConstants.NOTIFY_PLAYER_TURN:
				turn = (Boolean)event.getPayload();
				btnRollDice.setEnabled(true);
				txtTurn.setText("Is it your turn? Yes");
				break;
			case EventConstants.NOTIFY_WINNER:
				String winnername = (String)event.getPayload();
				
				
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
			}
		}
	}
	
	Button btnRollDice;
	Boolean turn;
	TextView txtTurn;
	TextView txtPlayer;
	int playerNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		btnRollDice = (Button) findViewById(R.id.btnRollDice);
		txtTurn = (TextView) findViewById(R.id.txtTurn);
		txtPlayer = (TextView) findViewById(R.id.txtPlayer);
		
		btnRollDice.setEnabled(false);
		
		EventManager.getInstance().setEventHandler(new PlayerEventHandler());
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		PpsManager.getInstance().stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		PpsManager.getInstance().start();
	}
	
	public void clickRollDice(View v) {
		btnRollDice.setEnabled(false);
		txtTurn.setText("Is it your turn? No");
		Event e= new Event(Event.R_PUBLIC_SCREENS,EventConstants.PLAYER_ROLL_DICE, playerNumber);
		EventManager.getInstance().sendEvent(e);
		
	}
	
}
