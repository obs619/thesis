package com.cardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cardgame.R;
import com.cardgame.screenapi.SessionManager;

public class LobbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
	}
	
	public void selectAsPersonal(View v) {
		Intent intent = new Intent(this, SessionActivity.class);
		SessionManager.getInstance().setScreenType(true);
		setDeviceAlias();
		startActivity(intent);
	}
	
	public void selectAsShared(View v) {
		Intent intent = new Intent(this, SessionActivity.class);
		SessionManager.getInstance().setScreenType(false);
		setDeviceAlias();
		startActivity(intent);
	}
	
	public void setDeviceAlias(){
		TextView mUserNameView;

		mUserNameView = (TextView) findViewById(R.id.txtUserName);
		SessionManager.getInstance().setAlias(mUserNameView.getText().toString());
	}
	
}
