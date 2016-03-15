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
import javax.net.ssl.HandshakeCompletedListener;

import android.content.Intent;
import android.os.*;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.view.SurfaceView;

import it.crs4.most.streaming.IStream;
import it.crs4.most.streaming.StreamingEventBundle;
import it.crs4.most.streaming.StreamingLib;
import it.crs4.most.streaming.enums.StreamState;
import it.crs4.most.streaming.enums.StreamingEvent;
import it.crs4.most.streaming.enums.StreamingEventType;
import it.crs4.most.streaming.test_activity.TestActivity;


public class StreamingLibTestSuite extends ActivityUnitTestCase implements Handler.Callback  {
	
	public StreamingLibTestSuite() {
		super(TestActivity.class);
	}
   
	protected void setUp() throws Exception {
		super.setUp();
		// Starts the MainActivity of the target application
		startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);	
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	 
	abstract class HandlerTest implements Handler.Callback{
		protected int curStateIndex = 0;
		protected StreamState [] expectedStates = {};
		
		public boolean isDone() {
			return curStateIndex>=expectedStates.length;
		}
		
		public abstract boolean handleMessage(Message voipMessage);
	}
	
	
	class StreamHandlerTest extends HandlerTest {
		
		StreamHandlerTest () {
			
			this.expectedStates = new StreamState[] {
					StreamState.INITIALIZING,
					StreamState.INITIALIZED,
					StreamState.PLAYING_REQUEST,
					StreamState.PLAYING,
					StreamState.PAUSED,
					StreamState.DEINITIALIZING,
					StreamState.DEINITIALIZED  //commented for a bug in the MockIStream class
			};
		}
 
		
		@Override
		public boolean handleMessage(Message streamingMessage) {
			//int msg_type = voipMessage.what;
			StreamingEventBundle myEventB = (StreamingEventBundle) streamingMessage.obj;
			
			StreamState streamState =  ((IStream)myEventB.getData()).getState();
			
			String infoMsg = myEventB.getEvent() + ":" + myEventB.getInfo() + " STATE:" + streamState ;
			Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
			
			assertEquals(StreamingEventType.STREAM_EVENT , myEventB.getEventType());
			assertEquals(StreamingEvent.STREAM_STATE_CHANGED , myEventB.getEvent());
			
			
			assertEquals(expectedStates[curStateIndex] , streamState);
			
			curStateIndex++;
			
			     if (streamState==StreamState.INITIALIZED)   myStream.play();
			     else if (streamState==StreamState.PLAYING)  myStream.pause();
			     else if (streamState==StreamState.PAUSED)   myStream.destroy();
			     else if (streamState==StreamState.DEINITIALIZED) {
			    	 Log.d(TAG,"stream destroyed");
			     }
			     
			return false;
		}

	}
	
	/**
	 *  This test calls the prepare() method of the Streaming Library. The testing callback method receives the updated Streaming Events. The test checks if
	 *  the received  Streaming Events and Stream states match with the expected events and states. 
	 */
	public void testStreamingLibInitialization()
	{
		Log.d(TAG, "Testing testStreamingLibInitialization...");
		
		this.streamingLib = new StreamingLibMockBackend();  
		//this.streamingLib = new StreamingLibBackend();
		 
		
		this.configParams = new HashMap<String, String>();
		this._testHandler(new StreamHandlerTest());
	}
	
	
	private static final String TAG = "StreamingTestActivity";
	private Handler handler = new Handler(this);
	private HandlerTest handlerTest = null;
	private StreamingLib streamingLib =null;
	private IStream myStream = null;
	private HashMap<String,String> configParams = null;
 
	private void _testHandler(HandlerTest handlerTest) {
		this.handlerTest= handlerTest;
		this.configParams.put("name", "Stream 1 [Test]");
		this.configParams.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
	    try {
	        this.streamingLib.initLib(getActivity().getApplicationContext());
	        this.myStream = this.streamingLib.createStream(configParams, this.handler);
	        this.myStream.prepare(new SurfaceView(getActivity().getApplicationContext()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			StreamingLibTestSuite.fail("Failed Streaming Lib initialization:" + e1.getMessage());
		}
		Log.d(TAG,"testStreaming with HandlerTest");
		 
 
		while (!handlerTest.isDone())
		{
		 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Log.d(TAG,"test running...please wait....");
		}
		Log.d(TAG,"Test End");
	}

	@Override
	public boolean handleMessage(Message msg) {
		//Log.d(TAG, "Called handleMessage with HandlerTest!!");
		return this.handlerTest.handleMessage(msg);
	 
	}
}
