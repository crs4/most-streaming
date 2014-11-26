.. java:import:: java.io File

.. java:import:: java.io FileFilter

.. java:import:: java.io FileInputStream

.. java:import:: java.io FileOutputStream

.. java:import:: java.util ArrayList

.. java:import:: java.util HashMap

.. java:import:: java.util Map

.. java:import:: android.content Context

.. java:import:: android.graphics Bitmap

.. java:import:: android.graphics BitmapFactory

.. java:import:: android.os Environment

.. java:import:: android.util Base64

.. java:import:: android.util Log

.. java:import:: android.widget ImageView

.. java:import:: com.android.volley AuthFailureError

.. java:import:: com.android.volley RequestQueue

.. java:import:: com.android.volley Response

.. java:import:: com.android.volley.toolbox ImageRequest

.. java:import:: com.android.volley.toolbox Volley

ImageDownloader
===============

.. java:package:: org.crs4.most.streaming.utils
   :noindex:

.. java:type:: public class ImageDownloader

   This class provides utility methods for downloading, storing and loading images from/to the internal storage. Note that the image downloading process is asynchronous, so it uses the interface \ :java:ref:`ImageDownloader.IBitmapReceiver`\  for notifying the user about the downloading outcomes.

Fields
------
receiver
^^^^^^^^

.. java:field::  IBitmapReceiver receiver
   :outertype: ImageDownloader

Constructors
------------
ImageDownloader
^^^^^^^^^^^^^^^

.. java:constructor:: public ImageDownloader(IBitmapReceiver receiver, Context ctx, String username, String password)
   :outertype: ImageDownloader

   This class handles asynchronous image downloads. Once a image has been successfully downloaded, it calls the method \ :java:ref:`ImageDownloader.IBitmapReceiver.onBitmapReady(Bitmap)`\ }

   :param receiver: the object that will receive the downloaded image
   :param ctx: the activity context
   :param username: the user name (needed for authentication)
   :param password: the password (needed for authentication)

Methods
-------
deleteInternalFile
^^^^^^^^^^^^^^^^^^

.. java:method:: public static boolean deleteInternalFile(Context ctx, String filename)
   :outertype: ImageDownloader

   Deletes an image from the internal storage

   :param ctx: the context the activity context
   :param filename: the name (no path) )of the file to be deleted
   :return: true if the file was deleted , false otherwise

downloadImage
^^^^^^^^^^^^^

.. java:method:: public void downloadImage(String url)
   :outertype: ImageDownloader

   asynchronously downloads an image from the web

   :param url: the url image

getInternalImages
^^^^^^^^^^^^^^^^^

.. java:method:: public static File[] getInternalImages(Context ctx)
   :outertype: ImageDownloader

   Get the list of .jpeg images stored in the internal archive (private to the activity context passed as argument)

   :param ctx: the activity context
   :return: the list of .jpg images

getInternalImages
^^^^^^^^^^^^^^^^^

.. java:method:: public static File[] getInternalImages(Context ctx, String filter)
   :outertype: ImageDownloader

   Get the list of images stored in the internal archive (private to the activity context passed as argument)

   :param ctx: the activity context
   :param filter: the image type filter (e.g: ".jpg")
   :return: the list of images

loadImageFromInternalStorage
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void loadImageFromInternalStorage(String filename)
   :outertype: ImageDownloader

   Loads an image from the internal storage. The loaded bitmap will be sent to the callback method \ :java:ref:`IBitmapReceiver.onBitmapDownloaded(ImageDownloader,Bitmap)`\

   :param filename: the name of the file to load

logAppFileNames
^^^^^^^^^^^^^^^

.. java:method:: public void logAppFileNames()
   :outertype: ImageDownloader

saveImageToInternalStorage
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void saveImageToInternalStorage(Bitmap image, String filename)
   :outertype: ImageDownloader

   Save a bitmap into to internal storage

   :param image: the bitmap to save
   :param filename: the filename of the saved bitmap

