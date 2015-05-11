.. java:import:: java.util HashMap

.. java:import:: org.crs4.most.streaming.enums StreamProperty

.. java:import:: org.crs4.most.streaming.enums StreamState

.. java:import:: org.crs4.most.streaming.enums StreamingEvent

.. java:import:: org.crs4.most.streaming.enums StreamingEventType

.. java:import:: org.crs4.most.streaming.utils Size

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

.. java:constructor:: public GStreamerBackend(HashMap<String, String> configParams, Handler notificationHandler) throws Exception
   :outertype: GStreamerBackend

Methods
-------
commitProperties
^^^^^^^^^^^^^^^^

.. java:method:: @Override public boolean commitProperties(StreamProperties properties)
   :outertype: GStreamerBackend

destroy
^^^^^^^

.. java:method:: @Override public void destroy()
   :outertype: GStreamerBackend

getErrorMsg
^^^^^^^^^^^

.. java:method:: @Override public String getErrorMsg()
   :outertype: GStreamerBackend

getName
^^^^^^^

.. java:method:: @Override public String getName()
   :outertype: GStreamerBackend

getProperty
^^^^^^^^^^^

.. java:method:: @Override public Object getProperty(StreamProperty property)
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

getVideoSize
^^^^^^^^^^^^

.. java:method:: @Override public Size getVideoSize()
   :outertype: GStreamerBackend

loadStillImage
^^^^^^^^^^^^^^

.. java:method:: @Override public boolean loadStillImage(String uri)
   :outertype: GStreamerBackend

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

.. java:method:: @Override public void prepare(SurfaceView surface)
   :outertype: GStreamerBackend

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

