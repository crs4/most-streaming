/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming;



import org.crs4.most.streaming.enums.StreamProperty;
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
	 * Destroy this stream
	 */
	public void destroy();

    /**
     * Reads the current value of the specified stream property
     * @param property
     * @return the value of the property
     */
	public Object getProperty(StreamProperty property);
	
	/**
	 * Commit the stream properties values specified as argument
	 * @param properties the stream properties to update
	 * @return true if no error occurred during the update request; False otherwise
	 */
	public boolean commitProperties(StreamProperties properties);

	/**
	 * Load a still image from the remote camera, provided the uri
	 * @param uri the uri pointing to the image to load
	 * @return <code>true</code> if no error occurred during the operation, <code>false</code> otherwise
	 */
	public boolean loadStillImage(String uri);
}

