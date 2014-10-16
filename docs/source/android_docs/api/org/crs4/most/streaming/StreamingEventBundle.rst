.. java:import:: org.crs4.most.streaming.enums StreamingEvent

.. java:import:: org.crs4.most.streaming.enums StreamingEventType

StreamingEventBundle
====================

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public class StreamingEventBundle

   This class represents a container of all the informations of the events triggered by the Streaming Library.

Constructors
------------
StreamingEventBundle
^^^^^^^^^^^^^^^^^^^^

.. java:constructor:: public StreamingEventBundle(StreamingEventType eventType, StreamingEvent event, String info, Object data)
   :outertype: StreamingEventBundle

   This object contains all the informations of any event triggered by the Streaming Library.

   :param eventType: the type of this event
   :param event: the event
   :param info: a textual information describing this event
   :param data: a generic object containing event-specific informations (the object type depends on the type of the event). In particular:

   ..

   * events of type \ :java:ref:`StreamingEventType.STREAM_EVENT`\  contain the \ :java:ref:`IStream`\  object that triggered this event

Methods
-------
getData
^^^^^^^

.. java:method:: public Object getData()
   :outertype: StreamingEventBundle

   Get a generic object containing event-specific informations (the object type depends on the type of the event). Note that events of type \ :java:ref:`StreamingEventType.STREAM_EVENT`\  contain the \ :java:ref:`IStream`\  object that triggered this event

   :return: a generic object containing event-specific informations

getEvent
^^^^^^^^

.. java:method:: public StreamingEvent getEvent()
   :outertype: StreamingEventBundle

   Get the triggered event

   :return: the event triggered by the library

getEventType
^^^^^^^^^^^^

.. java:method:: public StreamingEventType getEventType()
   :outertype: StreamingEventBundle

   Get the event type

   :return: the event type

getInfo
^^^^^^^

.. java:method:: public String getInfo()
   :outertype: StreamingEventBundle

   Get a textual description of this event

   :return: a textual description of this event

