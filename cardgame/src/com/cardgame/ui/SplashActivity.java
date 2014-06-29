package com.cardgame.ui;

import com.srpol.chordchat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity{


	public CharSequence[] screen_type_list = {"Private","Public"};
	public static String screenType = "Private";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		//pop alertdialog which asks user to choose between private/public
		AlertDialog.Builder builder=new AlertDialog.Builder(this)
		.setTitle("Choose screen type")
		.setItems(screen_type_list, new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				screenType = screen_type_list[which].toString();
				
				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
			}
		});
	
		builder.setCancelable(false);
		builder.create().show();
	}
	
	
}
