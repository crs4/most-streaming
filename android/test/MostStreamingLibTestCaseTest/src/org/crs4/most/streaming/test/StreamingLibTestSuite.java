package org.crs4.most.streaming.test;


import java.util.HashMap;

import android.content.Intent;
import android.os.*;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.view.SurfaceView;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingEventBundle;
import org.crs4.most.streaming.StreamingFactory;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.test_activity.TestActivity;


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
	
	 
	abstract class HandlerTest {
		protected int curEventIndex = 0;
		protected StreamingEvent [] expectedEvents = {};
		
		public boolean isDone() {
			return curEventIndex>=expectedEvents.length;
		}
		
		public abstract boolean handleMessage(Message voipMessage);

	}
	
	class StreamingLibInitializationHandlerTest extends HandlerTest {
		
		StreamingLibInitializationHandlerTest () {
			
			this.expectedEvents = new StreamingEvent[] {
					
					StreamingEvent.LIB_INITIALIZING , 
					StreamingEvent.LIB_INITIALIZED , 
					StreamingEvent.STREAM_INITIALIZING,
					StreamingEvent.STREAM_INITIALIZED,
					StreamingEvent.STREAM_DEINITIALIZING,
					StreamingEvent.STREAM_DEINITIALIZED,
					StreamingEvent.LIB_DEINITIALIZING,
					StreamingEvent.LIB_DEINITIALIZED};
		}
 
		
		@Override
		public boolean handleMessage(Message voipMessage) {
			//int msg_type = voipMessage.what;
			StreamingEventBundle myEvent = (StreamingEventBundle) voipMessage.obj;
			String infoMsg = myEvent.getEvent() + ":" + myEvent.getInfo();
			Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
			
			assertEquals( expectedEvents[curEventIndex], myEvent.getEvent());
			curEventIndex++;
			     if (myEvent.getEvent()==StreamingEvent.LIB_INITIALIZED)   myStream.destroy();	
			return false;
		}

	}
	
	class StreamHandlerTest extends HandlerTest {
		
		StreamHandlerTest () {
			
			this.expectedEvents = new StreamingEvent[] {
					
					StreamingEvent.LIB_INITIALIZING , 
					StreamingEvent.LIB_INITIALIZED , 
					StreamingEvent.STREAM_INITIALIZING,
					StreamingEvent.STREAM_INITIALIZED,
					StreamingEvent.STREAM_PLAYING,
					StreamingEvent.STREAM_PAUSED,
					StreamingEvent.STREAM_DEINITIALIZING,
					StreamingEvent.STREAM_DEINITIALIZED};
		}
 
		
		@Override
		public boolean handleMessage(Message voipMessage) {
			//int msg_type = voipMessage.what;
			StreamingEventBundle myEvent = (StreamingEventBundle) voipMessage.obj;
			String infoMsg = myEvent.getEvent() + ":" + myEvent.getInfo();
			Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
			
			assertEquals( myEvent.getEvent(), expectedEvents[curEventIndex]);
			curEventIndex++;
			     if (myEvent.getEvent()==StreamingEvent.STREAM_INITIALIZED)   myStream.play();
			     else if (myEvent.getEvent()==StreamingEvent.STREAM_PLAYING)   myStream.pause();
			     else if (myEvent.getEvent()==StreamingEvent.STREAM_PAUSED)   myStream.destroy();
			     
			return false;
		}

	}
	
	/**
	 *  This test calls the prepare() method of the Streaming Library. The testing callback method receives the updated Streaming Events. The test checks if
	 *  the received  Streaming Events match with the expected states. 
	 */
	public void testStreamingLibInitialization()
	{
		Log.d(TAG, "Testing testStreamingLibInitialization...");
		
		this.myStream = new MockStreamingLib();
		//this.myStream = StreamingFactory.getIStream();
		
		this.configParams = new HashMap<String, String>();
		this._testHandler(new StreamingLibInitializationHandlerTest());
	}
	
	
	private static final String TAG = "StreamingTestActivity";
	private Handler handler = new Handler(this);
	private HandlerTest handlerTest = null;
	private IStream myStream =null;
	private HashMap<String,String> configParams = null;
 
	private void _testHandler(HandlerTest handlerTest) {
		this.handlerTest= handlerTest;
		this.configParams.put("name", "Stream 1 [Test]");
		this.configParams.put("uri", "rtp://0.0.0.0:1234/test [Test]");
	    try {
			myStream.prepare(getActivity().getApplicationContext(), new SurfaceView(getActivity().getApplicationContext()),configParams , handler);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			StreamingLibTestSuite.fail("Failed Streaming Lib initialization:" + e1.getMessage());
		}
		Log.d(TAG,"testVoip with HandlerTest");
		 
 
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
