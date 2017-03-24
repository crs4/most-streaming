/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */


package it.crs4.most.streaming;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import it.crs4.most.streaming.enums.StreamProperty;
import it.crs4.most.streaming.enums.StreamState;
import it.crs4.most.streaming.enums.StreamingEvent;
import it.crs4.most.streaming.enums.StreamingEventType;
import it.crs4.most.streaming.utils.Size;

//import com.gstreamer.GStreamer;


//  the scope of this class is reserved to this current package. Don't instance this class, but use the StreamingFactory.getStream() method instead.
class GStreamerBackend implements IStream {

    private static final String TAG = "GStreamerBackend";
    // local fields

    private String mStreamUri = null;
    private String mStreamName = null;
    private StreamState mStreamState = StreamState.DEINITIALIZED;
    private int mLatency = 200;
    private static boolean libInitialized = false;
    private boolean mStreamInitialized = false;
    private boolean mPlayScheduled = false;

    private Handler mCustomNotificationHandler = null;
    private StreamHandler streamHandler;
    private SurfaceView mStreamSurfaceView;
    private Size mVideoSize = null;
    private String mErrorMsg;
    private ArrayList<IEventListener> mIEventListeners = new ArrayList<>();

    static {
        Log.d(TAG, "Loading streaming lib backend native libraries..");
        Log.d(TAG, "Loading most_streaming...");
        System.loadLibrary("most_streaming");
        Log.d(TAG, "Libraries loaded.");
        Log.d(TAG, "Loading native class and methods references...");
        libInitialized = nativeClassInit();
    }


    // native methods
    private native boolean nativeInit(String streamName, int latency, boolean frameAvailable);     // Initialize native code, build pipeline, etc

    private native void nativeFinalize(); // Destroy pipeline and shutdown native code

    private native boolean nativeSetUri(String uri); // Set the URI of the media to play

    private native boolean nativeSetUriAndLatency(String uri, int latency); // Set the URI of the media to play

    private native int nativeGetLatency(); // Get the latency of the stream to play

    private native boolean nativeSetLatency(int latency); // Set the latency of the stream to play

    private native void nativePlay();     // Set pipeline to PLAYING

    private native void nativeSetPosition(int milliseconds); // Seek to the indicated position, in milliseconds

    private native void nativePause();    // Set pipeline to PAUSED

    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks

    private native void nativeSurfaceInit(Object surface); // A new surface is available

    private native void nativeSurfaceFinalize(); // Surface about to be destroyed

    private long native_custom_data;      // Native code will use this to keep private data: DO NOT RENAME


    private static class StreamHandler extends Handler {

        private final WeakReference<GStreamerBackend> stream;

        public StreamHandler(GStreamerBackend stream) {
            this.stream = new WeakReference<>(stream);
        }

        private GStreamerBackend getStream() {
            return stream.get();
        }

        @Override
        public void handleMessage(Message streamingMessage) {
            StreamingEventBundle event = (StreamingEventBundle) streamingMessage.obj;
            String infoMsg = "Event Type:" + event.getEventType() + " ->" + event.getEvent() + ":" + event.getInfo();
            Log.d(TAG, "handleMessage: Current Event:" + infoMsg);
            String name = ((IStream) event.getData()).getName();
            if (! name.equals(getStream().getName())) {
                return;
            }

            if (event.getEvent() == StreamingEvent.VIDEO_SIZE_CHANGED) {

                Size videoSize = ((IStream) event.getData()).getVideoSize();
                if (videoSize != null) {
                    int width = videoSize.getWidth();
                    int height = videoSize.getHeight();
                    Log.d(TAG, String.format("VIDEOSIZE width %s, height %d", width, height));
                    getStream().onVideoSizeChanged(width, height);
                }
            }
            else if (event.getEvent() == StreamingEvent.STREAM_STATE_CHANGED) {
                StreamState streamState = ((IStream) event.getData()).getState();
                Log.d(TAG, name + " streamState " + streamState);
                if (streamState.equals(StreamState.PAUSED)){
                    getStream().onPause();
                }
                else if (streamState.equals(StreamState.PLAYING)){
                    getStream().onPlay();
                }
            }
        }
    }

