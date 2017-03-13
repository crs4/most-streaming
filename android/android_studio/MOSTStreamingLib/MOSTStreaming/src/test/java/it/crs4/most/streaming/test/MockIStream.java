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
import java.util.Timer;
import java.util.TimerTask;


import it.crs4.most.streaming.*;
import it.crs4.most.streaming.enums.StreamProperty;
import it.crs4.most.streaming.enums.StreamState;
import it.crs4.most.streaming.enums.StreamingEvent;
import it.crs4.most.streaming.enums.StreamingEventType;
import it.crs4.most.streaming.utils.Size;

//import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MockIStream implements IStream  {
	
	private static final String TAG = "StreamingLibMock";
	private Handler notificationHandler =null;
	private String streamName = null;
	private String uri = null;
	private int latency = 200;
	private StreamState streamState = StreamState.DEINITIALIZED;
	
	private void notifyState(StreamingEventBundle myStateBundle)
    {
		Log.d(TAG, "Called notifyState for event:" + myStateBundle.getEvent().name());
		StreamingEvent myEvent =   myStateBundle.getEvent();
		if (myEvent==StreamingEvent.STREAM_STATE_CHANGED) {
	    StreamState streamState = ((IStream) myStateBundle.getData()).getState();
		switch (streamState){
			case INITIALIZING: Log.d(TAG, "Stream has being initializing"); break;
			case INITIALIZED: Log.d(TAG, "Stream initialized"); break;
			case PLAYING_REQUEST: Log.d(TAG, "Stream is preparing to play"); break;
			case PLAYING: Log.d(TAG, "Stream is playing"); break;
			case PAUSED: Log.d(TAG, "Stream is paused"); break;
			case DEINITIALIZING: Log.d(TAG, "Stream has being deinitialized"); break;
			case DEINITIALIZED:  Log.d(TAG, "Stream is deinitialized"); this.streamState = StreamState.DEINITIALIZED; break;
		default:
			break;}
		}
		
		Log.d(TAG, "Sending event with stream state:" + this.streamState);
    	Message m = Message.obtain(this.notificationHandler,myStateBundle.getEventType().ordinal(), myStateBundle);
		m.sendToTarget();
    }

	@Override
	public String getName() {
		
		return this.streamName;
	}

	public MockIStream(
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
	}
	
	private void changeStateAfter(int secs, StreamState newState, String msg)
	{
	
		final StreamState toUpdateState = newState;
		final String eventMsg = msg;
		
		new Timer().schedule(new TimerTask(){
			
		  @Override
		  public void run() {
			Log.d(TAG, "Inside changeStateAfter----");
		    MockIStream.this.streamState = toUpdateState;
		    notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, eventMsg, MockIStream.this));
			
		  }
		},  secs*1000);
		
	
	}

	@Override
	public void play() {
		this.streamState = StreamState.PLAYING_REQUEST;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream is going to play", this));
		this.changeStateAfter(1, StreamState.PLAYING, "Stream is playing");
	}

	@Override
	public void pause() {
		this.streamState = StreamState.PAUSED;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream is paused", this));
		
	}


	@Override
	public void destroy() {
		this.streamState = StreamState.DEINITIALIZING;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Deinizializating Stream", this));
		this.changeStateAfter(1, StreamState.DEINITIALIZED, "Stream deinitialized");
		//this.streamState = StreamState.DEINITIALIZED;
		//this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream deinitialized", this));
	}

	@Override
	public StreamState getState() {
		return this.streamState;
	}

	@Override
	public void prepare(SurfaceView surface) {
		this.streamState = StreamState.INITIALIZING;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Inizializating Stream", this));
		this.changeStateAfter(1, StreamState.INITIALIZED, "Stream Inizialization Ok");
		//this.streamState = StreamState.INITIALIZED;
		//this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream Inizialization Ok",  this));
	}

	@Override
	public void prepare(SurfaceView surface, boolean frameCallback) {

	}

	@Override
	public Size getVideoSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(StreamProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSurface(SurfaceHolder surface) {

	}

	@Override
	public boolean commitProperties(StreamProperties properties) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadStillImage(String uri) {
		return true;
	}

	@Override
	public String getErrorMsg() {
		
		return "";
	}

	@Override
	public void addFrameListener(IFrameListener listener){

	}

	@Override
	public void removeFrameListener(IFrameListener listener){

	}

}
