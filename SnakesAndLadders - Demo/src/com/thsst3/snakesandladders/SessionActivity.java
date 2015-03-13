package com.thsst3.snakesandladders;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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

import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;

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
		
	
		/**
		 * Set the device's event handler
		 */
	
		
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
		
		
		/**
		 * load last chosen session from SessionManager, after device turned off
		 * get chosen session and add to listChannels
		 */
		
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {

		    	/**
		    	 * set the chosen session
		    	 * set txtSelectedSession with chosen session from sessionmanager
		    	 * 
		    	 */
		    	
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		/**
		 * start ppsmanager
		 */
		
		
		/**
		 * reset session mode and event handler - when the user presses back from the app mode
		 */
	}
	
	public void selectCreateSession(View v) {
		/**
		 * call createSession along with txt from txtChannel
		 * If created session is not null, add to listChannels
		 */
		
		txtChannel.setText("");
	}
	
	
	public void selectLock(View v) {
		/**
		 * lock chosen session
		 */
	}
	
	public void selectUnlock(View v) {
		/**
		 * unlock chosen session
		 */
	}

	
	public void selectProceed(View v) {
		/**
		 * check if chosen session is not empty, then
		 * if private device, move to playeractivity and set sessionmode to app mode
		 * if public device, move to boardactivity and set session mode to app mode
		 */
		
		
	}
	
}
