/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package org.crs4.most.streaming;


import java.io.FileNotFoundException;
import java.util.HashMap;

import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.enums.StreamingEvent;
import org.crs4.most.streaming.enums.StreamingEventType;

import com.gstreamer.GStreamer;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


//  the scope of this class is reserved to this current package. Don't instance this class, but use the StreamingFactory.getStream() method instead.
class GStreamerBackend implements SurfaceHolder.Callback, IStream {

	// native methods
    private native boolean nativeInit(String streamName, int latency);     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativeSetUri(String uri); // Set the URI of the media to play
    private native int nativeGetLatency(); // Get the latency of the stream to play
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativeSetPosition(int milliseconds); // Seek to the indicated position, in milliseconds
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativeSurfaceInit(Object surface); // A new surface is available
    private native void nativeSurfaceFinalize(); // Surface about to be destroyed
    private long native_custom_data;      // Native code will use this to keep private data
    
    private static final String TAG = "GSTREAMER_BACKEND";
    // local fields
    private Context context = null;
    private Handler notificationHandler = null;
	private SurfaceView surfaceView;
    
    private String uri = null;
    private String streamName = null;
    private StreamState streamState = StreamState.DEINITIALIZED;
    private int latency = 200;
    private static boolean lib_initialized = false;
    private boolean stream_initialized = false;

    static {
    	
     Log.d(TAG,"Loading native libraries...corretta?");
     
     Log.d(TAG,"Loading gstreamer_android...");
     System.loadLibrary("gstreamer_android");
     
     Log.d(TAG,"Loading most_streaming...");
     System.loadLibrary("most_streaming");
     Log.d(TAG,"Libraries loaded.");
    
     Log.d(TAG,"Loading native class and methods references...");
     lib_initialized = nativeClassInit();
    }
    
    public GStreamerBackend(Context context, HashMap<String, String> configParams ,Handler notificationHandler) throws Exception
    {
    	Log.d((TAG), "GStreamerBackend instance...");
    	if (!lib_initialized) throw new Exception("Error initilializing the native library.");
    	if (stream_initialized) throw new Exception("Error preparing the strem because it results already initialized");
    	
    	if (context==null) throw new IllegalArgumentException("Context parameter cannot be null");
    	if (notificationHandler==null) throw new IllegalArgumentException("Handler parameter cannot be null");
    	
    	if  (!configParams.containsKey("name")) throw new IllegalArgumentException("param name not found in configParams");
		if (!configParams.containsKey("uri"))  throw new IllegalArgumentException("param uri not found in configParams ");
		
		this.notificationHandler = notificationHandler;
		this.streamName =   configParams.get("name");
    	this.uri = configParams.get("uri");
    	this.latency = configParams.containsKey("latency") ?  Integer.valueOf(configParams.get("latency")) : 200;
    	
    	this.notificationHandler = notificationHandler;
    	
		this.context = context;
    	this.streamName =   configParams.get("name");
    	this.uri = configParams.get("uri");
    	this.latency = configParams.containsKey("latency") ?  Integer.valueOf(configParams.get("latency")) : 200;
    	
    	this.initLib();
    }
    
    private void notifyState(StreamingEventBundle myStateBundle)
    {
    	Message m = Message.obtain(this.notificationHandler,myStateBundle.getEventType().ordinal(), myStateBundle);
		m.sendToTarget();
    }
    
