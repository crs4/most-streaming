/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package org.crs4.most.streaming;


import java.util.HashMap;

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
    private int latency = 200;
    private static boolean lib_initialized = false;
    
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
    
    public GStreamerBackend()
    {
    	Log.d((TAG), "GStreamerBackend instance...");
    }
    
    private void notifyState(StreamingEventBundle myStateBundle)
    {
    	Message m = Message.obtain(this.notificationHandler,myStateBundle.getEventType().ordinal(), myStateBundle);
		m.sendToTarget();
    }
    
    @Override
	public void prepare(Context context, SurfaceView surface,
			HashMap<String, String> configParams, Handler notificationHandler)  
			throws Exception {
	    
    	if (!lib_initialized) throw new Exception("Error initilializing the native library.");
    	
    	if (context==null) throw new IllegalArgumentException("Context parameter cannot be null");
    	if (notificationHandler==null) throw new IllegalArgumentException("Handler parameter cannot be null");
    	if (surface==null) throw new IllegalArgumentException("Surface parameter cannot be null");
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
    	
    	
    	this.surfaceView = surface;
    	this.surfaceView.getHolder().addCallback(this);
    
    	this.initLib();
    	this.initStream();
	}
    
    private void initStream() {
    	this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_INITIALIZING, "Inizializating Stremm " + this.streamName, this.streamName));
    	
    	boolean stream_initialized = nativeInit(this.streamName, this.latency);
    	if (stream_initialized)
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_INITIALIZED, "Stremm " + this.streamName + " initialized", this.streamName));
    	else
    		this.notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_INITIALIZATION_FAILED, "Stremm initialization failed:" + this.streamName + " initialized", this.streamName));
    	
    }
    
	private void initLib() {
	this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZING, "Inizializating Streaming Lib for stremm " + this.streamName, this.streamName));
	try {
		GStreamer.init(this.context);
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZED, "GStreamer Lib successfully initialized: for stream " + this.streamName, this.streamName));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e(TAG, "GStreamer initialization failed: " + e.getMessage());
		this.notifyState(new StreamingEventBundle(StreamingEventType.LIB_EVENT, StreamingEvent.LIB_INITIALIZATION_FAILED, "GStreamer initialization failed: " + e.getMessage(), e));
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
		nativePlay();
	}
	
	/**
	 * pause the stream
	 */
	public void pause() {
		nativePause();
	}
	
	/**
	 * Update the uri of the stream
	 * @param uri the new uri
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
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
    	nativeSetUri(this.uri);
    	Log.d("GSTREAMER_BACKEND", "Stream initialized");
    	//gstListener.onStreamInitialized(this);
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
	
	 
}


