package com.cardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	
	public void selectAsPersonal(View v) {
		Intent intent = new Intent(this, SessionActivity.class);
		SessionManager.getInstance().setScreenType(PPSManager.PRIVATE);
		setDeviceAlias();
		startActivity(intent);
	}
	
	public void selectAsShared(View v) {
		Intent intent = new Intent(this, SessionActivity.class);
		//ppsManager = new PPSManager(this, false, false);
		SessionManager.getInstance().setScreenType(PPSManager.PUBLIC);
		setDeviceAlias();
		startActivity(intent);
	}
	
	public void setDeviceAlias(){
		TextView mUserNameView;

		mUserNameView = (TextView) findViewById(R.id.txtUserName);
		SessionManager.getInstance().setAlias(mUserNameView.getText().toString());
	}
	
}
