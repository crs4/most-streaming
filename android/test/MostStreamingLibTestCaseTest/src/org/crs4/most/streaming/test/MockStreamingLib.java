package org.crs4.most.streaming.test;


import java.util.HashMap;


import org.crs4.most.streaming.*;
import org.crs4.most.streaming.enums.StreamState;
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
	private StreamState streamState = StreamState.DEINITIALIZED;
	
	private void notifyState(StreamingEventBundle myStateBundle)
    {
		Log.d(TAG, "Called notifyState for state:" + myStateBundle.getEvent().name());
		StreamingEvent myEvent =   myStateBundle.getEvent();
		if (myEvent==StreamingEvent.STREAM_STATE_CHANGED) {
			
		switch ((StreamState) myStateBundle.getData()){
			case INITIALIZING:  Log.d(TAG, "Stream has being initialized"); break;
			case INITIALIZED: Log.d(TAG, "Stream initialized"); break;
			case PLAYING: Log.d(TAG, "Stream is playing"); break;
			case PAUSED: Log.d(TAG, "Stream is paused"); break;
			case DEINITIALIZING:  Log.d(TAG, "Stream has being deinitialized"); break;
			case DEINITIALIZED:  Log.d(TAG, "Stream is deinitialized"); break;
		default:
			break;}
		}
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
		
		//if (context==null) throw new IllegalArgumentException("Context parameter cannot be null");
    	if (notificationHandler==null) throw new IllegalArgumentException("Handler parameter cannot be null");
    	//if (surface==null) throw new IllegalArgumentException("Surface parameter cannot be null");
		if  (!configParams.containsKey("name")) throw new Exception("param name not found in Configuration!");
		if (!configParams.containsKey("uri")) throw new Exception("param uri not found in Configuration!");
		
		this.notificationHandler = notificationHandler;
		this.streamName =   configParams.get("name");
		this.streamState = StreamState.INITIALIZING;
    	this.uri = configParams.get("uri");
    	this.latency = configParams.containsKey("latency") ?  Integer.valueOf(configParams.get("latency")) : 200;
    	
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Inizializating Stream", this.streamState));
		this.simulatePause(1);
		this.streamState = StreamState.INITIALIZED;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream Inizialization Ok",  this.streamState));
	
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
		this.streamState = StreamState.PLAYING;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream is playing", this.streamState));
		
	}

	@Override
	public void pause() {
		this.streamState = StreamState.PAUSED;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream is paused", this.streamState));
		
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
		this.streamState = StreamState.DEINITIALIZING;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Deinizializating Stream", this.streamState));
		this.simulatePause(1);
		this.streamState = StreamState.DEINITIALIZED;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Deinizializating Stream", this.streamState));
	}

	@Override
	public StreamState getState() {
		
		return this.streamState;
	}

}
