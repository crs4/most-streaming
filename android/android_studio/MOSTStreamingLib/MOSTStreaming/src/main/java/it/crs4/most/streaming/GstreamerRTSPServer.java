package it.crs4.most.streaming;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gstreamer.GStreamer;

public class GstreamerRTSPServer implements StreamServer {

    private static String TAG = "GstreamerRTSPServer";
    private native void nativeInit();     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativePushFrame(byte[] data, int data_len);
    private long native_custom_data;

    static {
        System.loadLibrary("gstreamer_android");
        Log.d(TAG, "loaded gstreamer_android");
        System.loadLibrary("gst-rtsp-streaming");
        Log.d(TAG, "loaded gst-rtsp-streaming");
        nativeClassInit();
    }

    public GstreamerRTSPServer(Context ctx) throws RuntimeException{
        try{
            GStreamer.init(ctx);
            Log.d(TAG, "GStreamer.init called");
        } catch (Exception e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG).show();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void start() {
        nativeInit();
    }

    @Override
    public void stop() {

    }

    @Override
    public void feedData(byte[] data) {
        nativePushFrame(data, data.length);

    }

}
