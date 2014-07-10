package com.cardgame.gameengine;

import java.util.ArrayList;
import java.util.HashMap;

import com.cardgame.screenapi.SPSManager;
import com.cardgame.uiOLD.SplashActivity;
import com.google.common.collect.Maps;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.chord.Schord;
import com.srpol.chordchat.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class CardGameActivity extends Activity {
	SPSManager spsManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		spsManager=new SPSManager();
		
	}

}
