LOCAL_PATH:= $(call my-dir)
MY_LOCAL_PATH := $(LOCAL_PATH)

#GSTREAMER_SDK_ROOT_ANDROID := /home/mauro/work/gstreamer-1.0-android-arm-1.2.4.1-debug
#GSTREAMER_SDK_ROOT := /home/mauro/work/gstreamer-1.0-android-universal-1.10.2/arm
GSTREAMER_SDK_ROOT := $(GSTREAMER_ROOT)
ifndef GSTREAMER_SDK_ROOT
    ifndef GSTREAMER_SDK_ROOT_ANDROID
        $(error GSTREAMER_SDK_ROOT_ANDROID is not defined!)
    endif
    GSTREAMER_SDK_ROOT        := $(GSTREAMER_SDK_ROOT_ANDROID)
endif

include $(CLEAR_VARS)

LOCAL_MODULE    := most_streaming
LOCAL_SRC_FILES := most_streaming.c
LOCAL_SHARED_LIBRARIES := gstreamer_android
LOCAL_LDLIBS := -llog -landroid
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := gst-rtsp-streaming
LOCAL_SRC_FILES := gst-rtsp-streaming.c
LOCAL_SHARED_LIBRARIES := gstreamer_android
LOCAL_LDLIBS := -llog -landroid
LOCAL_STATIC_LIBRARIES := rtsp-server omx
include $(BUILD_SHARED_LIBRARY)



include $(CLEAR_VARS)
LOCAL_MODULE := gstnet-lib
LOCAL_SRC_FILES :=$(GSTREAMER_SDK_ROOT)/lib/libgstnet-1.0.a
LOCAL_EXPORT_C_INCLUDES +=$(GSTREAMER_SDK_ROOT)/include/gstreamer-1.0/
LOCAL_EXPORT_C_INCLUDES +=$(GSTREAMER_SDK_ROOT)/include/glib-2.0/gobject
LOCAL_EXPORT_C_INCLUDES +=$(GSTREAMER_SDK_ROOT)/include/glib-2.0/
LOCAL_EXPORT_C_INCLUDES +=$(GSTREAMER_SDK_ROOT)/lib/glib-2.0/include
include $(PREBUILT_STATIC_LIBRARY)


include $(CLEAR_VARS)

LOCAL_MODULE := rtsp-server
LOCAL_SRC_FILES := /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-address-pool.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-auth.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-client.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-context.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-media.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-media-factory.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-media-factory-uri.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-mount-points.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-params.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-permissions.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-sdp.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-server.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-session.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-session-media.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-session-pool.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-stream.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-stream-transport.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-thread-pool.c
LOCAL_SRC_FILES += /home/mauro/work/gst-rtsp-server/gst/rtsp-server/rtsp-token.c

LOCAL_CPPFLAGS := -std=gnu++0x -Wall -D__ANDROID__
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
LOCAL_SHARED_LIBRARIES := gstreamer_android
LOCAL_STATIC_LIBRARIES := gstnet-lib

include $(BUILD_STATIC_LIBRARY)




GSTREAMER_NDK_BUILD_PATH  := $(GSTREAMER_SDK_ROOT)/share/gst-android/ndk-build
include $(GSTREAMER_NDK_BUILD_PATH)/plugins.mk
# GSTREAMER_PLUGINS := $(GSTREAMER_PLUGINS_CORE) $(GSTREAMER_PLUGINS_SYS)  $(GSTREAMER_PLUGINS_VIS) $(GSTREAMER_PLUGINS_EFFECTS) $(GSTREAMER_PLUGINS_NET)   $(GSTREAMER_PLUGINS_NET_RESTRICTED)  $(GSTREAMER_PLUGINS_CODECS)  $(GSTREAMER_PLUGINS_CODECS_GPL) $(GSTREAMER_PLUGINS_CODECS_RESTRICTED) $(GSTREAMER_PLUGINS_PLAYBACK)
GSTREAMER_PLUGINS := $(GSTREAMER_PLUGINS_CORE) $(GSTREAMER_PLUGINS_SYS) $(GSTREAMER_PLUGINS_EFFECTS) \
                    $(GSTREAMER_PLUGINS_NET) $(GSTREAMER_PLUGINS_NET_RESTRICTED) \
                    $(GSTREAMER_PLUGINS_CODECS) $(GSTREAMER_PLUGINS_CODECS_RESTRICTED) \
                    $(GSTREAMER_PLUGINS_PLAYBACK) $(GSTREAMER_PLUGINS_ENCODING)

GSTREAMER_EXTRA_DEPS := gstreamer-1.0 gstreamer-video-1.0
include $(GSTREAMER_NDK_BUILD_PATH)/gstreamer-1.0.mk

