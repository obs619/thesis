package com.cardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cardgame.R;
import com.llsx.pps.PpsManager;
import com.llsx.pps.session.SessionManager;

public class LobbyActivity extends Activity {
	public static PpsManager ppsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
	}
	
	public void selectAsPrivate(View v) {
		openNextPage(PpsManager.PRIVATE);
	}
	
	public void selectAsPublic(View v) {
		openNextPage(PpsManager.PUBLIC);
	}
	
	public void openNextPage(boolean isPrivate) {
		Intent intent = new Intent(this, SessionActivity.class);
		ppsManager = new PpsManager(this, isPrivate, PpsManager.SESSION_MODE);
		
		TextView mUserNameView;
		mUserNameView = (TextView) findViewById(R.id.txtUserName);
		SessionManager.getInstance().setDeviceName(mUserNameView.getText().toString());
		
		startActivity(intent);
	}
	
}
