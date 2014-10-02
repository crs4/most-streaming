/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package org.crs4.most.streaming.examples;




import android.app.Activity;

import android.os.Bundle;
import android.util.Log;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingFactory;

 

public class MostStreamingExample1 extends Activity {
    
	private static final String TAG = "MostStreamingExample1";
	
	private IStream myStream = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        runExample();

    } // emd of onCreate

    
    private void runExample()
    {
    	// Get a new instance of the stream by using the factory class
    	this.myStream = StreamingFactory.getIStream();
    	
    	//this.myStream.prepare(this, surface, configParams, this);
    }
    
    protected void onSaveInstanceState (Bundle outState) {
       
    }

    protected void onDestroy() {
    	Log.d(TAG, "CALLED ON DESTROY!");
    	
    	
    	// finalize global references of the native gstreamer library
        //gstInstance.finalizeGlobals();
        super.onDestroy();
    }

}
