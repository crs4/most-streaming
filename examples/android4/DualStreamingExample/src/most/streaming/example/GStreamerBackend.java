package most.streaming.example;


import com.gstreamer.GStreamer;

import android.content.Context;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

public class GStreamerBackend {

	
    private native void nativeInit(String streamName);     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativeFinalizeGlobals(); // Destroy the global gstreamer references
    private native void nativeSetUri(String uri); // Set the URI of the media to play
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativeSetPosition(int milliseconds); // Seek to the indicated position, in milliseconds
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativeSurfaceInit(Object surface); // A new surface is available
    private native void nativeSurfaceFinalize(); // Surface about to be destroyed
    private long native_custom_data;      // Native code will use this to keep private data
    
    private String uri = null;
    
    static {
    	 
     System.loadLibrary("gstreamer_android");
     System.loadLibrary("tutorial-dual_streaming");
      
        nativeClassInit();
    }
    
    private GStreamerListener gstListener = null;

	private SurfaceView surfaceView;
    
    public GStreamerBackend (Context context, GStreamerListener gstListener, String uri, SurfaceView surfaceView) throws Exception {
        
    	this.gstListener = gstListener;
    	GStreamer.init(context);
    	this.uri = uri;
    	this.surfaceView = surfaceView;
    }
	
    public SurfaceView getSurfaceView() {
    	return this.surfaceView;
    }
    
	public void play() {
		nativePlay();
	}
	
	public void pause() {
		nativePause();
	}
	
	public void setUri(String uri)
	{
		this.uri = uri;
		nativeSetUri(this.uri);
	}
	
	public void finalizeLib() {
		nativeFinalize();
	}
	
	public void finalizeGlobals() {
		nativeFinalizeGlobals();
	}
	
	public void init(String streamName) {
	   nativeInit(streamName);
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
}


