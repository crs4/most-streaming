
package most.streaming.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

 

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
        	Properties uriProps = getUriProperties("uri.properties.default");
        	 String uri = uriProps.getProperty("uri_1"); 
        	 String uri2 = uriProps.getProperty("uri_2"); 
        	 
        	// Surface for the Stream 1
             SurfaceView sv = (SurfaceView) this.findViewById(R.id.surface_video);
             
             // Surface for the Stream 2
             SurfaceView sv2 = (SurfaceView) this.findViewById(R.id.surface_video2);
            
        	 Log.d("DualStreaming","URI 1:" + uri + "URI 2:" + uri2);
        	 
        	 gstInstance = new GStreamerBackend(this.getApplicationContext(), this, uri, sv);
        	 gstInstance2 = new GStreamerBackend(this.getApplicationContext(), this, uri2, sv2);
        	 
        	 Log.d(TAG, "GStreamer Backend instances Ok. ");
        	 
        	 SurfaceHolder sh = sv.getHolder();
             sh.addCallback(new SurfaceCallback(gstInstance));
             
              SurfaceHolder sh2 = sv2.getHolder();
              sh2.addCallback(new SurfaceCallback(gstInstance2));
             
             Log.d(TAG, " SurfaceHolders callbacks added. ");
             
             
        } catch (Exception e) {
        	Log.e(TAG, "Error initializing GStreamer: " + e.getMessage());
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

        gstInstance.init("Stream_1");
        gstInstance2.init("Stream_2");
    }

    protected void onSaveInstanceState (Bundle outState) {
        Log.d ("GStreamer", "Saving state, playing:" + is_playing_desired);
        outState.putBoolean("playing", is_playing_desired);
    }

    protected void onDestroy() {
    	Log.d(TAG, "CALLED ON DESTROY!");
    	gstInstance.finalizeLib();
    	gstInstance2.finalizeLib();
        gstInstance.finalizeGlobals();
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
        Log.i ("GStreamer", "Gst initialized. Restoring state, playing:" + is_playing_desired);
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
		Log.i ("GStreamer", "Media size changed to::: " + width + "x" + height);
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
