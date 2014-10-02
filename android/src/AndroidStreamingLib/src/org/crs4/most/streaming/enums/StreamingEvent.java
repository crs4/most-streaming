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
 * Contains all events triggered by the library
 * 
 */
public enum StreamingEvent {
	
  LIB_INITIALIZING,   
  LIB_INITIALIZED,
  LIB_INITIALIZATION_FAILED,
  LIB_DEINITIALIZING,
  LIB_DEINITIALIZED,
  LIB_DEINITIALIZATION_FAILED,
  
  STREAM_INITIALIZING,   
  STREAM_INITIALIZED,
  STREAM_INITIALIZATION_FAILED,
  STREAM_DEINITIALIZING,
  STREAM_DEINITIALIZED,
  STREAM_DEINITIALIZATION_FAILED,
  STREAM_PLAYING,
  STREAM_PAUSED,
  
}
   
