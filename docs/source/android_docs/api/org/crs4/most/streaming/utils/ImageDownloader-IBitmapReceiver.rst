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

ImageDownloader.IBitmapReceiver
===============================

.. java:package:: org.crs4.most.streaming.utils
   :noindex:

.. java:type:: public interface IBitmapReceiver
   :outertype: ImageDownloader

   An ImageDownloader user must provide this interface for receiving notifications about image donwloading and storing.

Methods
-------
onBitmapDownloaded
^^^^^^^^^^^^^^^^^^

.. java:method:: public void onBitmapDownloaded(ImageDownloader imageDownloader, Bitmap image)
   :outertype: ImageDownloader.IBitmapReceiver

   Called when a bitmap was successfully downloaded

   :param imageDownloader: the ImageDownloader that has triggered this callback
   :param image: the downloaded bitmap

onBitmapDownloadingError
^^^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public void onBitmapDownloadingError(ImageDownloader imageDownloader, Exception ex)
   :outertype: ImageDownloader.IBitmapReceiver

   Called when the image downloading failed for some raison

   :param imageDownloader: the ImageDownloader that has triggered this callback
   :param ex: the exception raised during the downloading process

onBitmapSaved
^^^^^^^^^^^^^

.. java:method:: public void onBitmapSaved(ImageDownloader imageDownloader, String filename)
   :outertype: ImageDownloader.IBitmapReceiver

   Called when the bitmp was stored in to the internal storage

   :param imageDownloader: the ImageDownloader that has triggered this callback
   :param filename: the name of the stored file

onBitmapSavingError
^^^^^^^^^^^^^^^^^^^

.. java:method:: public void onBitmapSavingError(ImageDownloader imageDownloader, Exception ex)
   :outertype: ImageDownloader.IBitmapReceiver

   Called when the image saving failed for some raison

   :param imageDownloader: the ImageDownloader that has triggered this callback
   :param ex: the exception raised during the saving process

