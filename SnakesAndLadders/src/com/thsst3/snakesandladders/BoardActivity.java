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
import com.llsx.pps.event.EventManager;
import com.llsx.pps.session.SessionManager;


public class BoardActivity extends Activity {

	public class BoardEventHandler implements EventHandler{
		@Override
		public void handleEvent(Event event) {
			Log.e("Handling BoardEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
			switch(event.getType())
			{
			case EventConstants.PLAYER_ROLL_DICE:
				int playerNumberWhoRolled = (Integer)event.getPayload();
				
				Random rand = new Random();
				int randomNum = rand.nextInt((6 - 1) + 1) + 1;
				String playerAliasName = SessionManager.getInstance().getDeviceName(playerMap.get(playerNumberWhoRolled));
				logList.add(0, 
						playerNumberWhoRolled + "-" + playerAliasName
						+ " has rolled a " + randomNum);
				logAdapter.notifyDataSetChanged();
				lstLogs.smoothScrollToPosition(0);


				if(isFirstRoll(playerAliasName)) {
					for(int i = 0; i < baseLayout.getChildCount(); i++) {
			            if(baseLayout.getChildAt(i) instanceof TableRow) {
			        		TableRow tableRow = (TableRow) baseLayout.getChildAt(i);
			                for(int x = 0; x < tableRow.getChildCount(); x++) {
			                    TextView txtView = (TextView) tableRow.getChildAt(x);   
			      
			                    if(txtView.getText().toString().contains("*" + randomNum + "*"))
			                    	txtView.append("\r\n" + playerAliasName); 
			             }             
			        }
					}
				}
				else {
					fullloop : for(int i = 0; i < baseLayout.getChildCount(); i++) {
			            if(baseLayout.getChildAt(i) instanceof TableRow) {
			        		TableRow tableRow = (TableRow) baseLayout.getChildAt(i);
			                for(int x = 0; x < tableRow.getChildCount(); x++) {
			                    TextView txtView = (TextView) tableRow.getChildAt(x);   
			      
			                    if(txtView.getText().toString().contains(playerAliasName)) {
			                    	txtView.setText(txtView.getText().toString().replaceAll("\r\n" + playerAliasName, ""));
			                    	String[] splitTxt = txtView.getText().toString().split("-");
			                    	int valueText = Integer.parseInt(splitTxt[0].substring(1, splitTxt[0].length() - 1));
			                    	int newValueOfPlayer = valueText + randomNum;
			                    	
			                    	for(int a = 0; a < baseLayout.getChildCount(); a++) {
			        		            if(baseLayout.getChildAt(a) instanceof TableRow) {
			        	            		TableRow tableRow1 = (TableRow) baseLayout.getChildAt(a);
			        	                    for(int b = 0; b < tableRow1.getChildCount(); b++) {
			        	                        TextView txtView1 = (TextView) tableRow1.getChildAt(b);   
			        	                        Log.e("new value of player",newValueOfPlayer + "!" + txtView1.getText().toString());
			        	                        if(txtView1.getText().toString().contains("*" + newValueOfPlayer + "*")) {
			        	                        	txtView1.append("\r\n" + playerAliasName);
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
				
				
				/*
				if(playerNumberWhoRolled + 1 == playerMap.size()) {
					//last player
					Event e= new Event(playerMap.get(0),EventConstants.NOTIFY_PLAYER_TURN, true);
					EventManager.getInstance().sendEvent(e);
				}
				else {
					Event e= new Event(playerMap.get(playerNumberWhoRolled + 1),EventConstants.NOTIFY_PLAYER_TURN, true);
					EventManager.getInstance().sendEvent(e);
				}
				*/
				break;
			}
		}
	}
	
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
				   ///tv.setText("R " + i + ", C" + j);
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
		
		EventManager.getInstance().setEventHandler(new BoardEventHandler());
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

	public void clickStartGame(View v) {
		if(PpsManager.getInstance().getPrivateScreenList().size() > 0) {
			
			//get number of players
			int numPlayers = PpsManager.getInstance().getPrivateScreenList().size();
			
		    playerMap = new TreeMap<Integer, String>();
		    
		    for(int i = 0; i < numPlayers; i++)
		    	playerMap.put(i, PpsManager.getInstance().getPrivateScreenList().get(i));
		    
		    String playersList = "";
		    for(Map.Entry<Integer,String> entry : playerMap.entrySet()) {
		    	  Integer key = entry.getKey();
		    	  String value = entry.getValue();
		    	  value = SessionManager.getInstance().getDeviceName(value);
		    	  playersList += key + "-" + value + "\r\n";
		    }
		    txtPlayers.setText(playersList);
		    
		    for(int i = 0; i < numPlayers; i++) {
		    	// send own player number
		    	Event e= new Event(playerMap.get(i),EventConstants.NOTIFY_PLAYER_NUM, i);
				EventManager.getInstance().sendEvent(e);
		    }
		    
		    btnStartGame.setEnabled(false);
		    Toast.makeText(this, "Game started!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Must have 2 or more players to start game!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
}
