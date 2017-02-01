#include <string.h>
#include <jni.h>
#include <android/log.h>
#include <gst/gst.h>
#include <pthread.h>
#include <gst/rtsp-server/rtsp-server.h>
#include <gst/app/gstappsrc.h>
#include <gst/video/video.h>





GST_DEBUG_CATEGORY_STATIC (debug_category);
#define GST_CAT_DEFAULT debug_category

/*
 * These macros provide a way to store the native pointer to CustomData, which might be 32 or 64 bits, into
 * a jlong, which is always 64 bits, without warnings.
 */
#if GLIB_SIZEOF_VOID_P == 8
# define GET_CUSTOM_DATA(env, thiz, fieldID) (CustomData *)(*env)->GetLongField (env, thiz, fieldID)
# define SET_CUSTOM_DATA(env, thiz, fieldID, data) (*env)->SetLongField (env, thiz, fieldID, (jlong)data)
#else
# define GET_CUSTOM_DATA(env, thiz, fieldID) (CustomData *)(jint)(*env)->GetLongField (env, thiz, fieldID)
# define SET_CUSTOM_DATA(env, thiz, fieldID, data) (*env)->SetLongField (env, thiz, fieldID, (jlong)(jint)data)
#endif

#define DEFAULT_RTSP_PORT "8554"
#define CAPS "video/x-raw-yuv,format=(fourcc)NV21,width=128,height=96"

static char *port = (char *) DEFAULT_RTSP_PORT;

/* Structure to contain all our information, so we can pass it to callbacks */
typedef struct _CustomData {
  jobject app;           /* Application instance, used to call its methods. A global reference is kept. */
  GstElement *pipeline;  /* The running pipeline */
  GMainContext *context; /* GLib context used to run the main loop */
  GMainLoop *main_loop;  /* GLib main loop */
  gboolean initialized;  /* To avoid informing the UI multiple times about the initialization */
  guint64 frame_num;
  GstElement *appsrc;
  gboolean accept_data;
} CustomData;

/* These global variables cache values which are not changing during execution */
static pthread_t gst_app_thread;
static pthread_key_t current_jni_env;
static JavaVM *java_vm;
static jfieldID custom_data_field_id;
//static jmethodID set_push_frame;

/*
 * Private methods
 */

/* Register this thread with the VM */
static JNIEnv *attach_current_thread (void) {
  JNIEnv *env;
  JavaVMAttachArgs args;

  GST_DEBUG ("Attaching thread %p", g_thread_self ());
  args.version = JNI_VERSION_1_4;
  args.name = NULL;
  args.group = NULL;

  if ((*java_vm)->AttachCurrentThread (java_vm, &env, &args) < 0) {
    GST_ERROR ("Failed to attach current thread");
    return NULL;
  }

  return env;
}

/* Unregister this thread from the VM */
static void detach_current_thread (void *env) {
  GST_DEBUG ("Detaching thread %p", g_thread_self ());
  (*java_vm)->DetachCurrentThread (java_vm);
}

/* Retrieve the JNI environment for this thread */
static JNIEnv *get_jni_env (void) {
  JNIEnv *env;

  if ((env = pthread_getspecific (current_jni_env)) == NULL) {
    env = attach_current_thread ();
    pthread_setspecific (current_jni_env, env);
  }

  return env;
}



/* Retrieve errors from the bus and show them on the UI */
static void error_cb (GstBus *bus, GstMessage *msg, CustomData *data) {
  GError *err;
  gchar *debug_info;
  gchar *message_string;

  gst_message_parse_error (msg, &err, &debug_info);
  message_string = g_strdup_printf ("Error received from element %s: %s", GST_OBJECT_NAME (msg->src), err->message);
  g_clear_error (&err);
  g_free (debug_info);
  g_free (message_string);
  gst_element_set_state (data->pipeline, GST_STATE_NULL);
}