    @Override
	public void prepare(SurfaceView surface)  
		 {
	    	Log.d((TAG), "preparing IStream instance...");
	    
	    	if (surface==null) 
	    	{
	    		this.streamState = StreamState.DEINITIALIZED;
	    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_ERROR, "No valid surface provided for the stream: " + this.streamName, this));
	    	}
	    		else
	    	{
	    		this.surfaceView = surface;
	        	this.surfaceView.getHolder().addCallback(this);
	        	this.initStream();
	    	}
    	
	}
    
    private void initStream() {
    	this.streamState = StreamState.INITIALIZING;
    	this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Inizializating Stream " + this.streamName, this));
    	
    	boolean native_init_result = nativeInit(this.streamName, this.latency);
    	if (!native_init_result)
    	{
    		this.streamState = StreamState.DEINITIALIZED;
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_ERROR, "Stremm initialization failed:" + this.streamName + " initialized", this));
    	}
    }
    
	private void initLib() throws Exception {
	
	try {
		GStreamer.init(this.context);
	} catch (FileNotFoundException e) {
		
		Log.w(TAG, "GStreamer.java maybe is trying to copy fonts to assets folder... operation not allowed in to alibrary: " + e.getMessage());
	}
	
	
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e(TAG, "GStreamer initialization failed: " + e.getMessage());
	    throw new Exception("Error initializing the GStreamer Backend Lib:" + e.getMessage());
		}
	}
	
	
    /**
     * 
     * @return the rendering Surface
     */
    public SurfaceView getSurfaceView() {
    	return this.surfaceView;
    }
    
    /**
     * Play the stream
     */
	public void play() {
		Log.d(TAG,"Trying to play stream...");
		nativePlay();
	}
	
	/**
	 * pause the stream
	 */
	public void pause() {
		Log.d(TAG,"Trying to pause stream...");
		nativePause();
	}
	
	/**
	 * Update the uri of the stream
	 * @param uri the new uri
	 */
	public void setUri(String uri)
	{   
		this.uri = uri;
		Log.d("GSTREAMER_BACKEND", "Setting uri to:" + this.uri);
		nativeSetUri(this.uri);
	}
	
	
    /**
     * Get the current value of latency property of this stream (Reads the value from native code to be sure to return the effective latency value)
     * @return the latency value in ms
     */
	@Override
	public int getLatency()
	{
		return nativeGetLatency();
	}
	
	
	@Override
	public void destroy() {
		this.streamState = StreamState.DEINITIALIZING;
		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Deinizializating Stremm " + this.streamName, this));
    	
		nativeFinalize();
	}


	public void surfaceInit(Surface surface) {
		nativeSurfaceInit(surface);
	}
	
	public void surfaceFinalize() {
		nativeSurfaceFinalize();
		
	}
	 // Called from native code
    private void setMessage(final String message)
    {
    	Log.d("GSTREAMER_BACKEND", "Message from Gstreamer:" + message);
    	//this.gstListener.onMessageReceived(this,"From Backend:" + message);
    }
    
    // Called from native code
    private void onGStreamerInitialized()
    {   
    	Log.d("GSTREAMER_BACKEND", "Called onGStreamerInitialized()");
    	
    	
    	this.streamState = StreamState.INITIALIZED;
    	Log.d("GSTREAMER_BACKEND", "Stream initialized");
    	if (!this.stream_initialized)
    	{   
    		
    		this.setUri(this.uri);
    		this.stream_initialized = true;
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream " + this.streamName + " initialized: (uri:" +this.uri + ")" , this));
    	}
    	else{
    		Log.w(TAG, "Stream already initialized.Unexpected callback from gstreamer ?!");
    	}
    		
    }
    
    // Called from native code wheen the streamState of the native stream changes
    private void onStreamStateChanged(int oldState, int newState){
    	Log.d(TAG, "onStreamStateChanged: state from state:" + oldState + " to:" + newState);
    	//this.streamState = GStreamerBackend.getStreamStateByGstState(newState);
    	
    	// from pause to play or from play to pause
    	if ((oldState==3 && newState==4) || (oldState==4 && newState==3)) {
    		this.streamState = GStreamerBackend.getStreamStateByGstState(newState);
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream state changed to:" + this.streamState, this));
    	}
    	// stream deinitialized
    	else if ((oldState>=2 && newState<=1))
    	{
    		this.stream_initialized = false;
    		this.streamState = GStreamerBackend.getStreamStateByGstState(newState);
    		
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream state changed to:" + this.streamState, this.streamState));
    	}
    	
    }
    
    // GST_STATE_VOID_PENDING        = 0,
   	// GST_STATE_NULL                = 1, // no resources allocated
    // GST_STATE_READY               = 2, // all no-streaming specific resources allocated
    // GST_STATE_PAUSED              = 3, // all resources allocated: stream ready to play
    // GST_STATE_PLAYING             = 4
    private static StreamState getStreamStateByGstState(int gstState)
    {
    	if (gstState<2)
    		return StreamState.DEINITIALIZED;
    	else if (gstState==2)
    		return StreamState.INITIALIZED;
    	else if (gstState==3)
    		return StreamState.PAUSED;
    	else if (gstState==4)
    		return StreamState.PLAYING;
    	else
    		throw new IllegalArgumentException("Unknown pipeline streamState received from gstreamer native code:" + gstState);
    }
    
     // Called from native code
    private void setCurrentPosition(final int position, final int duration) 
    {
    	//Log.i ("GStreamer", "setCurrentPosition: " + position + " on duration;" + duration);
    }
    
    // Called from native code when the size of the media changes or is first detected.
    // Inform the video surface about the new size and recalculate the layout.
    private void onMediaSizeChanged (int width, int height) {
        Log.i ("GStreamer", "Media size changed to " + width + "x" + height);
       //gstListener.onMediaSizeChanged(this,width, height);
    }
    
   
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        Log.d("GStreamer", "Surface changed to format " + format + " width "
                + width + " height " + height);
        this.surfaceInit(holder.getSurface());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface created: " + holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface destroyed");
        this.surfaceFinalize();
    }
    

	@Override
	public String getName() {
		
		return this.streamName;
	}
	@Override
	public StreamState getState() {
		return this.streamState;
	}
	
	 
}


