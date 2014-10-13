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
import org.crs4.most.streaming.StreamingFactory;
import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;

 
/**
 * This example shows how to get an instance of an IStream object for playing and pausing  video stream on a surface.
 * After the application started,an IStream object is created by calling the library factory method {@link StreamingFactory#getIStream()}. Then the stream is initialized by
 * calling the method {@link IStream#prepare(android.content.Context, SurfaceView, HashMap, Handler)} and the event notifications sent
 * to the application handler (this application in this example). 
 * Once the handler received the event {@link StreamingEvent#STREAM_STATE_CHANGED}, it checks for the current {@link StreamState} and, if the state is equal to
 * {@link StreamState#INITIALIZED} the method {@link IStream#play()} is called for playing the stream.
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
        
        ImageButton play = (ImageButton) this.findViewById(R.id.button_play);
        play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // play the stream
                play();
               
            }
        });

        ImageButton pause = (ImageButton) this.findViewById(R.id.button_pause);
        pause.setOnClickListener(new OnClickListener() {
         

    		public void onClick(View v) {
                // pause the stream
                pause();
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

    } // emd of onCreate

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
    	this.myStream = StreamingFactory.getIStream();
    	txtView.setText("Preparing stream");
    	
    	try {
			this.myStream.prepare(this.getApplicationContext(), (SurfaceView) findViewById(R.id.surfaceView1), getConfigParams(), this.handler);
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


    @Override
	public boolean handleMessage(Message streamingMessage) {
		StreamingEventBundle myEvent = (StreamingEventBundle) streamingMessage.obj;
		String infoMsg ="Event Type:" +  myEvent.getEventType() + " ->" +  myEvent.getEvent() + ":" + myEvent.getInfo();
		Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
		
		StreamState streamState = (StreamState) myEvent.getData();
		
		// notify the user about the event and the new Stream state.
		this.txtView.setText(infoMsg + " Stream State:" + streamState.toString());
		
		if (myEvent.getEventType()==StreamingEventType.STREAM_EVENT.STREAM_EVENT)
			{
			     if (streamState==StreamState.INITIALIZED)   {
			    	 this.changeButtonsState(true);
			         if (is_playing_desired) this.play();	 
			     }
			}
		     
		return false;
	}
    
    private void play()
    {
    	if (myStream!=null)
    	{
        is_playing_desired = true;
    	txtView.setText("Playing stream " + this.myStream.getName());
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
    	myStream.pause();
    	}
    	else 
    		txtView.setText("Unable to pause null stream! ");
    }

}