    public GStreamerBackend(HashMap<String, String> configParams) throws Exception {
        this(configParams, null);
    }
    public GStreamerBackend(HashMap<String, String> configParams, Handler notificationHandler) throws Exception {
        if (!libInitialized) {
            throw new Exception("Error initilializing the native library.");
        }
        if (mStreamInitialized) {
            throw new Exception("Error preparing the stream since it is already initialized");
        }

//        if (notificationHandler == null) {
//            throw new IllegalArgumentException("Handler parameter cannot be null");
//        }

        if (!configParams.containsKey("name")) {
            throw new IllegalArgumentException("param name not found in configParams");
        }
        if (!configParams.containsKey("uri")) {
            throw new IllegalArgumentException("param uri not found in configParams ");
        }

        mCustomNotificationHandler = notificationHandler;
        streamHandler = new StreamHandler(this);
        mStreamName = configParams.get("name");
        mStreamUri = configParams.get("uri");
        mLatency = configParams.containsKey("latency") ? Integer.valueOf(configParams.get("latency")) : 200;
    }

    private void notifyState(StreamingEventBundle stateBundle) {
        Message m;
        if (mCustomNotificationHandler != null) {
            m = Message.obtain(mCustomNotificationHandler, stateBundle.getEventType().ordinal(), stateBundle);
            m.sendToTarget();
        }
        m = Message.obtain(streamHandler, stateBundle.getEventType().ordinal(), stateBundle);
        m.sendToTarget();

    }

    @Override
    public void prepare(SurfaceView surface) {
        prepare(surface, false);
    }

    @Override
    public void prepare(SurfaceView surface, boolean frameCallback) {
        Log.d(TAG, "Preparing IStream instance...");
        initStream(surface, frameCallback);
    }

