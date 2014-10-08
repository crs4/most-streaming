/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */

package most.streaming.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

 
/**
 * 
 *  This example shows how to play two simultaneous streams on two Android surfaces by using the underlying native gstreamer backend.
 *  It implements the {@link GStreamerListener} interface for receiving remote notifications from the underlying gstreamer backend. 
 *
 */
public class DualStreamingActivity extends Activity implements GStreamerListener {
    
	private static final String TAG = "DualStreamingActivity";
	private GStreamerBackend gstInstance = null;
	private GStreamerBackend gstInstance2 = null;
	
	private boolean is_playing_desired;   // Whether the user asked to go to PLAYING
	
	private Properties getUriProperties(String FileName) {
		Properties properties = new Properties();
        try {
               /**
                * getAssets() Return an AssetManager instance for your
                * application's package. AssetManager Provides access to an
                * application's raw asset files;
                */
               AssetManager assetManager = this.getAssets();
               /**
                * Open an asset using ACCESS_STREAMING mode. This
                */
               InputStream inputStream = assetManager.open(FileName);
               /**
                * Loads properties from the specified InputStream,
                */
               properties.load(inputStream);

        } catch (IOException e) {
               // TODO Auto-generated catch block
               Log.e("AssetsPropertyReader",e.toString());
        }
        return properties;

 }
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Initialize GStreamer and warn if it fails
        try {
        	// Please create your uri.properties.default property file and put it into the assets folder.
        	// That folder already contains the uri.properties file that you can use as template for your own property file. 
        	Properties uriProps = getUriProperties("uri.properties.default");
        	 String uri = uriProps.getProperty("uri_1"); 
        	 String uri2 = uriProps.getProperty("uri_2"); 
        	 
        	 // Surface for the Stream 1
             SurfaceView sv = (SurfaceView) this.findViewById(R.id.surface_video);
             
             // Surface for the Stream 2
             SurfaceView sv2 = (SurfaceView) this.findViewById(R.id.surface_video2);
            
        	 Log.d("DualStreaming","URI 1:" + uri + "URI 2:" + uri2);
        	 
        	 // Each GStreamerBackend instance handles a video stream
        	 
        	 // First stream
        	 gstInstance = new GStreamerBackend("Stream_1", this.getApplicationContext(), this, uri, 200, sv);
        	 
        	 // Second stream
        	 
        	 gstInstance2 = new GStreamerBackend("Stream_2", this.getApplicationContext(), this, uri2, 250, sv2);
        	 
        	 Log.d(TAG, "GStreamer Backends instanced. ");
             
        } catch (Exception e) {
        	Log.e(TAG, "Error initializing GStreamer Backend: " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish(); 
            return;
        }

       

        ImageButton play = (ImageButton) this.findViewById(R.id.button_play);
        play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                is_playing_desired = true;
                gstInstance.play();
                gstInstance2.play();
            }
        });

        ImageButton pause = (ImageButton) this.findViewById(R.id.button_stop);
        pause.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                is_playing_desired = false;
                gstInstance.pause();
                gstInstance2.pause();
            }
        });

        if (savedInstanceState != null) {
            is_playing_desired = savedInstanceState.getBoolean("playing");
            Log.i ("GStreamer", "Activity created. Saved state is playing:" + is_playing_desired);
        } else {
            is_playing_desired = false;
            Log.i ("GStreamer", "Activity created. There is no saved state, playing: false");
        }

        // Start with disabled buttons, until native code is initialized
        this.findViewById(R.id.button_play).setEnabled(false);
        this.findViewById(R.id.button_stop).setEnabled(false);

    }

    protected void onSaveInstanceState (Bundle outState) {
        Log.d ("GStreamer", "Saving state, playing:" + is_playing_desired);
        outState.putBoolean("playing", is_playing_desired);
    }

    protected void onDestroy() {
    	Log.d(TAG, "CALLED ON DESTROY!");
    	
    	// finalize native resource of the first stream
    	gstInstance.finalizeLib();
    	
    	// finalize native resource of the second stream
    	gstInstance2.finalizeLib();
    	
    	// finalize global references of the native gstreamer library
        //gstInstance.finalizeGlobals();
        super.onDestroy();
    }

    // Called from native code. This sets the content of the TextView from the UI thread.
    public void setMessage(GStreamerBackend backend, final String message) {
        final TextView tv = (TextView) this.findViewById(R.id.textview_message);
        runOnUiThread (new Runnable() {
          public void run() {
            tv.setText(message);
          }
        });
    }

    // Called from native code. Native code calls this once it has created its pipeline and
    // the main loop is running, so it is ready to accept commands.
    public void onGStreamerInitialized (GStreamerBackend backend) {
        Log.i (TAG, "Gst initialized. Restoring state, playing:" + is_playing_desired);
        
       
        if (backend==gstInstance)
        {
        	// Just for testing, we invoke the native method to get the current latency of the first stream
        	Log.i (TAG, "Current latency on Instance 1:" + backend.getLatency());
        
        }else if (backend==gstInstance2)
            {
        	  // Just for testing, we invoke the native method to get the current latency of the second  stream
            	Log.i (TAG, "Current latency on Instance 2:" + backend.getLatency());
            }
        
        // Restore previous playing state
        if (is_playing_desired) {
            gstInstance.play();
            gstInstance2.play();
        } else {
        	gstInstance.pause();
        	gstInstance2.pause();
        }

        // Re-enable buttons, now that GStreamer is initialized
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            public void run() {
                activity.findViewById(R.id.button_play).setEnabled(true);
                activity.findViewById(R.id.button_stop).setEnabled(true);
            }
        });
    }
    
    
	@Override
	public void onMediaSizeChanged(GStreamerBackend gStreamerBackend,
			int width, int height) {
		Log.i ("GStreamerSurfaceView", "Media size changed from native gstreamer to::: " + width + "x" + height);
        final GStreamerSurfaceView gsv = (most.streaming.example.GStreamerSurfaceView) gStreamerBackend.getSurfaceView();
        gsv.media_width = width;
        gsv.media_height = height;
        runOnUiThread(new Runnable() {
            public void run() {
                gsv.requestLayout();
            }
        });
		
	}

    

    

}
