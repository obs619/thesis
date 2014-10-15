package com.cardgame.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cardgame.R;
import com.cardgame.handlers.SessionEventHandler;
import com.cardgame.screenapi.PpsManager;
import com.cardgame.screenapi.event.EventManager;
import com.cardgame.screenapi.session.SessionManager;

public class SessionActivity extends Activity{
	
	private Spinner spinChannels;
	public static List<String> listChannels;
	public static ArrayAdapter<String> channelsAdapter;
	
	private EditText txtChannel;
	private TextView txtSelectedSession;
	private TextView txtScreenType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		spinChannels = (Spinner) findViewById(R.id.spinner_channel_list);
		txtChannel = (EditText) findViewById(R.id.txt_channel_name);
		txtSelectedSession = (TextView) findViewById(R.id.txt_selected_session);
		txtScreenType = (TextView) findViewById(R.id.txt_screen_type);
		
		
		if(SessionManager.getInstance().isPersonal()) {
			txtScreenType.setText("Screen Type: Personal");
		}
		else if(!SessionManager.getInstance().isPersonal()) {
			txtScreenType.setText("Screen Type: Shared");
		}
		
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>(this,
												   android.R.layout.
												   simple_list_item_1,
												   listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
		
		SessionManager.getInstance().loadSavedSessionID();
		String session = SessionManager.getInstance().getChosenSession();
		listChannels.add(session);
		
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,
		    						   View selectedItemView, int position,
		    						   long id) {
		    	Log.e("session selected", spinChannels.getItemAtPosition(position).toString());
		    	
		    	SessionManager.getInstance().setChosenSession(spinChannels.getItemAtPosition(position).toString());
		    	txtSelectedSession.setText("Chosen Session: " + SessionManager.getInstance().getChosenSession());
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try{
			PpsManager.getInstance().stop();
		}catch(Exception e ){
			Log.e("PPSMANAGER", "stop failed");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		PpsManager.getInstance().setScreenMode(PpsManager.SESSION_MODE);
		PpsManager.getInstance().start();
		
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	}
	
	public void selectRefreshSessions(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getAvailableSessionsSet())
			nodes += node + ",";
		
		listChannels.clear();
		channelsAdapter.notifyDataSetChanged();
		
		listChannels.addAll(SessionManager.getInstance().getAvailableSessionsSet());
		channelsAdapter.notifyDataSetChanged();

		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void selectRequestSessions(View v) {
		SessionManager.getInstance().requestSessions();
		
		listChannels.clear();
		channelsAdapter.notifyDataSetChanged();
		
		final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	        	listChannels.addAll(SessionManager.getInstance().getAvailableSessionsSet());
	    		channelsAdapter.notifyDataSetChanged();
	        }
	    }, 500);
		
	}
	
	public void selectCreateSession(View v) {
		listChannels.add(SessionManager.getInstance().createSession(txtChannel.getText().toString()));
		channelsAdapter.notifyDataSetChanged();

		txtChannel.setText("");
	}
	
	public void selectProceed(View v) {
		if(SessionManager.getInstance().getAvailableSessionsSet().size() != 0) {
			if(!SessionManager.getInstance().getChosenSession().isEmpty()) {
				if(SessionManager.getInstance().isPersonal()) {
					Intent intent = new Intent(this, PlayPersonalActivity.class);
					PpsManager.getInstance().setScreenMode(PpsManager.GAME_MODE);
					startActivity(intent);
				}
				else if(!SessionManager.getInstance().isPersonal()) {
					Intent intent = new Intent(this, PlaySharedActivity.class);
					PpsManager.getInstance().setScreenMode(PpsManager.GAME_MODE);
					startActivity(intent);
				}
				Log.e("Select Proceed", "Session Name:" + SessionManager.getInstance().getChosenSession());
			}
			else
				Toast.makeText(this, "Please choose a session!", Toast.LENGTH_LONG).show();	
		}
		else
			Toast.makeText(this, "Please create a session!", Toast.LENGTH_LONG).show();	
	}
	
	public void selectLock(View v) {
		SessionManager.getInstance().lockSession(spinChannels.getSelectedItem().toString());
	}
	
	public void selectUnlock(View v) {
		SessionManager.getInstance().unlockSession(spinChannels.getSelectedItem().toString());
	}

}
