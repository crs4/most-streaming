/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming;



import org.crs4.most.streaming.enums.StreamState;
import org.crs4.most.streaming.utils.Size;

import android.view.SurfaceView;

/**
 * An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling
 * the method {@link StreamingLib#createStream(java.util.HashMap, android.os.Handler)}.
 *
 */
public interface IStream {
	
	 
    /**
     * 
     * @return the name of this stream
     */
    public String getName();
    
    
    /**
     * 
     * @return the current state of this stream
     */
    public StreamState getState();
    
    /**
     * 
     * @return the current size of the video stream
     */
    public Size getVideoSize();
    
    
    /**
	 * Prepare the stream by providing a video surface
	 * @param surfaceView the Surface where to render the stream
	 *
	 */
	public void prepare(SurfaceView surface);
	
    /**
     * Play the stream
     */
	public void play() ;
	
	/**
	 * pause the stream
	 */
	public void pause();
	
	/**
	 * Update the uri of the stream
	 * @param uri the new uri
	 * @return {@code True} if the uri was successfully updated; {@code False} otherwise.
	 */
	public boolean setUri(String uri);
	
	/**
     * Get the current value of uri property of this stream (it should read the value from native code (if any) to be sure to return the effective uri value)
     * @return the latency value in ms
     */
	public String getUri();
	
	/**
     * Get the current value of latency property of this stream (it should read the value from native code (if any) to be sure to return the effective latency value)
     * @return the latency value in ms
     */
	public int getLatency();
	
	/**
	 * Set the preferred latency for this stream
	 * @param latency the preferred latency (in ms)
	 * @return {@code True} if the new value was accepted;{@code False} otherwise
	 */
	public boolean setLatency(int latency);
	/**
	 * Destroy this stream
	 */
	public void destroy();


	boolean setUriAndLatency(String uri, int latency);

}

