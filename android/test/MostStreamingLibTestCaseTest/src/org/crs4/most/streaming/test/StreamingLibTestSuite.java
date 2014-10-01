package org.crs4.most.streaming.test;


import android.content.Intent;
import android.os.*;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingEventBundle;
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
		myStream = new MockStreamingLib();
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
					StreamingEvent.LIB_DEINITIALIZING,
					StreamingEvent.LIB_DEINITIALIZED};
		}
 
		
		@Override
		public boolean handleMessage(Message voipMessage) {
			//int msg_type = voipMessage.what;
			StreamingEventBundle myEvent = (StreamingEventBundle) voipMessage.obj;
			String infoMsg = myEvent.getEvent() + ":" + myEvent.getInfo();
			Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
			
			assertEquals( myEvent.getEvent(), expectedEvents[curEventIndex]);
			curEventIndex++;
			     if (myEvent.getEvent()==StreamingEvent.LIB_INITIALIZED)   myStream.destroy();	
			
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
		this._testHandler(new StreamingLibInitializationHandlerTest());
	}
	
	
	private static final String TAG = "StreamingTestActivity";
	private Handler handler = new Handler(this);
	private HandlerTest handlerTest = null;
	private IStream myStream =null;
 
	private void _testHandler(HandlerTest handlerTest) {
		this.handlerTest= handlerTest;
	    try {
			myStream.prepare(null, null, null, this.handler);
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
