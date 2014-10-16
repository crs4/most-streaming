.. java:import:: java.io FileNotFoundException

.. java:import:: java.util HashMap

.. java:import:: org.crs4.most.streaming.enums StreamState

.. java:import:: org.crs4.most.streaming.enums StreamingEvent

.. java:import:: org.crs4.most.streaming.enums StreamingEventType

.. java:import:: com.gstreamer GStreamer

.. java:import:: android.content Context

.. java:import:: android.os Handler

.. java:import:: android.os Message

.. java:import:: android.util Log

.. java:import:: android.view Surface

.. java:import:: android.view SurfaceHolder

.. java:import:: android.view SurfaceView

GStreamerBackend
================

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type::  class GStreamerBackend implements SurfaceHolder.Callback, IStream

Constructors
------------
GStreamerBackend
^^^^^^^^^^^^^^^^

.. java:constructor:: public GStreamerBackend()
   :outertype: GStreamerBackend

Methods
-------
destroy
^^^^^^^

.. java:method:: @Override public void destroy()
   :outertype: GStreamerBackend

getLatency
^^^^^^^^^^

.. java:method:: @Override public int getLatency()
   :outertype: GStreamerBackend

   Get the current value of latency property of this stream (Reads the value from native code to be sure to return the effective latency value)

   :return: the latency value in ms

getName
^^^^^^^

.. java:method:: @Override public String getName()
   :outertype: GStreamerBackend

getState
^^^^^^^^

.. java:method:: @Override public StreamState getState()
   :outertype: GStreamerBackend

getSurfaceView
^^^^^^^^^^^^^^

.. java:method:: public SurfaceView getSurfaceView()
   :outertype: GStreamerBackend

   :return: the rendering Surface

pause
^^^^^

.. java:method:: public void pause()
   :outertype: GStreamerBackend

   pause the stream

play
^^^^

.. java:method:: public void play()
   :outertype: GStreamerBackend

   Play the stream

prepare
^^^^^^^

.. java:method:: @Override public void prepare(Context context, SurfaceView surface, HashMap<String, String> configParams, Handler notificationHandler) throws Exception
   :outertype: GStreamerBackend

setUri
^^^^^^

.. java:method:: public void setUri(String uri)
   :outertype: GStreamerBackend

   Update the uri of the stream

   :param uri: the new uri

surfaceChanged
^^^^^^^^^^^^^^

.. java:method:: public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
   :outertype: GStreamerBackend

surfaceCreated
^^^^^^^^^^^^^^

.. java:method:: public void surfaceCreated(SurfaceHolder holder)
   :outertype: GStreamerBackend

surfaceDestroyed
^^^^^^^^^^^^^^^^

.. java:method:: public void surfaceDestroyed(SurfaceHolder holder)
   :outertype: GStreamerBackend

surfaceFinalize
^^^^^^^^^^^^^^^

.. java:method:: public void surfaceFinalize()
   :outertype: GStreamerBackend

surfaceInit
^^^^^^^^^^^

.. java:method:: public void surfaceInit(Surface surface)
   :outertype: GStreamerBackend

