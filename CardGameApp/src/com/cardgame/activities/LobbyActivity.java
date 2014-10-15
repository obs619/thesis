package com.cardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cardgame.R;
import com.cardgame.screenapi.PPSManager;
import com.cardgame.screenapi.session.SessionManager;

public class LobbyActivity extends Activity {
	public static PPSManager ppsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
	}
	
	public void selectAsPrivate(View v) {
		openNextPage(PPSManager.PRIVATE);
	}
	
	public void selectAsPublic(View v) {
		openNextPage(PPSManager.PUBLIC);
	}
	
	public void openNextPage(boolean isPrivate) {
		Intent intent = new Intent(this, SessionActivity.class);
		ppsManager = new PPSManager(this, isPrivate, PPSManager.SESSION_MODE);
		
		TextView mUserNameView;
		mUserNameView = (TextView) findViewById(R.id.txtUserName);
		SessionManager.getInstance().setAlias(mUserNameView.getText().toString());
		
		startActivity(intent);
	}
	
}
