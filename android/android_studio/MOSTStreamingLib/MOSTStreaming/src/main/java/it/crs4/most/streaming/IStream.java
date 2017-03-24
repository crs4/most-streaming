/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package it.crs4.most.streaming;


import android.view.SurfaceHolder;
import android.view.SurfaceView;

import it.crs4.most.streaming.enums.StreamProperty;
import it.crs4.most.streaming.enums.StreamState;
import it.crs4.most.streaming.utils.Size;

/**
 * An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling
 * the method {@link StreamingLib#createStream(java.util.HashMap, android.os.Handler)}.
 */
public interface IStream {


    /**
     * @return the name of this stream
     */
    String getName();


    /**
     * @return the current state of this stream
     */
    StreamState getState();

    /**
     * @return the current size of the video stream
     */
    Size getVideoSize();


    /**
     * Prepare the stream by providing a video surface
     *
     * @param surface the Surface where to render the stream
     */
    void prepare(SurfaceView surface);

    void prepare(SurfaceView surface, boolean frameCallback);

    /**
     * Play the stream
     */
    void play();

    /**
     * pause the stream
     */
    void pause();


    /**
     * Destroy this stream
     */
    void destroy();

    /**
     * Reads the current value of the specified stream property
     *
     * @param property
     * @return the value of the property
     */
    Object getProperty(StreamProperty property);

    void setSurface(SurfaceHolder surface);

    /**
     * Commit the stream properties values specified as argument
     *
     * @param properties the stream properties to update
     * @return true if no error occurred during the update request; False otherwise
     */
    boolean commitProperties(StreamProperties properties);

    /**
     * Load a still image from the remote camera, provided the uri
     *
     * @param uri the uri pointing to the image to load
     * @return <code>true</code> if no error occurred during the operation, <code>false</code> otherwise
     */
    boolean loadStillImage(String uri);

    /**
     * Get detailed informations about a stream error (return an empty stream if the stream is not in Stream.ERROR state)
     *
     * @return infomrations about the type of stream error
     */
    String getErrorMsg();

    void addEventListener(IEventListener listener);

    void removeEventListener(IEventListener listener);


}

