package com.cardgame.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cardgame.R;
import com.cardgame.adapters.HandAdapter;
import com.cardgame.handlers.CardGameEvent;
import com.cardgame.handlers.CardGameEventHandler;
import com.cardgame.objects.Card;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.event.Event;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.session.SessionManager;

public class PlayPersonalActivity extends Activity{
	
	// UI variables
	private ListView listCards;
	private TextView txtError;
	private TextView txtUserName;
	public static TextView txtPlayerNum;
	public static TextView txtPlayerToDrawFrom;
	public static TextView txtTurn;
	
	// Adapter variables
	private static HandAdapter handAdapter;
	
	// Shared/Personal screen variables
	public static PPSManager spsManager;
	
	public static String playerToDrawFromNumber;
	public static String playerToDrawFromAliasName;
	
	public static int playerNum;
	public static boolean turn = false;
	
	static Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_personal);
		
 		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Link UI variables to UI
		listCards = (ListView) findViewById(R.id.listPersonalCards);
		txtError = (TextView) findViewById(R.id.txtPersonalError);
		txtUserName = (TextView) findViewById(R.id.txtUserName);
		txtPlayerNum = (TextView) findViewById(R.id.txtPlayerNum);
		txtPlayerToDrawFrom = (TextView) findViewById(R.id.txtPlayerToDraw);
		txtTurn = (TextView) findViewById(R.id.txtTurn);
		
		activity = this;
		
		spsManager = new PPSManager(this, true, false);
		EventManager.getInstance().setEventHandler(new CardGameEventHandler());
		
		// Initialize adapters
		handAdapter = new HandAdapter(this);
		listCards.setAdapter(handAdapter);
		
		final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	txtUserName.setText("Username: " + SessionManager.getInstance().getOwnAlias());
	        }
	    }, 500);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		spsManager.stop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		spsManager.start();
	}
	
	public static void removeCard(Card card) {
		handAdapter.removeCard(card);
	}
	
	public static void addCard(Card c) {
		c.setSelected(false);
		handAdapter.addCard(c);
	}
	
	public void clickPlay(View v) {
		
	    List<Card> cardsToPlay = new ArrayList<Card>();
	    for( int i = 0; i < handAdapter.getCount(); i++ ){
	        Card item = (Card) handAdapter.getItem(i);
	        if(item.isSelected())
	        	cardsToPlay.add(item);
	    }
	    if(SessionManager.getInstance().getPublicScreenList().size() > 0) {
	    	if(cardsToPlay.size() == 2) {
	    		if(cardsToPlay.get(0).getNumber() == cardsToPlay.get(1).getNumber()) {
	    			for(Card card: cardsToPlay)
				    {
				    	Event e=new Event(Event.R_PUBLIC_SCREENS,CardGameEvent.CARD_PLAYED,card);
						EventManager.getInstance().triggerEvent(e);
				    }
			    	
	    			Log.e("Number of cards left in hand", handAdapter.getCount() + "");
	    			
	    			if(handAdapter.getCount() == 0) {
	    				Event e=new Event(Event.R_PUBLIC_SCREENS,CardGameEvent.OUT_OF_CARDS,playerNum);
						EventManager.getInstance().sendEvent(e);
	    				
	    				AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    					builder.setTitle("Result");
		    			    builder.setCancelable(false);
		    			    builder.setMessage("Congratulations! You're all out of cards!") ;
		    			    builder.setPositiveButton("ok", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							});
	    				
	    				AlertDialog alert = builder.create();
	                    alert.show();
	                    
	                    spsManager.stop();
	    			}
			    	txtError.setVisibility(View.GONE);
	    		}
	    		else {
	    			txtError.setText("The 2 cards must have the same value!");
					txtError.setVisibility(View.VISIBLE);
			    }
		    }
		    else {
		    	txtError.setText("Please select 2 same value cards");
				txtError.setVisibility(View.VISIBLE);
		    }
	    }
	    else {
	    	txtError.setText("No active shared device!");
			txtError.setVisibility(View.VISIBLE);
	    }
	}
	
	public void clickDraw(View v) {
		
		if(turn) {
			//to avoid drawing multiple cards due to network problem and multiple clicking; set turn immediately to false
			turn = false;
			PlayPersonalActivity.txtTurn.setText("Is it your turn? No");
			
			//send node name to player to notify for draw event
			Event e=new Event(SessionManager.getInstance().getNodeName(playerToDrawFromAliasName), CardGameEvent.CARD_DRAW_REQUEST, spsManager.getDeviceName());
			EventManager.getInstance().sendEvent(e);
			
		}
		else {
			Toast.makeText(this, "It is not your turn yet!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public static void respondDrawRequest(String requesterNodeName) {
		// get all available cards
		List<Card> cardsToPlay = new ArrayList<Card>();
	    for( int i = 0; i < handAdapter.getCount(); i++ ) {
	        Card item = (Card) handAdapter.getItem(i);
	        	cardsToPlay.add(item);
	    }
	    
	    //shuffle
	    Collections.shuffle(cardsToPlay);
	    
	    Toast.makeText(PPSManager.getContext(), cardsToPlay.get(0).toString() + " was taken from you.", Toast.LENGTH_LONG).show();
	    
	    //remove and send first random card
	    removeCard(cardsToPlay.get(0));
    	Event e=new Event(requesterNodeName, CardGameEvent.DRAW_RESPOND, cardsToPlay.get(0));
		EventManager.getInstance().sendEvent(e);
		
		//check if player has no cards left then player wins
		if(handAdapter.getCount() == 0) {
			Event e1=new Event(Event.R_PUBLIC_SCREENS,CardGameEvent.OUT_OF_CARDS,playerNum);
			EventManager.getInstance().sendEvent(e1);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("Result");
		    builder.setCancelable(false);
		    builder.setMessage("Congratulations! You're all out of cards!") ;
		    builder.setPositiveButton("ok", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.finish();
				}
			});
		
			AlertDialog alert = builder.create();
	        alert.show();
        
	        spsManager.stop();
		}
		
		 new CountDownTimer(3500, 1000) {

		     public void onTick(long millisUntilFinished) {
		         PlayPersonalActivity.txtTurn.setText("Is it your turn? " + millisUntilFinished / 1000);
		     }

		     public void onFinish() {
		    	 PlayPersonalActivity.turn = true;
		    	 PlayPersonalActivity.txtTurn.setText("Is it your turn? Yes");
		     }
		  }.start();
	}
	
	public static void showLoseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Result");
	    builder.setCancelable(false);
	    builder.setMessage("Lose! You are the monkey!") ;
	    builder.setPositiveButton("ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}
		});
	
		AlertDialog alert = builder.create();
	    alert.show();
	}
	
	public void clickPersonal(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getPrivateScreenAliasList())
			nodes += node + ",";
		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void clickShared(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getPublicScreenAliasList())
			nodes += node + ",";
		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void clickCheckSession(View v) {
		Toast.makeText(this, spsManager.getCurrentSessionName(), Toast.LENGTH_LONG).show();
	}
	
}
