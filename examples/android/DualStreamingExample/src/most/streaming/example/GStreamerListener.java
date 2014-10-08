/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package most.streaming.example;

public interface GStreamerListener {
    
	/**
	 * Called by the gstreamer native code when the native pipeline has been initialized
	 * @param gStreamerBackend the particular instance
	 */
	public void onGStreamerInitialized(GStreamerBackend gStreamerBackend);
	
	/**
	 * Called by the gstreamer native code for sending textual information  
	 * @param gStreamerBackend
	 * @param message
	 */
	public void setMessage(GStreamerBackend gStreamerBackend,final String message);
	
	/**
	 * Called from the gstreamer native code when the size of the video stream has changed
	 * @param gStreamerBackend the gstreamer instance that called this method
	 * @param width the new width of the stream
	 * @param height he new height of the stream
	 */
	public void onMediaSizeChanged (GStreamerBackend gStreamerBackend, int width, int height);
}
