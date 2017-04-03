/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package it.crs4.most.streaming;

public interface IEventListener {
    public void frameReady(byte[] frame);
    public void onPlay();
    public void onPause();
    public void onVideoChanged(int width, int height);


}
