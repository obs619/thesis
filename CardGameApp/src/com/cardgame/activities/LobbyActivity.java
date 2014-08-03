package com.cardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cardgame.R;
import com.cardgame.screenapi.PPSManager;

public class LobbyActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
	}
	
	public void selectAsPersonal(View v) {
		Intent intent = new Intent(this, PlayPersonalActivity.class);
		startActivity(intent);
	}
	
	public void selectAsShared(View v) {
		Intent intent = new Intent(this, PlaySharedActivity.class);
		startActivity(intent);
	}
	
}
