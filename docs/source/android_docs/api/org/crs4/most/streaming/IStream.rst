.. java:import:: java.util HashMap

.. java:import:: org.crs4.most.streaming.enums StreamState

.. java:import:: android.content Context

.. java:import:: android.os Handler

.. java:import:: android.view SurfaceView

IStream
=======

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public interface IStream

   An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling one of the methods provided by the class \ :java:ref:`StreamingFactory`\ .

Methods
-------
destroy
^^^^^^^

.. java:method:: public void destroy()
   :outertype: IStream

   Destroy this stream

getLatency
^^^^^^^^^^

.. java:method:: public int getLatency()
   :outertype: IStream

   Get the current value of latency property of this stream (Reads the value from native code to be sure to return the effective latency value)

   :return: the latency value in ms

getName
^^^^^^^

.. java:method:: public String getName()
   :outertype: IStream

   :return: the name of this stream

getState
^^^^^^^^

.. java:method:: public StreamState getState()
   :outertype: IStream

   :return: the current state of this stream

pause
^^^^^

.. java:method:: public void pause()
   :outertype: IStream

   pause the stream

play
^^^^

.. java:method:: public void play()
   :outertype: IStream

   Play the stream

prepare
^^^^^^^

.. java:method:: public void prepare(Context context, SurfaceView surface, HashMap<String, String> configParams, Handler notificationHandler) throws Exception
   :outertype: IStream

   Instance a new Streaming object

   :param context: the application context
   :param surfaceView: the Surface where to render the stream
   :param configParams: All needed configuration string parameters. All the supported parameters are the following:

   ..

   * name: (mandatory) the name of the stream (it must be unique for stream)
   * uri: (mandatory) the uri of the stream (it can be also changed later)
   * latency: (optional) the preferred latency of the stream in ms (default value: 200 ms)
   :param notificationHandler: the handler where to receive all notifications from the Library
   :throws Exception:

setUri
^^^^^^

.. java:method:: public void setUri(String uri)
   :outertype: IStream

   Update the uri of the stream

   :param uri: the new uri

