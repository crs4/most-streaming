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
	
	private void notifyState(StreamingEventBundle myStateBundle)
    {
		Log.d(TAG, "Called notifyState for state:" + myStateBundle.getEvent().name());
		switch (myStateBundle.getEvent()){
			case LIB_INITIALIZING: Log.d(TAG, "Streaming Lib initializing"); break;
			case LIB_INITIALIZED: Log.d(TAG, "Streaming Lib initialized"); break;
			case LIB_DEINITIALIZING: Log.d(TAG, "Streaming Lib initializing"); break;
			case LIB_DEINITIALIZED: Log.d(TAG, "Streaming Lib initialized"); break;
			
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
		
		this.notificationHandler = notificationHandler;
		
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZING, "Inizializating Streaming Lib", null));
		this.simulatePause(1);
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZED, "Inizialization Ok", null));
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
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
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_DEINITIALIZING, "Deinizializating Streaming Lib", null));
		this.simulatePause(1);
		
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_DEINITIALIZED, "Deinizialization Ok", null));
		
		
	}

}
