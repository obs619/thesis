package com.cardgame.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cardgame.R;
import com.cardgame.handlers.SessionEventHandler;
import com.cardgame.screenapi.EventManager;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.SessionManager;
import com.cardgame.screenapi.chordimpl.ChordNetworkManager;

public class SessionActivity extends Activity{
	
	private PPSManager spsManager;
	
	private Spinner spinChannels;
	public static List<String> listChannels;
	public static ArrayAdapter<String> channelsAdapter;
	
	private EditText txtChannel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
		
		spinChannels = (Spinner) findViewById(R.id.spinner_channel_list);
		txtChannel = (EditText) findViewById(R.id.txt_channel_name);
		
		
		if(SessionManager.getInstance().isPersonal())
			spsManager = new PPSManager(this, true, true);
		else if(!SessionManager.getInstance().isPersonal())
			spsManager = new PPSManager(this, false, true);
		
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
		
		listChannels = new ArrayList<String>();
		
		channelsAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_list_item_1, listChannels);
		
		spinChannels.setAdapter(channelsAdapter);
		channelsAdapter.notifyDataSetChanged();
	
		spinChannels.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	Log.e("item selected",spinChannels.getItemAtPosition(position).toString());
		    	SessionManager.getInstance().setChosenSession(spinChannels.getItemAtPosition(position).toString());
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    	
		    }
		});
		
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
		
		if(SessionManager.getInstance().isPersonal())
			spsManager = new PPSManager(this, true, true);
		else if(!SessionManager.getInstance().isPersonal())
			spsManager = new PPSManager(this, false, true);
		
		EventManager.getInstance().setEventHandler(new SessionEventHandler());
	}
	
	public void selectGetCurChannel(View v) {
		String nodes = "";
		for(String node: SessionManager.getInstance().getAvailableSessionsList())
			nodes += node + ",";
		
		listChannels.clear();
		channelsAdapter.notifyDataSetChanged();
		
		listChannels.addAll(SessionManager.getInstance().getAvailableSessionsList());
		channelsAdapter.notifyDataSetChanged();
		
		Toast.makeText(this, nodes, Toast.LENGTH_LONG).show();
	}
	
	public void selectCreateChannel(View v) {
		listChannels.add(txtChannel.getText().toString());
		channelsAdapter.notifyDataSetChanged();

		SessionManager.getInstance().sendNewSessionNotification(txtChannel.getText().toString());
		
		txtChannel.setText("");
	}
	
	public void selectProceed(View v) {
		if(SessionManager.getInstance().getAvailableSessionsList().size() != 0) {
			if(SessionManager.getInstance().isPersonal()) {
				Intent intent = new Intent(this, PlayPersonalActivity.class);
				startActivity(intent);
			}
			else if(!SessionManager.getInstance().isPersonal()) {
				Intent intent = new Intent(this, PlaySharedActivity.class);
				startActivity(intent);
			}
		}
		else
			Toast.makeText(this, "Please create a session!", Toast.LENGTH_LONG).show();
				
	}

}
