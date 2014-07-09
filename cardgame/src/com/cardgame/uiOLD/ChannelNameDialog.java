/*
 ********************************************************************************
 * Copyright (c) 2013 Samsung Electronics, Inc.
 * All rights reserved.
 *
 * This software is a confidential and proprietary information of Samsung
 * Electronics, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Samsung Electronics.
 ********************************************************************************
 */
package com.cardgame.uiOLD;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.srpol.chordchat.R;

/**
 * {@link DialogFragment} used for entering player's name.
 */
public class ChannelNameDialog extends DialogFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.channel_name_fragment, container);
		final TextView userNameText = (TextView) view.findViewById(R.id.user_name_text_view);
		view.findViewById(R.id.user_name_ok_button).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String name = userNameText.getText().toString().trim();
				if (name.length() > 0) {
					((OnAddChannelListener) getActivity()).onAddChannel(name);
					dismiss();
				}
			}
			
		});
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}
	
	public interface OnAddChannelListener {
		
		void onAddChannel(String channelName);
		
	}
}