    private void initStream(SurfaceView surface, boolean frameCallback) {
        if (surface == null) {
            mStreamState = StreamState.DEINITIALIZED;
            notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
                                                 StreamingEvent.STREAM_ERROR,
                                                 "No valid surface provided for the stream: " + mStreamName, this));
            return;
        }

        if (mStreamState != StreamState.DEINITIALIZED) {
            Log.d(TAG, "The stream is currently on state:" + mStreamState + " prepare method ignored..");
            return;
        }

        mStreamState = StreamState.INITIALIZING;
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
            StreamingEvent.STREAM_STATE_CHANGED, "Inizializating Stream " + mStreamName, this));

        boolean nativeInitResult = nativeInit(mStreamName, mLatency, frameCallback);
        if (!nativeInitResult) {
            mStreamState = StreamState.DEINITIALIZED;
            notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
                StreamingEvent.STREAM_ERROR, "Stremm initialization failed:" + mStreamName + " initialized", this));
        }
        else {
            mStreamSurfaceView = surface;
            mStreamSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    Log.d(TAG, "Surface changed to format " + format + " width for stream " + getName() +
                            + width + " height " + height);
//                    surfaceInit(holder.getSurface());
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    Log.d(TAG, "Surface created for stream " + getName());
                    surfaceInit(holder.getSurface());
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    Log.d(TAG, "Surface destroyed for stream " + getName());
                    surfaceFinalize();
                    // if the surface was destroyed we also destroy the stream
//                    destroy();
                }

            });
            if (surface.getHolder().getSurface().isValid()) {
                Log.d(TAG, "Surface ready, calling surfaceInit");
                surfaceInit(surface.getHolder().getSurface());
            }
        }
    }

    @Override
    public void setSurface(SurfaceHolder surface) {

    }

    @Override
    public Size getVideoSize() {
        return mVideoSize;
    }

    /**
     * @return the rendering Surface
     */
    public SurfaceView getStreamSurfaceView() {
        return mStreamSurfaceView;
    }

    /**
     * Play the stream
     */
    @Override
    public void play() {
        Log.d(TAG, "Trying to play stream " + getName());
        if (getState() == StreamState.PLAYING) {
            Log.d(TAG, "The stream is already playing...request ignored");
            return;
        }

        mStreamState = StreamState.PLAYING_REQUEST;
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Playing request for Stream: " + mStreamName, this));
        Log.d(TAG, "before call nativePlay, current state : " + mStreamState);
//		if (mStreamState.ordinal() < StreamState.PAUSED.ordinal()){
//			mPlayScheduled = true;
//			Log.d(TAG, "play scheduled");
//		}
//
//		else
        nativePlay();
        Log.d(TAG, "nativePlay called");
    }

    /**
     * pause the stream
     */
    @Override
    public void pause() {
        Log.d(TAG, "Trying to pause stream " + getName());
        nativePause();
    }

    @Override
    public void destroy() {
        Log.d(TAG, "Called destroy() on stream " + getName());
        if (mStreamState == StreamState.DEINITIALIZED) {
            Log.d(TAG, "Stream " + getName() + " already deinitialized...");
            return;
        }
        mStreamState = StreamState.DEINITIALIZING;
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
                                             StreamingEvent.STREAM_STATE_CHANGED,
                                             "Deinizializating stream " + mStreamName, this));
        nativeFinalize();
    }

    public void surfaceInit(Surface surface) {
        nativeSurfaceInit(surface);
    }

    public void surfaceFinalize() {
        nativeSurfaceFinalize();
    }

    // Called from native code
    private void setMessage(final String message) {
        Log.d(TAG, "Message from Gstreamer:" + message + " " + getName());
        //gstListener.onMessageReceived(this,"From Backend:" + message);
    }

    // Called from native code
    private void onGStreamerInitialized() {
        Log.d(TAG, "Called onGStreamerInitialized()");

        mStreamState = StreamState.INITIALIZED;
        Log.d(TAG, "Stream initialized");
        if (!mStreamInitialized) {
            setUri(mStreamUri);
            mStreamInitialized = true;
            notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
                StreamingEvent.STREAM_STATE_CHANGED,
                "Stream " + mStreamName + " initialized: (mStreamUri:" + mStreamUri + ")", this));
        }
        else {
            Log.w(TAG, "Stream already initialized.Unexpected callback from gstreamer ?!");
        }

    }

    //Called from the native code when an error occurred
    private void onStreamError(String info) {
        String infoMsg = getName() + ":" + info;
        Log.e(TAG, "Stream Error:" + info);
        mErrorMsg = info;
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_ERROR, infoMsg, this));
        mStreamState = StreamState.ERROR;
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream state changed to:" + mStreamState, this));
    }

    // Called from native code when the mStreamState of the native stream changes
    private void onStreamStateChanged(int oldState, int newState) {
        Log.d(TAG, "onStreamStateChanged: state from state:" + oldState + " to:" + newState +
                " for stream " + getName());
        //mStreamState = GStreamerBackend.getStreamStateByGstState(newState);
        // from pause to play or from play to pause
        if ((oldState == 3 && newState == 4) || (oldState == 4 && newState == 3)) {
            mStreamState = GStreamerBackend.getStreamStateByGstState(newState);
            notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream state changed to:" + mStreamState, this));
        }
        // stream deinitialized
        else if ((oldState >= 2 && newState <= 1)) {
            mStreamInitialized = false;
            mStreamState = GStreamerBackend.getStreamStateByGstState(newState);

            notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT, StreamingEvent.STREAM_STATE_CHANGED, "Stream state changed to:" + mStreamState, this));
        }
        if (mStreamInitialized && mPlayScheduled) {
            nativePlay();
            mPlayScheduled = false;

        }
    }

    // GST_STATE_VOID_PENDING        = 0,
    // GST_STATE_NULL                = 1, // no resources allocated
    // GST_STATE_READY               = 2, // all no-streaming specific resources allocated
    // GST_STATE_PAUSED              = 3, // all resources allocated: stream ready to play
    // GST_STATE_PLAYING             = 4
    private static StreamState getStreamStateByGstState(int gstState) {
        if (gstState < 2) {
            return StreamState.DEINITIALIZED;
        }
        else if (gstState == 2) {
            return StreamState.INITIALIZED;
        }
        else if (gstState == 3) {
            return StreamState.PAUSED;
        }
        else if (gstState == 4) {
            return StreamState.PLAYING;
        }
        else {
            throw new IllegalArgumentException("Unknown pipeline mStreamState received from gstreamer native code:" + gstState);
        }
    }

    // Called from native code
    private void setCurrentPosition(final int position, final int duration) {
        //Log.i (TAG, "setCurrentPosition: " + position + " on duration;" + duration);
    }

    // Called from native code when the size of the media changes or is first detected.
    // Inform the video surface about the new size and recalculate the layout.
    private void onMediaSizeChanged(int width, int height) {
        Log.i(TAG, "Media size changed to " + width + "x" + height);
        mVideoSize = new Size(width, height);
        notifyState(new StreamingEventBundle(StreamingEventType.STREAM_EVENT,
            StreamingEvent.VIDEO_SIZE_CHANGED, "Stream state changed to:" + mStreamState, this));
    }

