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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.llsx.pps.PpsManager;
import com.llsx.pps.event.Event;
import com.llsx.pps.event.EventHandler;

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
		
		//Use isPrivate() from PpsManager instance to check device screen type, if private hide layout create and lock unlock
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
		
		
		/**
		 * set txtDeviceName to hi name
		 */
		
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
		
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView,View selectedItemView, int position, long id) {

		    	/**
		    	 * join session
		    	 * set txtSelectedSession text to session to join
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
		 * reset event handler - when the user presses back
		 */
		
		
		/**
		 * refresh list of sessions
		 */
		
	}
	
	public void selectCreateSession(View v) {
		/**
		 * call createSession along with txt from txtChannel
		 * If created session is not null, add to listChannels
		 * set txtChannel text to empty afterwards
		 */
		
	}
	
	
	public void selectLock(View v) {
		/**
		 * lock chosen session(session to join)
		 */
	}
	
	public void selectUnlock(View v) {
		/**
		 * unlock chosen session(session to join)
		 */
	}

	
	public void selectJoin(View v) {
		/**
		 * if private device, move to playeractivity and set sessionmode to app mode
		 * if public device, move to boardactivity and set session mode to app mode
		 */
						
	}
	
}
