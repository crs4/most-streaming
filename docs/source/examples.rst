
Examples
========

Android
-------

 * **DualStreamingExample** located into the folder *examples/android*. This example shows how to simultaneously play two streams on two video surfaces inside an Android Activity by using the underlying native gstreamer backend.
 
  For running the Android example, import the project in your preferred IDE (e.g Eclipse) and do the following changes:
   * Edit the file *jni/Android.mk* and properly change the absolute path of the environment variables GSTREAMER_SDK_ROOT_ANDROID and GSTREAMER_ROOT 
   * Create your *uri.properties.default* property file and put it into the *assets* folder.(That folder already contains the *uri.properties* file that you can use as template for your own property file)
   * Build the project (Note that the NDK must be installed and configurated on your system in order to build the project)
   * Deploy the application on your android device or on your emulator 
   
iOS
---

* **TestDualStream** located into the folder *examples/ios8*. This example shows how to simultaneously play two streams on two video surfaces inside an iOS Application by using the underlying native gstreamer backend.
 
 