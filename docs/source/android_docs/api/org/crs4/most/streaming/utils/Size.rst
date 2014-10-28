Size
====

.. java:package:: org.crs4.most.streaming.utils
   :noindex:

.. java:type:: public class Size

   Image size (width and height dimensions).

Constructors
------------
Size
^^^^

.. java:constructor:: public Size(int w, int h)
   :outertype: Size

   Sets the dimensions for pictures.

   :param w: the photo width (pixels)
   :param h: the photo height (pixels)

Methods
-------
equals
^^^^^^

.. java:method:: @Override public boolean equals(Object obj)
   :outertype: Size

   Compares \ ``obj``\  to this size.

   :param obj: the object to compare this size with.
   :return: \ ``true``\  if the width and height of \ ``obj``\  is the same as those of this size. \ ``false``\  otherwise.

getHeight
^^^^^^^^^

.. java:method:: public int getHeight()
   :outertype: Size

getWidth
^^^^^^^^

.. java:method:: public int getWidth()
   :outertype: Size

hashCode
^^^^^^^^

.. java:method:: @Override public int hashCode()
   :outertype: Size

toString
^^^^^^^^

.. java:method:: @Override public String toString()
   :outertype: Size

