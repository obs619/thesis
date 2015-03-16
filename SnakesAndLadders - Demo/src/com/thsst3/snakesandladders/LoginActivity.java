package com.thsst3.snakesandladders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.llsx.pps.PpsManager;
import com.llsx.pps.session.SessionManager;

public class LoginActivity extends Activity {
	
	/**
	 * Instantiate PpsManager
	 */
	
	private EditText txtUsername;
	private RadioGroup radioGroup;
	private RadioButton radioDeviceButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		txtUsername = (EditText) findViewById(R.id.txtUserName);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroupType);
	}
	
	public void clickLogin(View v) {
		
		if(!txtUsername.getText().toString().isEmpty()) {
			int selectedRadioId = radioGroup.getCheckedRadioButtonId();
			radioDeviceButton = (RadioButton) findViewById(selectedRadioId);
			
			if(radioDeviceButton.getText().equals("Player"))
				openNextPage(PpsManager.PRIVATE);
			else if(radioDeviceButton.getText().equals("Board"))
				openNextPage(PpsManager.PUBLIC);
		}else
			Toast.makeText(this, "Please enter your username", Toast.LENGTH_LONG).show();
		
	}
	
	public void openNextPage(boolean isPrivate) {
		Intent intent = new Intent(this, LobbyActivity.class);
		/**
		 * instantiate PpsManager
		 */
		
		TextView mUserNameView;
		mUserNameView = (TextView) findViewById(R.id.txtUserName);
		SessionManager.getInstance().setDeviceName(mUserNameView.getText().toString());
		
		startActivity(intent);
	}
	
}
