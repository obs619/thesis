package com.cardgame.gameengine;

import java.util.ArrayList;
import java.util.HashMap;

import com.cardgame.screenapi.PPSManager;
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
	PPSManager spsManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		spsManager=new PPSManager(null);
		
		//On initialize, choose whether public or private screen
		//if public screen and game has not started, show Start Game button
		//if private screen and game has not started, display cards but do not display Play Card button
		//if public screen and game has started, display cards that have been played <--consider late-joining public screens
		
	}

}
