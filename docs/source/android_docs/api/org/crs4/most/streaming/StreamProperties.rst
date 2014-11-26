.. java:import:: java.util Properties

.. java:import:: org.crs4.most.streaming.enums StreamProperty

StreamProperties
================

.. java:package:: org.crs4.most.streaming
   :noindex:

.. java:type:: public class StreamProperties

   This class collects a set of stream properties a user intend to apply to a stream.

   **See also:** :java:ref:`IStream.commitProperties(StreamProperties)`

Methods
-------
add
^^^

.. java:method:: public StreamProperties add(StreamProperty streamProperty, String value)
   :outertype: StreamProperties

   Add a new property

   :param streamProperty: the stream property to update
   :param value: the value to be set for this property
   :return: this StreamProperties so you can chain more properties to add

get
^^^

.. java:method:: public String get(StreamProperty property)
   :outertype: StreamProperties

   Get the specified property value

   :param property: the stream property key
   :return: the specified property value(or null if the key was not found)

getAll
^^^^^^

.. java:method:: public Properties getAll()
   :outertype: StreamProperties

   Get all added properties

   :return: the added properties

remove
^^^^^^

.. java:method:: public String remove(StreamProperty streamProperty)
   :outertype: StreamProperties

   Remove the specified property

   :param streamProperty: the property to remove
   :return: value of the removed property, or null if this property was not found

