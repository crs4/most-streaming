StreamState
===========

.. java:package:: org.crs4.most.streaming.enums
   :noindex:

.. java:type:: public enum StreamState

Enum Constants
--------------
DEINITIALIZED
^^^^^^^^^^^^^

.. java:field:: public static final StreamState DEINITIALIZED
   :outertype: StreamState

   the stream has not been initialized yet , the initialization failed or it has been deinitialized

DEINITIALIZING
^^^^^^^^^^^^^^

.. java:field:: public static final StreamState DEINITIALIZING
   :outertype: StreamState

   the stream has being deinitialized

ERROR
^^^^^

.. java:field:: public static final StreamState ERROR
   :outertype: StreamState

   The stream is in an inconsistent state

INITIALIZED
^^^^^^^^^^^

.. java:field:: public static final StreamState INITIALIZED
   :outertype: StreamState

   the stream was successfully initialized and it is ready to play

INITIALIZING
^^^^^^^^^^^^

.. java:field:: public static final StreamState INITIALIZING
   :outertype: StreamState

   the stream has being initialized

PAUSED
^^^^^^

.. java:field:: public static final StreamState PAUSED
   :outertype: StreamState

   the stream is in pause state

PLAYING
^^^^^^^

.. java:field:: public static final StreamState PLAYING
   :outertype: StreamState

   the stream is playing

PLAYING_REQUEST
^^^^^^^^^^^^^^^

.. java:field:: public static final StreamState PLAYING_REQUEST
   :outertype: StreamState

   a play request is sent to the stream that is preparing to start playing

