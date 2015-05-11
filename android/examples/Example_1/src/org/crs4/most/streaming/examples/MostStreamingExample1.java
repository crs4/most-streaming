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

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingEventBundle;
import org.crs4.most.streaming.StreamingLib;
import org.crs4.most.streaming.StreamingLibBackend;
import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;

 
/**
 * This example shows how to get an instance of an IStream object for playing and pausing a video stream on an android surface.
 * After the application started, an IStream object is created by calling the library factory method {@link StreamingLib#createStream(HashMap, Handler)}. Then the stream is initialized by
 * calling the method {@link IStream#prepare(android.content.Context, SurfaceView, HashMap, Handler)} and the event notifications sent
 * to the application handler (this application itself in this example). 
 * Once the handler received the event {@link StreamingEvent#STREAM_STATE_CHANGED}, it checks for the current {@link StreamState} and, if the state is equal to
 * {@link StreamState#INITIALIZED} the play and pause buttons are enabled so the user can click on them for playing  {@link IStream#play()}  or pausing {@link IStream#pause()} the stream.
 * </br>
 * Note that, you can change the screen orientation while playing the stream. In this case, the activity is destroyed, recreated and the stream resumes playing automatically.
 */
public class MostStreamingExample1 extends Activity implements Handler.Callback {
    
	private static final String TAG = "MostStreamingExample1";

	// The stream object 
	private IStream myStream = null;
	
	// The textView used for notification messages
    private TextView txtView = null;
    
    // The handler used for event notification
    private Handler handler = null;
    
    // flag for notifying if the user wants the streamer to play
    private boolean is_playing_desired = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.txtView = (TextView) findViewById(R.id.textview_message);
        this.handler = new Handler(this);
        
        ImageButton butPlay = (ImageButton) this.findViewById(R.id.button_play);
        butPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // play the stream
                play();
            }
        });

        ImageButton butPause = (ImageButton) this.findViewById(R.id.button_pause);
        butPause.setOnClickListener(new OnClickListener() {
         

    		public void onClick(View v) {
                // pause the stream
                pause();
            }
        });
        
        
        ImageButton butExit = (ImageButton) this.findViewById(R.id.button_exit);
        butExit.setOnClickListener(new OnClickListener() {
         

    		public void onClick(View v) {
                // destroy the activity
                finish();
            }
        });

        if (savedInstanceState != null) {
            is_playing_desired = savedInstanceState.getBoolean("playing");
            Log.i (TAG, "Activity created. Saved state is playing:" + is_playing_desired);
        } else {
            is_playing_desired = false;
            Log.i (TAG, "Activity created. There is no saved state, playing: false");
        }

        // Start with disabled buttons, until the stream is initialized
        this.changeButtonsState(false);
        
        runExample();

    } // end of onCreate

    private void changeButtonsState(boolean enabled)
    {
    	Log.d(TAG,"Setting button states to:" + String.valueOf(enabled));
    	
    	this.findViewById(R.id.button_play).setEnabled(enabled);
        this.findViewById(R.id.button_pause).setEnabled(enabled);
    }
    
    private HashMap<String, String> getConfigParams(){
    	
    	HashMap<String, String>  configParams = new HashMap<String, String>();
    	configParams.put("name", "Stream 1");
		configParams.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
    	return configParams;
    }
    
    
    private void runExample()
    {
    	// Get a new instance of the stream by using the factory class
    	txtView.setText("Getting IStreamInstance,,,");
    	
        
    	
    	try {
    		
    		StreamingLib streamingLib = new StreamingLibBackend();
    		streamingLib.initLib(this.getApplicationContext());
    		
    		this.myStream = streamingLib.createStream(getConfigParams(), this.handler);
        	txtView.setText("Preparing stream");
        	
    		// initialize the stream
			this.myStream.prepare((SurfaceView) findViewById(R.id.surfaceView1));
			
		} catch (Exception e) {
			this.txtView.setText("Error preparing stream:" + e.getMessage());
			e.printStackTrace();
		}
    }
    
  
	protected void onSaveInstanceState (Bundle outState) {
	    Log.d (TAG, "Saving state, playing:" + is_playing_desired);
	    outState.putBoolean("playing", is_playing_desired);
	}

    protected void onDestroy() {
    	Log.d(TAG, "CALLED ON DESTROY!");
    	// Remember to destroy the current stream object before exiting the activity...
    	if (this.myStream!=null)
    		myStream.destroy();
        super.onDestroy();
    }

    
    // handle all events triggered from the streaming library
    @Override
	public boolean handleMessage(Message streamingMessage) {
    	
    	// The bundle containing all available informations and resources about the incoming event
		StreamingEventBundle myEvent = (StreamingEventBundle) streamingMessage.obj;
		
		String infoMsg ="Event Type:" +  myEvent.getEventType() + " ->" +  myEvent.getEvent() + ":" + myEvent.getInfo();
		Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
		
		
		
		// for simplicity, in this example we only handle events of type STREAM_EVENT
		if (myEvent.getEventType()==StreamingEventType.STREAM_EVENT)
			{
			    // All events of type STREAM_EVENT provide a reference to the stream that triggered it.
			    // In this case we are handling a single stream, so we are sure that the event is referred to our stream.
			    // Note that we are only interested to the new state of the stream
				StreamState streamState =  ((IStream) myEvent.getData()).getState();
				
				// notify the user about the event and the new Stream state.
				this.txtView.setText(infoMsg + " Stream State:" + streamState.toString());
			
			     if (streamState==StreamState.INITIALIZED)   {
			    	 this.changeButtonsState(true);
			         if (is_playing_desired) this.play();	 
			     }
			     else if (streamState==StreamState.DEINITIALIZED)
			     {
			    	 this.changeButtonsState(false);
			     }
			   
			}
		else if (myEvent.getEvent()==StreamingEvent.STREAM_ERROR)
		{
			// notify the user about the event error and the new Stream state.
			this.txtView.setText("Stream Error:" + infoMsg + " Stream State:" + ((IStream) myEvent.getData()).getState());
		}
		 
		     
		return false;
	}
    
    private void play()
    {
    	if (myStream!=null)
    	{
        is_playing_desired = true;
    	txtView.setText("Playing stream " + this.myStream.getName());
    	
    	// play the stream
    	myStream.play();
    	}
    	else 
    		txtView.setText("Unable to play a null stream! ");
    }
    
    private void pause()
    {
    	if (myStream!=null)
    	{
    		is_playing_desired = false;
    		
    		// pause the stream
    		myStream.pause();
    	}
    	else 
    		txtView.setText("Unable to pause null stream!");
    }

}
