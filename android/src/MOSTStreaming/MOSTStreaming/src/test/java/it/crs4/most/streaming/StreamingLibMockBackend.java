/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */



package it.crs4.most.streaming.test;

import java.util.HashMap;

import it.crs4.most.streaming.IStream;
import it.crs4.most.streaming.StreamingLib;

import android.content.Context;
import android.os.Handler;

public class StreamingLibMockBackend implements StreamingLib {

	private boolean initialized = false;
	@Override
	public void initLib(Context context) throws Exception {
		this.initialized = true;
	}

	@Override
	public IStream createStream(HashMap<String, String> configParams,
			Handler notificationHandler) throws Exception {
		if (!initialized) throw new Exception("Library no initialized yet");
		else
		return new MockIStream(configParams, notificationHandler);
	}
  
}