/* Notify UI about pipeline state changes */
static void state_changed_cb (GstBus *bus, GstMessage *msg, CustomData *data) {
  GstState old_state, new_state, pending_state;
  gst_message_parse_state_changed (msg, &old_state, &new_state, &pending_state);
  /* Only pay attention to messages coming from the pipeline, not its children */
  if (GST_MESSAGE_SRC (msg) == GST_OBJECT (data->pipeline)) {
    gchar *message = g_strdup_printf("State changed to %s", gst_element_state_get_name(new_state));
    g_free (message);
  }
}

/* Check if all conditions are met to report GStreamer as initialized.
 * These conditions will change depending on the application */
static void check_initialization_complete (CustomData *data) {
  JNIEnv *env = get_jni_env ();
  if (!data->initialized && data->main_loop) {
    GST_DEBUG ("Initialization complete, notifying application. main_loop:%p", data->main_loop);
    if ((*env)->ExceptionCheck (env)) {
      GST_ERROR ("Failed to call Java method");
      (*env)->ExceptionClear (env);
    }
    data->initialized = TRUE;
  }
}

typedef struct
{
  gboolean white;
  GstClockTime timestamp;
} MyContext;


//push frame data to the appsrc
static jboolean gst_native_push_data(JNIEnv * env, jobject thiz, jbyteArray byteArray, jint  data_len) {
        GST_DEBUG("gst_native_push_data");
        GstBuffer *buffer;
        jbyte *temp;
        GstFlowReturn ret;
        GstMapInfo map;

        CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
        GST_DEBUG("data->accept_data %d", data->accept_data);
        if(!data->accept_data){
            return JNI_TRUE;
        }

        jint frame_rate = 15;
        buffer = gst_buffer_new_allocate (NULL, data_len, NULL);
        gst_buffer_map(buffer,&map,GST_MAP_WRITE);

        temp = (*env)->GetByteArrayElements(env, byteArray,  JNI_FALSE);
        memcpy((char*)map.data,temp, (int) data_len);

        GST_BUFFER_TIMESTAMP (buffer) = gst_util_uint64_scale (data->frame_num, GST_SECOND, frame_rate);
        GST_BUFFER_DURATION (buffer) = gst_util_uint64_scale (1, GST_SECOND, frame_rate);

        GST_DEBUG("pushing buffer");

        //gst_app_src_push_buffer(data->appsrc, buffer);
        g_signal_emit_by_name (data->appsrc, "push-buffer", buffer, &ret);
        gst_buffer_unmap(buffer,&map);
        data->frame_num++;
        (*env)->ReleaseByteArrayElements(env, byteArray, temp, JNI_ABORT);
        gst_buffer_unref(buffer);
        if(ret == GST_FLOW_OK){
         return JNI_TRUE;
        }
        return JNI_FALSE;
}

/* This signal callback is called when appsrc needs data, we add an idle handler
 * to the mainloop to start pushing data into the appsrc */
static void start_feed (GstElement * pipeline, guint size, CustomData *data){
    GST_DEBUG("start_feed");
    data->accept_data = 1;
    JNIEnv *env = get_jni_env();
    //(*env)->CallVoidMethod (env, data->app, set_push_frame, TRUE);
}

static void stop_feed(GstElement * pipeline, guint size, CustomData *data){
    GST_DEBUG("stop_feed");
    data->accept_data = 0;
    JNIEnv *env = get_jni_env();
    //(*env)->CallVoidMethod (env, data->app, set_push_frame, FALSE);
}



/* called when a new media pipeline is constructed. We can query the
 * pipeline and configure our appsrc */
