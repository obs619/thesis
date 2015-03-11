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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;
import com.llsx.pps.event.EventManager;
import com.llsx.pps.session.SessionManager;

public class SessionActivity extends Activity{
	
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
		
		/**
		 * Use isPrivate() from PpsManager instance to check device screen type
		 */
		if(PpsManager.getInstance().isPrivate())
			txtScreenType.setText("Screen Type: Personal");
		else if(!PpsManager.getInstance().isPrivate()) 
			txtScreenType.setText("Screen Type: Shared");
		
	
		/**
		 * Set the device's event handler
		 */
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	
		
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
		
		
		/**
		 * load last chosen session, after device turned off
		 */
		SessionManager.getInstance().loadSavedSessionId();
		String session = SessionManager.getInstance().getChosenSession();
		listChannels.add(session);
		
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {

		    	/**
		    	 * set the chosen session
		    	 */
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
		 * reset session mode and event handler - when the user presses back from the app mode
		 */
		PpsManager.getInstance().setSessionMode(PpsManager.SESSION_MODE);
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	}
	
	public void selectCreateSession(View v) {
		/**
		 * call createSession along with txt from txtChannel
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
		String session = SessionManager.getInstance().getChosenSession();
		SessionManager.getInstance().lockSession(session);
	}
	
	public void selectUnlock(View v) {
		/**
		 * unlock chosen session
		 */
		String session = SessionManager.getInstance().getChosenSession();
		SessionManager.getInstance().unlockSession(session);
	}

	
	public void selectProceed(View v) {
		/**
		 * check if device has a chosen session for the app
		 * if private device, move to playeractivity and set sessionmode to app mode
		 * if public device, move to boardactivity and set session mode to app mode
		 */
		if(!SessionManager.getInstance().getChosenSession().isEmpty()) {
			
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
		else
			Toast.makeText(this, "Please choose a session!", Toast.LENGTH_LONG).show();	
		
	}
	
}
