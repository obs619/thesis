package com.cardgame.activities;

import com.cardgame.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Sharmaine
 * 
 *		This is the starting page when the app is opened.
 */

public class LobbyActivity extends Activity {
	
	private Button btnPersonal;
	private Button btnShared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
		btnPersonal = (Button) findViewById(R.id.btnLobbyPersonal);
		btnShared = (Button) findViewById(R.id.btnLobbyShared);
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
