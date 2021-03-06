package com.thsst3.snakesandladders;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.session.SessionManager;

public class LobbyActivity extends Activity{
	
	public class SessionEventHandler implements EventHandler{
		@Override
		public void handleEvent(Event event) {
			Log.e("Handling SessionEvent", "Type: "+event.getType() + "Payload: " + event.getPayload().toString());
			switch(event.getType())
			{
			/**
			 * Event.T_ADD_NEW_SESSION - event sent by the API, allows developers to handle newly created session
			 */
			case Event.T_ADD_NEW_SESSION:
				listChannels.add(event.getSession());
				channelsAdapter.notifyDataSetChanged();
				break;
			}
		}

	}
	
	private Spinner spinChannels;
	public List<String> listChannels;
	public ArrayAdapter<String> channelsAdapter;
	
	private EditText txtChannel;
	private TextView txtSelectedSession;
	private TextView txtDeviceName;
	
	private RelativeLayout layoutCreate;
	private LinearLayout layoutLockUnlock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		spinChannels = (Spinner) findViewById(R.id.spinner_channel_list);
		txtChannel = (EditText) findViewById(R.id.txt_channel_name);
		txtSelectedSession = (TextView) findViewById(R.id.txt_selected_session);
		txtDeviceName = (TextView) findViewById(R.id.txt_device_name);
		layoutCreate = (RelativeLayout) findViewById(R.id.layoutCreate);
		layoutLockUnlock = (LinearLayout) findViewById(R.id.layoutLockUnlock);
		
		
		// Use isPrivate() from PpsManager instance to check device screen type, if private hide layout create and lock unlock	
		if(PpsManager.getInstance().isPrivate()) {
			layoutCreate.setVisibility(View.GONE);
			layoutLockUnlock.setVisibility(View.GONE);
		}
		else if(!PpsManager.getInstance().isPrivate()) {
			layoutCreate.setVisibility(View.VISIBLE);
			layoutLockUnlock.setVisibility(View.VISIBLE);
		}
		
	
		/**
		 * Set the device's event handler
		 */
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	
		/**
		 * set txtDeviceName to hi name
		 */
		txtDeviceName.setText("Hi, " + SessionManager.getInstance().getOwnDeviceName() + "!");
		
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
		
		
		/**
		 * load last chosen session from SessionManager, after device turned off
		 * get session to join and add to listChannels
		 * set text txtSelectedSession to chosen session
		 */
		SessionManager.getInstance().loadSavedSessionId();
		String session = SessionManager.getInstance().getSessionToJoin();
		listChannels.add(session);
		txtSelectedSession.setText("Chosen Session: " + session);
		
		
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {

		    	/**
		    	 * join session
		    	 * 
		    	 */
		    	SessionManager.getInstance().joinSession(spinChannels.getItemAtPosition(position).toString());
		    	
		    	
		    	txtSelectedSession.setText("Chosen Session: " + SessionManager.getInstance().getSessionToJoin());
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * stop ppsmanager
		 */
		PpsManager.getInstance().stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		/**
		 * start ppsmanager
		 */
		PpsManager.getInstance().start();
		
		
		/**
		 * reset event handler - when the user presses back
		 */
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
		
		/**
		 * refresh list of sessions
		 */
		listChannels.clear();
		channelsAdapter.notifyDataSetChanged();
		
		listChannels.addAll(SessionManager.getInstance().getAvailableSessions());
		channelsAdapter.notifyDataSetChanged();
	}
	
	public void selectCreateSession(View v) {
		/**
		 * call createSession along with txt from txtChannel
		 * If created session is not null, add to listChannels
		 */
		String createdSession = SessionManager.getInstance().createSession(txtChannel.getText().toString());
		
		if(createdSession != null)
		{
			listChannels.add(createdSession);
			channelsAdapter.notifyDataSetChanged();
		}
		
		txtChannel.setText("");
	}
	
	
	public void selectLock(View v) {
		/**
		 * lock chosen session
		 */
		String session = SessionManager.getInstance().getSessionToJoin();
		SessionManager.getInstance().lockSession(session);
	}
	
	public void selectUnlock(View v) {
		/**
		 * unlock chosen session
		 */
		String session = SessionManager.getInstance().getSessionToJoin();
		SessionManager.getInstance().unlockSession(session);
	}

	
	public void selectJoin(View v) {
		/**
		 * if private device, move to playeractivity and set sessionmode to app mode
		 * if public device, move to boardactivity and set session mode to app mode
		 */
		if(PpsManager.getInstance().isPrivate()) {
			Intent intent = new Intent(this, PlayerActivity.class);
			PpsManager.getInstance().setSessionMode(PpsManager.APP_MODE);
			startActivity(intent);
		}
		else if(!PpsManager.getInstance().isPrivate()) {
			Intent intent = new Intent(this, BoardActivity.class);
			PpsManager.getInstance().setSessionMode(PpsManager.APP_MODE);
			startActivity(intent);
		}
						
	}
	
}
