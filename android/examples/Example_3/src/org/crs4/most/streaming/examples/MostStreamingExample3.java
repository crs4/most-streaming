/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingEventBundle;
import org.crs4.most.streaming.StreamingLib;
import org.crs4.most.streaming.StreamingLibBackend;
import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;
import org.crs4.most.streaming.examples.StreamDialogFragment.StreamDialogListener;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/**
 * This example shows how to simultaneously play two streams into an activity. You can also rotate the device, so the activity
 * along with the layout and the stream are automatically recreated.  
 *  
 *
 */
public class MostStreamingExample3 extends Activity implements Handler.Callback, IStreamFragmentCommandListener, StreamDialogListener  {
	
	private static final String TAG = "Example2_MainActivity";
	
	private ArrayList<IStream> streamsArray = null;
	private ArrayAdapter<IStream> streamsArrayAdapter = null;


	private Handler handler;

	
	private IStream stream1 = null;
	private IStream stream2 = null;
	StreamViewerFragment stream1Fragment = null;
	StreamViewerFragment stream2Fragment = null;
	
	private boolean exitFromAppRequest = false;
	//ID for the menu exit option
    private final int ID_MENU_EXIT = 1;
    
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
		 	//get the MenuItem reference
		 MenuItem item = 
		    	menu.add(Menu.NONE,ID_MENU_EXIT,Menu.NONE,R.string.mnu_exit);
		 return true;
	    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	    	//check selected menu item
	    	if(item.getItemId() == ID_MENU_EXIT)
	    	{
	    		exitFromApp();
	    		return true;
	    	}
	    	return false;
	    }
	 
	 
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if (savedInstanceState == null)
        {
         try {
        	this.handler = new Handler(this);
        	
        	
        	// Instance and initialize the Streaming Library
        	
        	StreamingLib streamingLib = new StreamingLibBackend();
        	
        	// First of all, initialize the library 
        	streamingLib.initLib(this.getApplicationContext());
        	
        	
        	// Instance the first stream
        	HashMap<String,String> stream1_params = new HashMap<String,String>();
        	stream1_params.put("name", "Stream_1");
        	//stream1_params.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
        	stream1_params.put("uri", "http://clips.vorwaerts-gmbh.de/VfE.webm");
        	 
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
    		
    	
        	
            this.setupStreamsListView();
            
			} catch (Exception e) {
				e.printStackTrace();
				 Log.e(TAG, "Error initializing the streams:" + e.getMessage());
			}
        }
    }

	
	 private void setupStreamsListView()
	    {
	    		this.streamsArray = new ArrayList<IStream>();
	    		final ListView streamsView = (ListView) findViewById(R.id.listStreams);
	            this.streamsArray= new ArrayList<IStream>();
	            
	            this.streamsArrayAdapter = new IStreamArrayAdapter(this, R.layout.istream_row, this.streamsArray);
	            
	            LayoutInflater inflater = getLayoutInflater();
	            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.istream_header, streamsView, false);
	            streamsView.addHeaderView(header, null, false);
	            
	            streamsView.setAdapter(this.streamsArrayAdapter);
	            
	            
 
	            streamsView.setOnItemClickListener(new OnItemClickListener() {

	            	
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.d(TAG, "SELECTED ITEM:" + String.valueOf(position));
						
						    // Create and show the dialog.
						    final IStream selectedStream = streamsArray.get(position-1);
						   
						// custom dialog
						final Dialog dialog = new Dialog(MostStreamingExample3.this);
						dialog.setContentView(R.layout.istream_popup_editor);
						
						
						
						dialog.setTitle(selectedStream.getName() + " [" + selectedStream.getState()+"]");
						
						
						final EditText txtUri = (EditText) dialog.findViewById(R.id.editUri);
						final String currentUri =  selectedStream.getUri();
						txtUri.setText("http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
						//txtUri.setText(currentUri);
						final EditText txtLatency = (EditText) dialog.findViewById(R.id.editLatency);
						final String currentLatency = String.valueOf(selectedStream.getLatency());
						txtLatency.setText(currentLatency);
						
						Button butOk = (Button) dialog.findViewById(R.id.button_ok);
						// if button is clicked, close the custom dialog
						butOk.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								
								if (!txtLatency.getText().toString().equals(currentLatency) || !txtUri.getText().toString().equals(currentUri))
								{
									int newLatency = Integer.parseInt(txtLatency.getText().toString());
									final String newUri = txtUri.getText().toString(); 
									selectedStream.setUriAndLatency(newUri, newLatency);
								}
								
								dialog.dismiss();
							}
						});
						
						Button butCancel = (Button) dialog.findViewById(R.id.button_cancel);
						// if button is clicked, close the custom dialog
						butCancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Log.d(TAG, "Dialog operation cancelled");
								dialog.dismiss();
							}
						});
						
						
						dialog.show();
					 
						}// end of onItemClick
	            	} 
	            	 
	            	);
	           
	    }
	 
	 	@Override
		public void onStreamDialogPositiveClick(StreamDialogFragment dialog) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStreamDialogNegativeClick(StreamDialogFragment dialog) {
			// TODO Auto-generated method stub
			
		}
	    
	    
	private void updateStreamStateInfo(IStream stream)
	    { Log.d(TAG, "Called updateStreamStateInfo on stream");
	    	if (stream==null)
	    	{
	    		Log.e(TAG, "Called updateBuddyStateInfo on NULL stream");
	    		return;
	    	}
	    	
	    	Log.d(TAG, "Called updateStreamStateInfo on stram:" + stream.getName());
	    	
	    	int streamPosition = this.streamsArrayAdapter.getPosition(stream);
	    	if (streamPosition<0)
	    	{
	    		Log.d(TAG, "Adding stream to listView!");
	    		this.streamsArray.add(stream);
	    		
	    	}
	    	else 
	    	{
	    		Log.d(TAG, "Replacing stream into the listView!");
	    		this.streamsArray.set(streamPosition, stream);
	    	}
	    	this.streamsArrayAdapter.notifyDataSetChanged();
	    }
	
	@Override
	// handle all events triggered from the streaming library
	public boolean handleMessage(Message streamingMessage) {
    	
    	// The bundle containing all available informations and resources about the incoming event
		StreamingEventBundle myEvent = (StreamingEventBundle) streamingMessage.obj;
		
		String infoMsg ="Event Type:" +  myEvent.getEventType() + " ->" +  myEvent.getEvent() + ":" + myEvent.getInfo();
		Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
		
		
		
		// for simplicity, in this example we only handle events of type STREAM_EVENT
		if (myEvent.getEventType()==StreamingEventType.STREAM_EVENT)
			if (myEvent.getEvent()== StreamingEvent.STREAM_STATE_CHANGED)
			{
			    // All events of type STREAM_EVENT provide a reference to the stream that triggered it.
			    // In this case we are handling two streams, so we need to check what stream triggered the event.
			    // Note that we are only interested to the new state of the stream
				IStream stream  =  (IStream) myEvent.getData();
			   
				// update the stream array
				updateStreamStateInfo(stream);
				

				 switch(stream.getState())
				 {
				 	case INITIALIZING: Log.d(TAG, "Stream:" + stream.getName() + " HAS BEING INITIALIZED"); break;
				 	case INITIALIZED:   Log.d(TAG, "Stream:" + stream.getName() + " INITIALIZED"); break;
				 	case PLAYING:  Log.d(TAG, "Stream:" + stream.getName() + " IS PLAYING"); break;
				 	case PAUSED:   Log.d(TAG, "Stream:" + stream.getName() + " IS PAUSED"); break;
				 	case DEINITIALIZING:  Log.d(TAG, "Stream:" + stream.getName() + " HAS BEING DEINITIALIZED"); break;
				 	case DEINITIALIZED:  Log.d(TAG, "Stream:" + stream.getName() + " IS DEINITIALIZED"); 
				 	Log.d(TAG, "Stream deinitialized. All destroyed ?" + areStreamsDeinitialized() + " Exit request:" + exitFromAppRequest);
				    if (areStreamsDeinitialized() && exitFromAppRequest==true) finish();
				 	break;
				 }
				
			}
			 else if (myEvent.getEvent()== StreamingEvent.STREAM_ERROR)
			 {
				 Toast.makeText(getApplicationContext(), myEvent.getInfo(), Toast.LENGTH_LONG).show();
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
	public void onPause() {
		super.onPause();
		Log.d(TAG, "The activity is on Pause state");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "The activity is on Stop state");
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