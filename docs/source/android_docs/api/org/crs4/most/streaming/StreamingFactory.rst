.. java:import:: java.util HashMap

.. java:import:: android.content Context

.. java:import:: android.os Handler

StreamingFactory
================

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public class StreamingFactory

   This class provide factory methods for creating \ :java:ref:`IStream`\  objects.

Methods
-------
getIStream
^^^^^^^^^^

.. java:method:: public static IStream getIStream(HashMap<String, String> configParams, Handler notificationHandler) throws Exception
   :outertype: StreamingFactory

   This factory method provides a new IStrean instance

   :param configParams: All needed configuration string parameters. All the supported parameters are the following:

   ..

   * name: (mandatory) the name of the stream (it must be unique for stream)
   * uri: (mandatory) the uri of the stream (it can be also changed later)
   * latency: (optional) the preferred latency of the stream in ms (default value: 200 ms)
   :param notificationHandler: the handler where to receive all notifications from the Library
   :throws Exception: if an error occurred during the stream initialization

