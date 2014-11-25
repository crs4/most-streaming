.. java:import:: org.crs4.most.streaming.enums StreamProperty

.. java:import:: org.crs4.most.streaming.enums StreamState

.. java:import:: org.crs4.most.streaming.utils Size

.. java:import:: android.view SurfaceView

IStream
=======

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public interface IStream

   An IStream object represents a single audio/video stream object. You can obtain a new IStream object by calling the method \ :java:ref:`StreamingLib.createStream(java.util.HashMap,android.os.Handler)`\ .

Methods
-------
commitProperties
^^^^^^^^^^^^^^^^

.. java:method:: public boolean commitProperties(StreamProperties properties)
   :outertype: IStream

   Commit the stream properties values specified as argument

   :param properties: the stream properties to update
   :return: true if no error occurred during the update request; False otherwise

destroy
^^^^^^^

.. java:method:: public void destroy()
   :outertype: IStream

   Destroy this stream

getErrorMsg
^^^^^^^^^^^

.. java:method:: public String getErrorMsg()
   :outertype: IStream

   Get detailed informations about a stream error (return an empty stream if the stream is not in Stream.ERROR state)

   :return: infomrations about the type of stream error

getName
^^^^^^^

.. java:method:: public String getName()
   :outertype: IStream

   :return: the name of this stream

getProperty
^^^^^^^^^^^

.. java:method:: public Object getProperty(StreamProperty property)
   :outertype: IStream

   Reads the current value of the specified stream property

   :param property:
   :return: the value of the property

getState
^^^^^^^^

.. java:method:: public StreamState getState()
   :outertype: IStream

   :return: the current state of this stream

getVideoSize
^^^^^^^^^^^^

.. java:method:: public Size getVideoSize()
   :outertype: IStream

   :return: the current size of the video stream

loadStillImage
^^^^^^^^^^^^^^

.. java:method:: public boolean loadStillImage(String uri)
   :outertype: IStream

   Load a still image from the remote camera, provided the uri

   :param uri: the uri pointing to the image to load
   :return: \ ``true``\  if no error occurred during the operation, \ ``false``\  otherwise

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