//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        Log.d(TAG, "Surface changed to format " + format + " width for stream " + getName() +
//            + width + " height " + height);
//        surfaceInit(holder.getSurface());
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        Log.d(TAG, "Surface created for stream " + getName());
//        surfaceInit(holder.getSurface());
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        Log.d(TAG, "Surface destroyed for stream " + getName());
//        surfaceFinalize();
//        // if the surface was destroyed we also destroy the stream
//        destroy();
//    }

    @Override
    public String getName() {
        return mStreamName;
    }

    @Override
    public StreamState getState() {
        return mStreamState;
    }

    private String getStreamUri() {
        return mStreamUri;
    }

    /**
     * Update the mStreamUri of the stream
     *
     * @param uri the new mStreamUri
     * @return {@code True} if the mStreamUri was successfully updated; {@code False} otherwise.
     */
    private boolean setUri(String uri) {

        Log.d(TAG, "Setting mStreamUri to:" + uri);
        boolean uriUpdated = nativeSetUri(uri);
        if (uriUpdated) {
            mStreamUri = uri;
            Log.d(TAG, "mStreamUri updated to:" + uri);
        }
        else Log.d(TAG, "mStreamUri NOT updated! current value is ->" + uri);
        return uriUpdated;
    }

    /**
     * Get the current value of mLatency property of this stream (Reads the value from native code to be sure to
     * return the effective mLatency value)
     *
     * @return the mLatency value in ms
     */
    private int getLatency() {
        mLatency = nativeGetLatency();
        return mLatency;
    }

    private boolean setLatency(int latency) {
        Log.d(TAG, "Called setLatency with proposed value:" + latency);
        return nativeSetLatency(latency);
    }

    private boolean setUriAndLatency(String uri, int latency) {
        Log.d(TAG, "Called setUriAndLatency with proposed mStreamUri:" + uri + " mLatency:" + latency);
        boolean result = nativeSetUriAndLatency(uri, latency); // Set the URI of the media to play
        Log.d(TAG, "result setUriAndLatency: " + result);
        return result;
    }// Set the URI of the media to play

    @Override
    public boolean commitProperties(StreamProperties properties) {
        String uriProperty = properties.get(StreamProperty.URI);
        String latencyProperty = properties.get(StreamProperty.LATENCY);
        if (uriProperty != null) {
            mStreamUri = uriProperty;
        }
        if (latencyProperty != null) {
            try {
                mLatency = Integer.parseInt(latencyProperty);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return setUriAndLatency(mStreamUri, mLatency);
    }

    @Override
    public Object getProperty(StreamProperty property) {
        if (property == StreamProperty.NAME) {
            return mStreamName;
        }
        else if (property == StreamProperty.URI) {
            return getStreamUri();
        }
        else if (property == StreamProperty.LATENCY) {
            return String.valueOf(getLatency());
        }
        else if (property == StreamProperty.VIDEO_SIZE) {
            return mVideoSize;
        }
        else if (property == StreamProperty.STATE) {
            return mStreamState;
        }
        return null;
    }

    @Override
    public boolean loadStillImage(String uri) {
        return nativeSetUri(uri);
    }

    @Override
    public String getErrorMsg() {
        if (mStreamState == StreamState.ERROR) {
            return mErrorMsg;
        }
        else {
            return "";
        }
    }

    @Override
    public void addEventListener(IEventListener listener) {
        mIEventListeners.add(listener);
    }

    @Override
    public void removeEventListener(IEventListener listener) {
        mIEventListeners.remove(listener);
    }

    //FIXME: Should be private?
    public void onFrameAvailable(byte[] frame) {
        for (IEventListener l : mIEventListeners) {
            l.frameReady(frame);
        }
    }

    private void onVideoSizeChanged(int width, int height) {
        for (IEventListener l : mIEventListeners) {
            l.onVideoChanged(width, height);
        }
    }

    private void onPlay() {
        for (IEventListener l : mIEventListeners) {
            l.onPlay();
        }
    }
    private void onPause() {
        for (IEventListener l : mIEventListeners) {
            l.onPause();
        }
    }
}