static void media_configure(GstRTSPMediaFactory * factory, GstRTSPMedia * media, CustomData *data){
    GST_DEBUG("media_configure");
    GstElement *element;
    MyContext *ctx;

    /* get the element used for providing the streams of the media */
    element = gst_rtsp_media_get_element (media);

    /* get our appsrc, we named it 'mysrc' with the name property */
    data->appsrc = gst_bin_get_by_name_recurse_up (GST_BIN (element), "cam_src");

    /* this instructs appsrc that we will be dealing with timed buffer */
    gst_util_set_object_arg (G_OBJECT (data->appsrc), "format", "time");
    // configure the caps of the video

    g_object_set (G_OBJECT (data->appsrc), "caps",
      gst_caps_new_simple ("video/x-raw",
          "format", G_TYPE_STRING, "NV21",
          "width", G_TYPE_INT, 320,
          "height", G_TYPE_INT, 240,
          "framerate", GST_TYPE_FRACTION, 15, 1,
          NULL), NULL);


    //make sure ther datais freed when the media is gone
    g_object_set_data_full (G_OBJECT (media), "my-extra-data", data->context, (GDestroyNotify) g_free);

    g_signal_connect (data->appsrc, "need-data", G_CALLBACK (start_feed), data);
    g_signal_connect (data->appsrc, "enough-data", G_CALLBACK (stop_feed), data);
    //gst_app_src_set_max_bytes (data->appsrc, 1*460800);

  /* install the callback that will be called when a buffer is needed */
  //g_signal_connect (data->appsrc, "need-data", (GCallback) need_data, ctx);
}


/* Main method for the native code. This is executed on its own thread. */
static void *app_function (void *userdata) {
    JavaVMAttachArgs args;
    GstBus *bus;
    CustomData *data = (CustomData *)userdata;
    GSource *bus_source;
    GError *error = NULL;
    GstRTSPServer *server;
    GstRTSPMountPoints *mounts;
    GstRTSPMediaFactory *factory;
     GMainLoop *loop;


    GST_DEBUG ("Creating pipeline in CustomData at %p", data);

    /* Create our own GLib Main Context and make it the default one */
    data->context = g_main_context_new ();
    g_main_context_push_thread_default(data->context);

    data->accept_data = 0;
    server = gst_rtsp_server_new ();
    data->pipeline = server;
    g_object_set (server, "service", DEFAULT_RTSP_PORT, NULL);

    /* get the mount points for this server, every server has a default object
      * that be used to map uri mount points to media factories */
    mounts = gst_rtsp_server_get_mount_points (server);

     /* make a media factory for a test stream. The default media factory can use
      * gst-launch syntax to create pipelines.
      * any launch line works as long as it contains elements named pay%d. Each
      * element with pay%d names will be a stream */
    factory = gst_rtsp_media_factory_new ();
    gst_rtsp_media_factory_set_launch (factory,
          "( appsrc name=cam_src is-live=1 ! videoconvert ! jpegenc ! queue ! rtpjpegpay name=pay0 pt=96  )");

    //"( appsrc name=cam_src is-live=1 ! queue max-size-buffers=1000 ! videoconvert ! omx_h264enc tune=zerolatency ! rtph264pay name=pay0 pt=96 )");

      /* notify when our media is ready, This is called whenever someone asks for
       * the media and a new pipeline with our appsrc is created */
    g_signal_connect (factory, "media-configure", (GCallback) media_configure, data);

    /* attach the test factory to the /test url */
    gst_rtsp_mount_points_add_factory (mounts, "/test", factory);

    /* don't need the ref to the mapper anymore */
    g_object_unref (mounts);

    /* attach the server to the default maincontext */
    if (gst_rtsp_server_attach (server, data->context) == 0) {
        GST_ERROR("FAILED TO ATTACH THE SERVER");
    }

    /* Build pipeline

    loop = g_main_loop_new (NULL, FALSE);
    //g_timeout_add_seconds (2, (GSourceFunc) timeout, server);
	g_main_loop_run (loop);

    */
    /* Create a GLib Main Loop and set it to run */
    GST_DEBUG ("Entering main loop... (CustomData:%p)", data);
    data->main_loop = g_main_loop_new (data->context, FALSE);
    check_initialization_complete (data);
    g_main_loop_run (data->main_loop);
    GST_DEBUG ("Exited main loop");
    g_main_loop_unref (data->main_loop);
    data->main_loop = NULL;

    /* Free resources */
    g_main_context_pop_thread_default(data->context);
    g_main_context_unref (data->context);
    gst_element_set_state (data->pipeline, GST_STATE_NULL);
    gst_object_unref (data->pipeline);

    return NULL;
}

/*
 * Java Bindings
 */

