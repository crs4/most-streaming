package org.crs4.most.streaming.test;


import java.util.HashMap;


import org.crs4.most.streaming.*;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

public class MockStreamingLib implements IStream  {
	
	private static final String TAG = "StreamingLibMock";
	private Handler notificationHandler =null;
	private String streamName = null;
	private String uri = null;
	private int latency = 200;
	
	private void notifyState(StreamingEventBundle myStateBundle)
    {
		Log.d(TAG, "Called notifyState for state:" + myStateBundle.getEvent().name());
		switch (myStateBundle.getEvent()){
			case LIB_INITIALIZING: Log.d(TAG, "Streaming Lib initializing"); break;
			case LIB_INITIALIZED: Log.d(TAG, "Streaming Lib initialized"); break;
			case LIB_DEINITIALIZING: Log.d(TAG, "Streaming Lib initializing"); break;
			case LIB_DEINITIALIZED: Log.d(TAG, "Streaming Lib initialized"); break;
			case STREAM_INITIALIZING: Log.d(TAG, "Stream initializing..."); break;
			case STREAM_INITIALIZED: Log.d(TAG, "Stream initialized"); break;
			case STREAM_INITIALIZATION_FAILED: Log.e(TAG, "Stream initialization failed"); break;
			case STREAM_PLAYING: Log.d(TAG, "Stream is playing"); break;
			case STREAM_PAUSED: Log.d(TAG, "Stream is paused"); break;
		default:
			break;}
				
    	Message m = Message.obtain(this.notificationHandler,myStateBundle.getEventType().ordinal(), myStateBundle);
		m.sendToTarget();
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepare(Context context, SurfaceView surface,
			HashMap<String, String> configParams, Handler notificationHandler)
			throws Exception {
		
		if  (!configParams.containsKey("name")) throw new Exception("param name not found in Configuration!");
		if (!configParams.containsKey("uri")) throw new Exception("param uri not found in Configuration!");
		
		this.notificationHandler = notificationHandler;
		this.streamName =   configParams.get("name");
    	this.uri = configParams.get("uri");
    	this.latency = configParams.containsKey("latency") ?  Integer.valueOf(configParams.get("latency")) : 200;
    	
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZING, "Inizializating Streaming Lib", null));
		this.simulatePause(1);
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZED, "Inizialization Ok", null));
	    
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_INITIALIZING, "Inizializating Stream", null));
		this.simulatePause(1);
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_INITIALIZED, "Stream Inizialization Ok", null));
	
	}
	
	private void simulatePause(int secs)
	{
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void play() {
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_PLAYING, "Stream is playing", null));
		
	}

	@Override
	public void pause() {
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_PLAYING, "Stream is playing", null));
		
		
	}

	@Override
	public void setUri(String uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLatency() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void destroy() {
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_DEINITIALIZING, "Deinizializating Stream", null));
		this.simulatePause(1);
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_DEINITIALIZED, "Stream Deinizialization Ok", null));
		
	}

}
