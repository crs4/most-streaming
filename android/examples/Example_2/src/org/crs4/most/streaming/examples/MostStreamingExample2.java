/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming.examples;

import java.util.HashMap;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingEventBundle;
import org.crs4.most.streaming.StreamingFactory;
import org.crs4.most.streaming.StreamingLib;
import org.crs4.most.streaming.StreamingLibBackend;
import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * This example shows how to simultaneously play two streams into an activity. You can also rotate the device, so the activity
 * along with the layout and the stream are automatically recreated.  
 *  
 *
 */
public class MostStreamingExample2 extends ActionBarActivity implements Handler.Callback, IStreamFragmentCommandListener  {
	
	private static final String TAG = "Example2_MainActivity";
	

	private Handler handler;
	private TextView txtView = null;
	
	private IStream stream1 = null;
	private IStream stream2 = null;
	StreamViewerFragment stream1Fragment = null;
	StreamViewerFragment stream2Fragment = null;
	
	private boolean exitFromAppRequest = false;
	 
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if (savedInstanceState == null)
        {
         try {
        	this.handler = new Handler(this);
        	this.txtView = (TextView) this.findViewById(R.id.textview_message);
        	
        	ImageButton exitButton = (ImageButton) this.findViewById(R.id.button_exit);
        	exitButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					exitFromApp();
				}
			});
        	
        	// Instance and initialize the Streaming Library
        	
        	StreamingLib streamingLib = new StreamingLibBackend();
        	
        	// First of all, initialize the library 
        	streamingLib.initLib(this.getApplicationContext());
        	
        	
        	// Instance the first stream
        	HashMap<String,String> stream1_params = new HashMap<String,String>();
        	stream1_params.put("name", "Stream_1");
        	stream1_params.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
        	this.stream1 = streamingLib.createStream(stream1_params, this.handler);
        	Log.d(TAG,"STREAM 1 INSTANCE");
        	// Instance the first StreamViewer fragment where to render the first stream by passing the stream name as its ID.
        	this.stream1Fragment = StreamViewerFragment.newInstance(stream1.getName());
        	
        	// add the first fragment to the first container
        	FragmentTransaction fragmentTransaction = getFragmentManager()
    				.beginTransaction();
    		fragmentTransaction.add(R.id.container_stream_1,
    				stream1Fragment);
    		fragmentTransaction.commit();
        	
    		// Instance the second stream
        	HashMap<String,String> stream2_params = new HashMap<String,String>();
        	stream2_params.put("name", "Stream_2");
        	stream2_params.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
        	Log.d(TAG,"STREAM 2 INSTANCE");
        	this.stream2 = streamingLib.createStream(stream2_params, this.handler);
        	
        	// Instance the second StreamViewer fragment where to render the second stream by passing the stream name as its ID.
        	this.stream2Fragment = StreamViewerFragment.newInstance(stream2.getName());
        	
        	// add the second fragment to the second container
        	fragmentTransaction = getFragmentManager()
    				.beginTransaction();
    		fragmentTransaction.add(R.id.container_stream_2,
    				stream2Fragment);
    		fragmentTransaction.commit();
        	
       
			} catch (Exception e) {
				e.printStackTrace();
				txtView.setText("Error initializing the streams:" + e.getMessage());
			}
        }
    }

	
	@Override
	// handle all events triggered from the streaming library
	public boolean handleMessage(Message streamingMessage) {
    	
    	// The bundle containing all available informations and resources about the incoming event
		StreamingEventBundle myEvent = (StreamingEventBundle) streamingMessage.obj;
		
		String infoMsg ="Event Type:" +  myEvent.getEventType() + " ->" +  myEvent.getEvent() + ":" + myEvent.getInfo();
		Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
		
		
		
		// for simplicity, in this example we only handle events of type STREAM_EVENT
		if (myEvent.getEventType()==StreamingEventType.STREAM_EVENT && myEvent.getEvent()== StreamingEvent.STREAM_STATE_CHANGED)
			{
			    // All events of type STREAM_EVENT provide a reference to the stream that triggered it.
			    // In this case we are handling two streams, so we need to check what stream triggered the event.
			    // Note that we are only interested to the new state of the stream
				IStream stream  =  (IStream) myEvent.getData();
			
				// notify the user about the event and the new Stream state.
				this.txtView.setText("Stream:"  + stream.getName() + "  State:" + stream.getState().toString());
			    
				 switch(stream.getState())
				 {
				 	case INITIALIZING: txtView.setText("Stream:" + stream.getName() + " HAS BEING INITIALIZED"); break;
				 	case INITIALIZED:  txtView.setText("Stream:" + stream.getName() + " INITIALIZED"); break;
				 	case PLAYING: txtView.setText("Stream:" + stream.getName() + " IS PLAYING"); break;
				 	case PAUSED: txtView.setText("Stream:" + stream.getName() + " IS PAUSED"); break;
				 	case DEINITIALIZING: txtView.setText("Stream:" + stream.getName() + " HAS BEING DEINITIALIZED"); break;
				 	case DEINITIALIZED: txtView.setText("Stream:" + stream.getName() + " IS DEINITIALIZED"); 
				 	Log.d(TAG, "Stream deinitialized. All destroyed ?" + areStreamsDeinitialized() + " Exit request:" + exitFromAppRequest);
				    if (areStreamsDeinitialized() && exitFromAppRequest==true) finish();
				 	break;
				 }
			   
			}
		     
		return false;
	}

	@Override
	public void onPlay(String streamId) {
		Log.d(TAG, "Called onPlay request for stream:" + streamId);
		if (streamId.equals(this.stream1.getName()))
		{
			// we play the stream only if it's state is INITIALIZED or PAUSED
			if (this.stream1.getState()==StreamState.INITIALIZED || this.stream1.getState()==StreamState.PAUSED)
			{
				Log.d(TAG, "Trying to play stream:" + streamId);
				this.stream1.play();
			}
		}
		else if (streamId.equals(this.stream2.getName()))
		{
			 
			if (this.stream2.getState()==StreamState.INITIALIZED || this.stream2.getState()==StreamState.PAUSED)
			{
				Log.d(TAG, "Trying to play stream:" + streamId);
				this.stream2.play();
			}
		}
		
	}

	@Override
	public void onPause(String streamId) {
		Log.d(TAG, "Called onPause request for stream:" + streamId);
		if (streamId.equals(this.stream1.getName()))
		{
			if (this.stream1.getState()==StreamState.PLAYING)
			{
				Log.d(TAG, "Trying to pause stream:" + streamId);
				this.stream1.pause();
			}
		}
		else if (streamId.equals(this.stream2.getName()))
		{
			if (this.stream2.getState()==StreamState.PLAYING)
			{
				Log.d(TAG, "Trying to pause stream:" + streamId);
				this.stream2.pause();
			}
		}
		
	}
	
	private void exitFromApp() {
		this.exitFromAppRequest = true;
		this.destroyStreams();
	}
	
	/**
	 *  
	 * @return true if all the streams have been deinitialized; false otherwise
	 */
	private boolean areStreamsDeinitialized()
	{
		return ( (this.stream1==null || this.stream1.getState()==StreamState.DEINITIALIZED) &&
				 (this.stream2==null || this.stream2.getState()==StreamState.DEINITIALIZED));
	}
	
	private void destroyStreams()
	{
		Log.d(TAG, "Destroy the streams");
		if (this.stream1!=null)
    		this.stream1.destroy();
    	if (this.stream2!=null)
    		this.stream2.destroy();
	}
	protected void onDestroy() {
    	Log.d(TAG, "CALLED ON DESTROY!");
    	// Remember to destroy the current stream object before exiting the activity...
        super.onDestroy();
    }

	@Override
	public void onSurfaceViewCreated(String streamId, SurfaceView surfaceView) {
		Log.d(TAG, "Stream_ID:" + streamId);
		Log.d(TAG, "SurfaceView:" + surfaceView);
	try{
		// We have a valid android surface where to render the stream: so we pass it to the native library to initialize the stream
		if (streamId.equals(this.stream1.getName()))
			{
				Log.d(TAG, "Trying to load tmp surface for stream 1");  
				this.stream1.prepare(surfaceView);
			}
			else if (streamId.equals(this.stream2.getName()))
			{    
				Log.d(TAG, "Trying to load tmp surface for stream 2"); 
				this.stream2.prepare(surfaceView);
			}
		}catch (Exception e) {
			Log.e(TAG,"Exception in onSurfaceCreated");
		}
	}

	@Override
	public void onSurfaceViewDestroyed(String streamId) {
		Log.d(TAG, "called onSurfaceViewDestroyed from fragment for:" + streamId);  
		if (streamId.equals(this.stream1.getName()))
			this.stream1.destroy();
		else 
			if (streamId.equals(this.stream2.getName()))
			this.stream2.destroy();
	}
    
	
	
}