.. java:import:: java.util HashMap

.. java:import:: java.util Map

.. java:import:: org.crs4.most.streaming.enums PTZ_Direction

.. java:import:: org.crs4.most.streaming.enums PTZ_Zoom

.. java:import:: android.content Context

.. java:import:: android.util Base64

.. java:import:: android.util Log

.. java:import:: com.android.volley AuthFailureError

.. java:import:: com.android.volley Request

.. java:import:: com.android.volley RequestQueue

.. java:import:: com.android.volley Response

.. java:import:: com.android.volley VolleyError

.. java:import:: com.android.volley.toolbox StringRequest

.. java:import:: com.android.volley.toolbox Volley

PTZ_Manager
===========

.. java:package:: org.crs4.most.streaming.ptz
   :noindex:

.. java:type:: public class PTZ_Manager

Constructors
------------
PTZ_Manager
^^^^^^^^^^^

.. java:constructor:: public PTZ_Manager(Context ctx, String uri, String username, String password)
   :outertype: PTZ_Manager

   Handles ptz commands of a remote Axis webcam. (tested on Axis 214 PTZ model)

   :param ctx: The activity context
   :param uri: The ptz uri of the webcam
   :param username: the username used for ptz authentication
   :param password: the username used for ptz authentication

Methods
-------
getPassword
^^^^^^^^^^^

.. java:method:: public String getPassword()
   :outertype: PTZ_Manager

   Get the password used for the authentication

   :return: the password

getUri
^^^^^^

.. java:method:: public String getUri()
   :outertype: PTZ_Manager

   Get the uri connection string

   :return: the uri used for connecting to the webcam

getUsername
^^^^^^^^^^^

.. java:method:: public String getUsername()
   :outertype: PTZ_Manager

   Get the username used for the authentication

   :return: the username

goTo
^^^^

.. java:method:: public void goTo(String preset)
   :outertype: PTZ_Manager

   Move the webcam to the position (and/or zoom value) specified by the preset passed as argument

   :param preset: the preset name

startMove
^^^^^^^^^

.. java:method:: public void startMove(PTZ_Direction direction)
   :outertype: PTZ_Manager

   Start moving the webcam to the specified direction

   :param direction: the direction (\ :java:ref:`PTZ_Direction.STOP`\ } stops the webcam)

startMove
^^^^^^^^^

.. java:method:: public void startMove(PTZ_Direction direction, int speed)
   :outertype: PTZ_Manager

   Start moving the webcam to the specified direction and speed

   :param direction: the moving direction
   :param speed: the speed

startZoom
^^^^^^^^^

.. java:method:: public void startZoom(PTZ_Zoom zoomDirection)
   :outertype: PTZ_Manager

   Start zooming to the specified direction

   :param zoomDirection: the zoom direction

startZoom
^^^^^^^^^

.. java:method:: public void startZoom(PTZ_Zoom zoomDirection, int speed)
   :outertype: PTZ_Manager

   Start zooming to the specified direction and speed

   :param zoomDirection: the zoom directiom
   :param speed: the zoom speed

stopMove
^^^^^^^^

.. java:method:: public void stopMove()
   :outertype: PTZ_Manager

   Stop the pan and/or tilt movement of the webcam

stopZoom
^^^^^^^^

.. java:method:: public void stopZoom()
   :outertype: PTZ_Manager

   Stop the zoom

zoom
^^^^

.. java:method:: public void zoom(int value)
   :outertype: PTZ_Manager

   Zoom the webcam to the specified value (positive values are for zoom-in, negative values for zoom-out)

   :param value:

