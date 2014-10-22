/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.gstreamer.GStreamer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class StreamingLibBackend implements StreamingLib {

	private static final String TAG = "STREAMINGLIB_BACKEND";
	private static boolean isLibInitialized = false;
	
	static {
    	
	     Log.d(TAG,"Loading native libraries...");
	     
	     Log.d(TAG,"Loading gstreamer_android...");
	     System.loadLibrary("gstreamer_android");
	     
	    }
	
	@Override
	public void initLib(Context context) throws Exception {
		
		if (context==null) throw new IllegalArgumentException("Context parameter cannot be null");
		
		try {
			GStreamer.init(context);
			isLibInitialized = true;
		} catch (FileNotFoundException e) {
			
			Log.w(TAG, "GStreamer.java maybe is trying to copy fonts to assets folder... operation not allowed in to a android library: " + e.getMessage());
			return;
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "GStreamer initialization failed: " + e.getMessage());
		    throw new Exception("Error initializing the GStreamer Backend Lib:" + e.getMessage());
			}
		
	}

	@Override
	public IStream createStream(HashMap<String, String> configParams,
			Handler notificationHandler) throws Exception {
		if (!isLibInitialized) throw new Exception("Lib not initialized yet. Please call initLib(Context context) method first");
		return new GStreamerBackend(configParams, notificationHandler);
	}

}
