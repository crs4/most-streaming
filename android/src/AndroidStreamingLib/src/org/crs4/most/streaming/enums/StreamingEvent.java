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
  
  STREAM_INITIALIZING,  
  STREAM_STATE_CHANGED,   
  STREAM_DEINITIALIZING, 
  STREAM_ERROR
}
   
