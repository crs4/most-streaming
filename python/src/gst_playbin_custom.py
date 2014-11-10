# -*- coding:utf-8 -*-
# GStreamer SDK Tutorials in Python
#
# basic-tutorial-1
#
"""
basic-tutorial-1: Hello world!
http://docs.gstreamer.com/display/GstSDK/Basic+tutorial+2%3A+GStreamer+concepts
"""

# see https://wiki.ubuntu.com/Novacut/GStreamer1.0#element_link_many.28.29
import gi
gi.require_version('Gst', '1.0')
from gi.repository import Gst
print "Gstreamer version:"  + str(Gst.version())
Gst.init(None)

gst_uri="playbin uri=http://docs.gstreamer.com/media/sintel_trailer-480p.webm"
#gst_uri="playbin uri=rtsp://<user>:<pwd>@<ip>/mpeg4/media.amp latency=50"


pipeline = Gst.parse_launch(gst_uri)
# http://<user>:<pwd>@<ip>/axis-cgi/jpg/image.cgi?resolution=CIF

# Create the elements inside the sink bin
equalizer = Gst.ElementFactory.make ("equalizer-3bands", "equalizer")
convert = Gst.ElementFactory.make ("audioconvert", "convert")
sink =Gst.ElementFactory.make("autoaudiosink", "audio_sink")

if not equalizer or not convert or not sink:
    print ("Not all elements could be created.\n")
    raise Exception("Not all elements could be created.\n")

# Create the sink bin, add the elements and link them 
#gst_bin_add_many (GST_BIN (bin), equalizer, convert, sink, NULL);
bin = Gst.Bin.new ("audio_sink_bin")
bin.add(equalizer)
bin.add(convert)
bin.add(sink)

#gst_element_link_many (equalizer, convert, sink, NULL);
equalizer.link(convert)
convert.link(sink)

#pad = gst_element_get_static_pad (equalizer, "sink");
pad = equalizer.get_static_pad('sink')

# ghost_pad = gst_ghost_pad_new ("sink", pad)
ghost_pad = Gst.GhostPad.new("sink", pad)

#gst_pad_set_active (ghost_pad, TRUE);
ghost_pad.set_active(True)

#gst_element_add_pad (bin, ghost_pad);
bin.add_pad(ghost_pad)

#gst_object_unref (pad);


# Start playing
pipeline.set_state(Gst.State.PLAYING)

# Wait until error or EOS
bus = pipeline.get_bus()
msg = bus.timed_pop_filtered(
Gst.CLOCK_TIME_NONE, Gst.MessageType.ERROR | Gst.MessageType.EOS)
# Free resources
pipeline.set_state(Gst.State.NULL)
