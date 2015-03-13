package com.thsst3.snakesandladders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;
import com.llsx.pps.session.SessionManager;


public class BoardActivity extends Activity {

	public class BoardEventHandler implements EventHandler{
		@Override
		public void handleEvent(Event event) {
			Log.e("Handling BoardEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
			switch(event.getType())
			{
			case EventConstants.PLAYER_ROLL_DICE:
				//received an event from player who just rolled a dice
				
				// get the player number of the player who rolled
				int playerNumberWhoRolled = (Integer)event.getPayload();
				
				// generate random number from 1-6 which the player would roll; get the device name from the player number
				Random rand = new Random();
				int randomNum = rand.nextInt((6 - 1) + 1) + 1;
				String playerAliasName = SessionManager.getInstance().getDeviceName(playerMap.get(playerNumberWhoRolled));
				logList.add(0, playerNumberWhoRolled + "-" + playerAliasName + " has rolled a " + randomNum);
				logAdapter.notifyDataSetChanged();
				lstLogs.smoothScrollToPosition(0);


				// if its player first time to roll, place player to the number based on their roll
				if(isFirstRoll(playerAliasName)) {
					for(int i = 0; i < baseLayout.getChildCount(); i++) {
			            if(baseLayout.getChildAt(i) instanceof TableRow) {
			        		TableRow tableRow = (TableRow) baseLayout.getChildAt(i);
			                for(int x = 0; x < tableRow.getChildCount(); x++) {
			                    TextView txtView = (TextView) tableRow.getChildAt(x);   
			      
			                    if(txtView.getText().toString().contains("*" + randomNum + "*")) {
			                    	txtView.append("\r\n" + playerAliasName); 
			                    	
			                    	logList.add(0, playerAliasName + "'s new value is now " + randomNum);
				    				logAdapter.notifyDataSetChanged();
				    				lstLogs.smoothScrollToPosition(0);
			                    }
			                }             
			            }
					}
				}
				else {
					//if player has rolled before and is now on the board, search for their location, get the number and add the value of the roll to know the new location of the player
					fullloop : for(int i = 0; i < baseLayout.getChildCount(); i++) {
			            if(baseLayout.getChildAt(i) instanceof TableRow) {
			        		TableRow tableRow = (TableRow) baseLayout.getChildAt(i);
			                for(int x = 0; x < tableRow.getChildCount(); x++) {
			                    TextView txtView = (TextView) tableRow.getChildAt(x);   
			      
			                    //if textview contains the players name, found the location
			                    if(txtView.getText().toString().contains(playerAliasName)) {
			                    	txtView.setText(txtView.getText().toString().replaceAll("\r\n" + playerAliasName, ""));
			                    	String[] splitTxt = txtView.getText().toString().split("-");
			                    	int valueText = Integer.parseInt(splitTxt[0].substring(1, splitTxt[0].length() - 1));
			                    	int newValueOfPlayer = valueText + randomNum;
			                    	
			                    	//position player to the new value after the add
			                    	for(int a = 0; a < baseLayout.getChildCount(); a++) {
			        		            if(baseLayout.getChildAt(a) instanceof TableRow) {
			        	            		TableRow tableRow1 = (TableRow) baseLayout.getChildAt(a);
			        	                    for(int b = 0; b < tableRow1.getChildCount(); b++) {
			        	                        TextView txtView1 = (TextView) tableRow1.getChildAt(b);   
			        	                        
			        	                        // if exceed the 100, force it to be 100
			        	                        if(newValueOfPlayer > 100)
			        	                        	newValueOfPlayer = 100;
			        	                        
			        	                        //search in the board where the number is equal to the new value of the player
			        	                        Log.e("new value of player",newValueOfPlayer + "!" + txtView1.getText().toString());
			        	                        if(txtView1.getText().toString().contains("*" + newValueOfPlayer + "*")) {
			        	                        	txtView1.append("\r\n" + playerAliasName);
			        	                        	
			        	                        	logList.add(0, playerAliasName + "'s new value is now " + newValueOfPlayer);
			        			    				logAdapter.notifyDataSetChanged();
			        			    				lstLogs.smoothScrollToPosition(0);
			        			    				
			        			    				
			        			    				//if new value of player >= 100, player wins
			        			    				if(newValueOfPlayer >= 100) {
			        			    					logList.add(0, playerAliasName + " wins the game!");
				        			    				logAdapter.notifyDataSetChanged();
				        			    				lstLogs.smoothScrollToPosition(0);
				        			    				
				        			    				/**
				        			    				 * send to all personal screens winner's alias name (NOTIFY_WINNER)
				        			    				 */
				        			    				
			        			    				}
			        			    				
			        			    				break fullloop;
			        	                        }
			        	                    
			        	                    }
			        		            }             
			        		        }
			                    }
			                 }             
			            }
					}
				}
				
				
				
				if(playerNumberWhoRolled + 1 == playerMap.size()) {
					/**
					 * if last player, notify playerMap.get(0) of his/her turn (NOTIFY_PLAYER_TURN), boolean true message
					 */
					
				}
				else {
					/**
					 * if not last player, send notification to player number who rolled + 1
					 */
					
				}
				
				break;
			}
		}
	}
	
