/*
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming.enums;

public enum StreamingEventType {
	/** 
	 * generic library events 
	 */
     LIB_EVENT, 
     
     /**  
      * stream events as state changes (play, pause...) or other stream related events 
      */
	 STREAM_EVENT, 
}
