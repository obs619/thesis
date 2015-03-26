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
import com.llsx.pps.PpsManager;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.session.SessionManager;

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
		
		
		if(PpsManager.getInstance().isPrivate()) {
			txtScreenType.setText("Screen Type: Personal");
		}
		else if(!PpsManager.getInstance().isPrivate()) {
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
		
		SessionManager.getInstance().loadSavedSessionId();
		String session = SessionManager.getInstance().getChosenSession();
		listChannels.add(session);
		
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,
		    						   View selectedItemView, int position,
		    						   long id) {
		    	Log.e("session selected", spinChannels.getItemAtPosition(position).toString());
		    	
		    	SessionManager.getInstance().joinSession(spinChannels.getItemAtPosition(position).toString());
		    	txtSelectedSession.setText("Current Session: " + SessionManager.getInstance().getCurrentSessionName());
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
		PpsManager.getInstance().setSessionMode(PpsManager.SESSION_MODE);
		PpsManager.getInstance().start();
		
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	}
	
	public void selectRefreshSessions(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getAvailableSessions())
			nodes += node + ",";
		
		listChannels.clear();
		channelsAdapter.notifyDataSetChanged();
		
		listChannels.addAll(SessionManager.getInstance().getAvailableSessions());
		channelsAdapter.notifyDataSetChanged();

		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void selectCreateSession(View v) {
		String createdSession = SessionManager.getInstance().createSession(txtChannel.getText().toString());
		if(createdSession != null)
		{
			listChannels.add(createdSession);
			channelsAdapter.notifyDataSetChanged();
		}
		txtChannel.setText("");
	}
	
	public void selectProceed(View v) {
		if(SessionManager.getInstance().getAvailableSessions().size() != 0) {
			if(!SessionManager.getInstance().getChosenSession().isEmpty()) {
				if(PpsManager.getInstance().isPrivate()) {
					Intent intent = new Intent(this, PlayPersonalActivity.class);
					PpsManager.getInstance().setSessionMode(PpsManager.APP_MODE);
					startActivity(intent);
				}
				else if(!PpsManager.getInstance().isPrivate()) {
					Intent intent = new Intent(this, PlaySharedActivity.class);
					PpsManager.getInstance().setSessionMode(PpsManager.APP_MODE);
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
		String session = SessionManager.getInstance().getChosenSession();
		
		
			SessionManager.getInstance().lockSession(session);
	}
	
	public void selectUnlock(View v) {
		String session = SessionManager.getInstance().getChosenSession();
		
			SessionManager.getInstance().unlockSession(session);
	}

}