/* Instruct the native code to create its internal data structure, pipeline and thread */
static void gst_native_init (JNIEnv* env, jobject thiz) {
  CustomData *data = g_new0 (CustomData, 1);
  SET_CUSTOM_DATA (env, thiz, custom_data_field_id, data);
  GST_DEBUG_CATEGORY_INIT (debug_category, "most-streaming-rtsp", 0, "most-streaming-rtsp");
  gst_debug_set_threshold_for_name("gst-rtsp-streaming", GST_LEVEL_DEBUG);
  //gst_debug_set_default_threshold(GST_LEVEL_DEBUG);
  gst_debug_set_threshold_for_name("rtspserver", GST_LEVEL_LOG);
  GST_DEBUG ("Created CustomData at %p", data);
  data->app = (*env)->NewGlobalRef (env, thiz);
  GST_DEBUG ("Created GlobalRef for app object at %p", data->app);
  pthread_create (&gst_app_thread, NULL, &app_function, data);
}

/* Quit the main loop, remove the native thread and free resources */
static void gst_native_finalize (JNIEnv* env, jobject thiz) {
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data) return;
  GST_DEBUG ("Quitting main loop...");
  g_main_loop_quit (data->main_loop);
  GST_DEBUG ("Waiting for thread to finish...");
  pthread_join (gst_app_thread, NULL);
  GST_DEBUG ("Deleting GlobalRef for app object at %p", data->app);
  (*env)->DeleteGlobalRef (env, data->app);
  GST_DEBUG ("Freeing CustomData at %p", data);
  g_free (data);
  SET_CUSTOM_DATA (env, thiz, custom_data_field_id, NULL);
  GST_DEBUG ("Done finalizing");
}

/* Set pipeline to PLAYING state */
static void gst_native_play (JNIEnv* env, jobject thiz) {
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data) return;
  //GST_DEBUG ("Setting state to PLAYING");
  //gst_element_set_state (data->pipeline, GST_STATE_PLAYING);
}

/* Set pipeline to PAUSED state */
static void gst_native_pause (JNIEnv* env, jobject thiz) {
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data) return;
  //GST_DEBUG ("Setting state to PAUSED");
  //gst_element_set_state (data->pipeline, GST_STATE_PAUSED);
}

/* Static class initializer: retrieve method and field IDs */
static jboolean gst_native_class_init (JNIEnv* env, jclass klass) {
  custom_data_field_id = (*env)->GetFieldID (env, klass, "native_custom_data", "J");

  //set_push_frame = (*env)->GetMethodID (env, klass, "setPushFrame", "(Z)V");

  if (!custom_data_field_id) {
    /* We emit this message through the Android log instead of the GStreamer log because the later
     * has not been initialized yet.
     */
    __android_log_print (ANDROID_LOG_ERROR, "tutorial-2", "The calling class does not implement all necessary interface methods");
    return JNI_FALSE;
  }
  return JNI_TRUE;
}

/* List of implemented native methods */
static JNINativeMethod native_methods[] = {
  { "nativeInit", "()V", (void *) gst_native_init},
  { "nativeFinalize", "()V", (void *) gst_native_finalize},
  { "nativePlay", "()V", (void *) gst_native_play},
  { "nativePause", "()V", (void *) gst_native_pause},
  { "nativeClassInit", "()Z", (void *) gst_native_class_init},
  { "nativePushFrame", "([BI)V", (void *) gst_native_push_data}
};

/* Library initializer */
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
  JNIEnv *env = NULL;

  java_vm = vm;

  if ((*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4) != JNI_OK) {
    __android_log_print (ANDROID_LOG_ERROR, "cam-streaming", "Could not retrieve JNIEnv");
    return 0;
  }
  jclass klass = (*env)->FindClass (env, "it/crs4/most/streaming/GstreamerRTSPServer");
  (*env)->RegisterNatives (env, klass, native_methods, G_N_ELEMENTS(native_methods));

  pthread_key_create (&current_jni_env, detach_current_thread);

  return JNI_VERSION_1_4;
}


