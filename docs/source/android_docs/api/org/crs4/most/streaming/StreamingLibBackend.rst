.. java:import:: java.io FileNotFoundException

.. java:import:: java.util HashMap

.. java:import:: com.gstreamer GStreamer

.. java:import:: android.content Context

.. java:import:: android.os Handler

.. java:import:: android.util Log

StreamingLibBackend
===================

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public class StreamingLibBackend implements StreamingLib

   This class implements the \ :java:ref:`StreamingLib`\  interface. It internally uses the GStreamer library as backend. So, you can get a \ :java:ref:`StreamingLib`\  instance in the following way:

   .. parsed-literal::

      StreamingLib myStreamingLib = new StreamingLibBackend();
      myStreamingLib.initLib(getApplicationContext());

   Remember that, before using the library, you must call the method \ :java:ref:`initLib(Context)`\  to initialize it. To get a \ :java:ref:`IStream`\  instance you can call the \ :java:ref:`createStream(HashMap,Handler)`\  method:

   .. parsed-literal::

      HashMap stream1_params = new HashMap();
      stream1_params.put("name", "Stream_1");
      stream1_params.put("uri", "http://docs.gstreamer.com/media/sintel_trailer-368p.ogv");
      Handler notificationHandler = new Handler(this);
      IStream myStream = myStreamingLib.createStream(stream_1_params, notificationHandler);

   **See also:** :java:ref:`StreamingLib`

Methods
-------
createStream
^^^^^^^^^^^^

.. java:method:: @Override public IStream createStream(HashMap<String, String> configParams, Handler notificationHandler) throws Exception
   :outertype: StreamingLibBackend

initLib
^^^^^^^

.. java:method:: @Override public void initLib(Context context) throws Exception
   :outertype: StreamingLibBackend

