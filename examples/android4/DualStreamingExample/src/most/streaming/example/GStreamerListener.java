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

	public void onGStreamerInitialized(GStreamerBackend gStreamerBackend);
	public void setMessage(GStreamerBackend gStreamerBackend,final String message);
	public void onMediaSizeChanged (GStreamerBackend gStreamerBackend, int width, int height);
}
