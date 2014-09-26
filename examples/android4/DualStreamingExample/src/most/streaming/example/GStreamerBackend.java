package most.streaming.example;


import com.gstreamer.GStreamer;

import android.content.Context;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GStreamerBackend implements SurfaceHolder.Callback {

	
    private native void nativeInit(String streamName, int latency);     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativeFinalizeGlobals(); // Destroy the global gstreamer references
    private native void nativeSetUri(String uri); // Set the URI of the media to play
    private native int nativeGetLatency(); // Get the latency of the stream to play
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativeSetPosition(int milliseconds); // Seek to the indicated position, in milliseconds
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativeSurfaceInit(Object surface); // A new surface is available
    private native void nativeSurfaceFinalize(); // Surface about to be destroyed
    private long native_custom_data;      // Native code will use this to keep private data
    
    private String uri = null;
    private String streamName = null;
    private int latency = 200;
    
    static {
    	 
     System.loadLibrary("gstreamer_android");
     System.loadLibrary("tutorial-dual_streaming");
      
        nativeClassInit();
    }
    
    private GStreamerListener gstListener = null;

	private SurfaceView surfaceView;
    
	/**
	 * Instance a new Streaming object
	 * @param context the application context
	 * @param gstListener the Listener where to receive all notification from the Library
	 * @param uri the uri of the stream
	 * @param latency the preferred latency of the stream (in ms)
	 * @param surfaceView the Surface where to render the stream
	 * @throws Exception
	 */
    public GStreamerBackend (String streamName, Context context, GStreamerListener gstListener, String uri, int latency, SurfaceView surfaceView) throws Exception {
        
    	this.gstListener = gstListener;
    	GStreamer.init(context);
    	this.uri = uri;
    	this.latency = latency;
    	this.streamName = streamName;
    	this.surfaceView = surfaceView;
    	this.surfaceView.getHolder().addCallback(this);
    	
    	this.init();
    }
	
    
	private void init() {
	   nativeInit(this.streamName, this.latency);
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
	public int getLatency()
	{
		return nativeGetLatency();
	}
	
	public void finalizeLib() {
		nativeFinalize();
	}
	
	public void finalizeGlobals() {
		nativeFinalizeGlobals();
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
    	//Log.d("GSTREAMER_BACKEND", "Message from Gstreamer:" + message);
    	this.gstListener.setMessage(this,"From Backend:" + message);
    }
    
    // Called from native code
    private void onGStreamerInitialized()
    {   
    	nativeSetUri(this.uri);
    	gstListener.onGStreamerInitialized(this);
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
       gstListener.onMediaSizeChanged(this,width, height);
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
}


