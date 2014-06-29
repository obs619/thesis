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
package com.cardgame.chord;

import com.samsung.android.sdk.chord.SchordChannel;

/**
 * Adapter for the {@link IChordChannelListener}.
 */
public class IChordChannelListenerAdapter implements SchordChannel.StatusListener {

	@Override
	public void onDataReceived(String fromNode, String fromChannel, String payloadType, byte[][] payload) {
	}

	@Override
	public void onFileChunkReceived(String fromNode, String fromChannel, String fileName, String hash, String fileType,
			String exchangeId, long fileSize, long offset) {
	}

	@Override
	public void onFileChunkSent(String toNode, String toChannel, String fileName, String hash, String fileType,
			String exchangeId, long fileSize, long offset, long chunkSize) {
	}

	@Override
	public void onFileFailed(String node, String channel, String fileName, String hash, String exchangeId, int reason) {
	}

	@Override
	public void onFileReceived(String fromNode, String fromChannel, String fileName, String hash, String fileType,
			String exchangeId, long fileSize, String tmpFilePath) {
	}

	@Override
	public void onFileSent(String toNode, String toChannel, String fileName, String hash, String fileType,
			String exchangeId) {
	}

	@Override
	public void onFileWillReceive(String fromNode, String fromChannel, String fileName, String hash, String fileType,
			String exchangeId, long fileSize) {
	}

	@Override
	public void onNodeJoined(String fromNode, String fromChannel) {
	}

	@Override
	public void onNodeLeft(String fromNode, String fromChannel) {
	}

	@Override
	public void onMultiFilesChunkReceived(String arg0, String arg1,
			String arg2, String arg3, int arg4, String arg5, long arg6,
			long arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesChunkSent(String arg0, String arg1, String arg2,
			String arg3, int arg4, String arg5, long arg6, long arg7, long arg8) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesFailed(String arg0, String arg1, String arg2,
			String arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesFinished(String arg0, String arg1, String arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesReceived(String arg0, String arg1, String arg2,
			String arg3, int arg4, String arg5, long arg6, String arg7) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesSent(String arg0, String arg1, String arg2,
			String arg3, int arg4, String arg5) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMultiFilesWillReceive(String arg0, String arg1, String arg2,
			String arg3, int arg4, String arg5, long arg6) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUdpDataDelivered(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUdpDataReceived(String arg0, String arg1, String arg2,
			byte[][] arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

}
