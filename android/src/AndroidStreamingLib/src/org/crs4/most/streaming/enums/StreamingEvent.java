/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming.enums;

/**
 * Contains all events triggered by the Streaming library
 * 
 */
public enum StreamingEvent {
  
  STREAM_STATE_CHANGED,     /** a stream internal state change occurred */
  VIDEO_SIZE_CHANGED,       /** the size of the video stream changed */ 
  STREAM_ERROR   			/** a streaming error occurred */
}
   
