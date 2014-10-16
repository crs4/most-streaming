package org.crs4.most.streaming.examples;

import java.util.HashMap;

import org.crs4.most.streaming.IStream;
import org.crs4.most.streaming.StreamingFactory;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;

public class StreamController implements IStreamFragmentCommandReceiver {
	
	private StreamFragment streamFragment = null;
	private IStream stream = null;
	private Context ctx;
	private HashMap<String, String> streamParams = null;
	private  Handler notificationHandler = null;
	private static final String TAG = "Example2_StreamController";
	
	public StreamController(Context ctx, FragmentManager fragmentManager, int containerId, HashMap<String, String> streamParams, Handler notificationHandler) throws Exception {
		
		this.ctx = ctx;
		this.streamParams = streamParams;
		this.notificationHandler = notificationHandler;
		 
		// initialize the Fragment and add it to the container
		this.streamFragment = new StreamFragment(this);
		
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(containerId,
				this.streamFragment);
		fragmentTransaction.commit();
		
		this.stream = StreamingFactory.getIStream();
		
	}

	
	public StreamFragment getStreamFragment() {
		return streamFragment;
	}
	
	public IStream getStream() {
		return this.stream;
	}
	
	
   public void onPlay() {
	   this.stream.play();
   } 
   
   public void onPause() {
	   this.stream.pause();
   }


@Override
public void onSurfaceViewCreated(SurfaceView surfaceView) {
	try {
		this.stream.prepare(ctx, this.streamFragment.getSurfaceView(), this.streamParams, notificationHandler);
	} catch (Exception e) {
		Log.d(TAG,"error preparing the stream:" + e.getMessage());
		e.printStackTrace();
	}
	
}
  
}
