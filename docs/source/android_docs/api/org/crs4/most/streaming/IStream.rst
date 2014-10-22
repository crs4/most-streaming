.. java:import:: org.crs4.most.streaming.enums StreamState

.. java:import:: android.view SurfaceView

IStream
=======

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public interface IStream

   An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling the method \ :java:ref:`StreamingLib.createStream(java.util.HashMap,android.os.Handler)`\ .

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

.. java:method:: public void prepare(SurfaceView surface)
   :outertype: IStream

   Prepare the stream by providing a video surface

   :param surfaceView: the Surface where to render the stream

setUri
^^^^^^

.. java:method:: public void setUri(String uri)
   :outertype: IStream

   Update the uri of the stream

   :param uri: the new uri