	// check if first roll, name not exist yet on the board
	private boolean isFirstRoll(String playerAliasName) {
		for(int i = 0; i < baseLayout.getChildCount(); i++) {
            if(baseLayout.getChildAt(i) instanceof TableRow) {
        		TableRow tableRow = (TableRow) baseLayout.getChildAt(i);
                for(int x = 0; x < tableRow.getChildCount(); x++) {
                	
                    TextView txtView = (TextView) tableRow.getChildAt(x);   
                    if(txtView.getText().toString().contains(playerAliasName)) 
                    	return false;
                    
                }
             }             
        }
		return true;
	}
	
	TableLayout baseLayout;
	Button btnStartGame;
	TextView txtPlayers;
	ListView lstLogs;
	
	Map<Integer, String> playerMap;
	List<String> logList;
	ArrayAdapter<String> logAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		
		baseLayout = (TableLayout) findViewById(R.id.baseLayout);
		btnStartGame = (Button) findViewById(R.id.btnStartGame);
		txtPlayers = (TextView) findViewById(R.id.txtPlayers);
		lstLogs = (ListView) findViewById(R.id.lstLogs);

		logList = new ArrayList<String>();
		
		logAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logList);
		lstLogs.setAdapter(logAdapter);
		
		for (int i = 9; i >= 0; i--) {
			   TableRow row = new TableRow(this);
			   row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
			     LayoutParams.WRAP_CONTENT));

			   for (int j = 9; j >= 0; j--) {
				   TextView tv = new TextView(this);
				   tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
						   TableRow.LayoutParams.WRAP_CONTENT));

				   tv.setBackgroundResource(R.drawable.rectangle);
				   tv.setText("*" + (i * 10 + j + 1) + "*- ");
				   tv.setPadding(16, 16, 16, 16);
				   tv.setGravity(Gravity.CENTER);
				   tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TextView tv = (TextView) v;
						Toast.makeText(getApplicationContext(), tv.getText(), Toast.LENGTH_LONG).show();
					}
				});
				   row.addView(tv);
			   }
			   baseLayout.addView(row);

		  }
		
		
		/**
		 * set event handler as boardeventhandler
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

	public void clickStartGame(View v) {
		// check if more than 1 player
		if(PpsManager.getInstance().getPrivateScreenList().size() >= 2) {
			
			//get number of players
			int numPlayers = PpsManager.getInstance().getPrivateScreenList().size();
			
		    playerMap = new TreeMap<Integer, String>();
		    
		    // assign numbers to players
		    for(int i = 0; i < numPlayers; i++)
		    	playerMap.put(i, PpsManager.getInstance().getPrivateScreenList().get(i));
		    
		    // get the alias name of the devices from the session manager
		    String playersList = "";
		    for(Map.Entry<Integer,String> entry : playerMap.entrySet()) {
		    	  Integer key = entry.getKey();
		    	  String value = entry.getValue();
		    	  value = SessionManager.getInstance().getDeviceName(value);
		    	  playersList += key + "-" + value + "\r\n";
		    }
		    txtPlayers.setText(playersList);
		    
		    //send players their number
		    for(int i = 0; i < numPlayers; i++) {
		    	/**
		    	 * send event to each in playerMap, their player number. (NOTIFY_PLAYER_NUM)
		    	 */
		    	
		    }
		    
		    btnStartGame.setEnabled(false);
		    Toast.makeText(this, "Game started!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Must have 2 or more players to start game!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
}
