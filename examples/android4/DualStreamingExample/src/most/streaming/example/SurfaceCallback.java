package most.streaming.example;

import android.util.Log;
import android.view.SurfaceHolder;

public class SurfaceCallback implements SurfaceHolder.Callback {

	private GStreamerBackend gstBackend = null;
	
	public SurfaceCallback (GStreamerBackend gstBackend)
	{
		this.gstBackend = gstBackend;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        Log.d("GStreamer", "Surface changed to format " + format + " width "
                + width + " height " + height);
        this.gstBackend.surfaceInit(holder.getSurface());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface created: " + holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface destroyed");
        this.gstBackend.surfaceFinalize();
    }

}
