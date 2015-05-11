from PyQt4 import QtCore
import gst
import time
import sys

class RTSPPlayerAV(QtCore.QThread):

    have_ns_view = QtCore.pyqtSignal(int, str)

    def __init__(self, player_id, address, user, pwd, controller, start_volume=0.3, volume_signal=None):

        QtCore.QThread.__init__(self)
        print 'istanziato RTSPPlayerAV'
        self.controller = controller
        self.player_id = player_id
        self.address = address
        self.user = user
        self.pwd = pwd
        self.volume = None
        self.start_volume = start_volume
        #self.have_ns_view = QtCore.SIGNAL("have_ns_view(int,int)")
        

        if volume_signal:
            self.connect(volume_signal['emitter'], volume_signal['signal'], self.on_volume_signal)

        
        self.exit = False
        self.have_video = False
        self.have_audio = False
        self.decoders = []
        
        #self.player = gst.Pipeline("singleplayer_%s" % self.player_id)
        #self.player.set_state(gst.STATE_NULL)
        #Connect signals
        #self.container.composite.gui.connect(self, QtCore.SIGNAL("updatePlayer(PyQt_PyObject)"), self.container.composite.gui.updatePlayer)
        
    def on_volume_signal(self, volume):
        if volume:
            volume = float(volume)/100
        
        self.start_volume = volume

        print '###### ---- ###### on volume signal: %s' % self.start_volume
        if self.volume:
            self.volume.set_property("volume", volume) 

     
    def run(self):

        print 'in play thread'
        self.player = gst.Pipeline("singleplayer_%s" % self.player_id)
        self.source = gst.element_factory_make("rtspsrc","src_%s" % self.player_id)
        self.dec0    = gst.element_factory_make("decodebin2", "dec1_%s" % self.player_id)
        self.cspace = gst.element_factory_make("ffmpegcolorspace", "cspace_%s" % self.player_id)
        self.vsink   = gst.element_factory_make("osxvideosink","videosink_%s" % self.player_id)

        self.dec1    = gst.element_factory_make("decodebin2", "dec2_%s" % self.player_id)
        self.aconvert = gst.element_factory_make("audioconvert", "aconvert_%s" % self.player_id)
        self.aresample = gst.element_factory_make("audioresample", "aresample_%s" % self.player_id)
        self.volume = gst.element_factory_make("volume", "volume_%s" % self.player_id)
        self.asink   = gst.element_factory_make("osxaudiosink","audiosink_%s" % self.player_id)
        self.volume.set_property("volume", float(self.start_volume))
        
        self.player.add(self.source, self.dec0, self.cspace, self.vsink, self.dec1, self.aconvert, self.aresample, self.volume, self.asink)
        gst.element_link_many(self.cspace, self.vsink)
        gst.element_link_many(self.aconvert, self.aresample, self.volume, self.asink)
        
        
        self.source.set_property("location", self.address)
        self.source.set_property("latency", 50)
        self.source.set_property("port-range", '3000-3999')
        
        if self.user is not 'none':
            self.source.set_property("user-id", self.user)
            self.source.set_property("user-pw", self.pwd)
        
        print 'settandone la property'
        self.vsink.set_property("sync", True)
        self.asink.set_property("sync", True)
        
        self.source.connect("pad-added", self.on_rtsp_ready)
        #self.dec.connect("new-decoded-pad", self.decoder_callback)
        #self.sink.connect("have-ns-view", self.on_ns_view)     
        
        bus = gst.Pipeline.get_bus(self.player)
        #bus.add_signal_watch()
        bus.enable_sync_message_emission()
        bus.connect("message", self.on_message)
        bus.connect("sync-message::element", self.on_sync_message)    
        
        
            
        self.player.set_state(gst.STATE_PLAYING)
        
        print 'after pipeline play'
        #get the current thread in Qt
        self.play_thread_id = self.currentThread        
        
        self.controller.loop.timer_add(500, self.main_loop)



    def main_loop(self):
        #print 'IN LOOP'
        if not self.exit:
            self.controller.loop.timer_add(500, self.main_loop)

        else:
            print '<<<<---------------- EXITING LOOP ---------------->>>>'



        
    def on_sync_message(self, bus, message):
        
        if message.structure is None:
            return
        
        print '###-->on sync message: %s' %  message.structure.get_name()
        
        if message.structure.get_name() == 'have-ns-view':
            pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
            #self.emit(self.have_ns_view, self.player_id, pointerAddress)
            print 'pointeraddress: %s', pointerAddress
            self.have_ns_view.emit(self.player_id, str(pointerAddress))

            print 'after emit: "%s" ' % message.structure["nsview"]
            print 'after emit: "%s" ' % str(message.structure["nsview"])[13:-1]
        
        '''
        if message.structure.get_name() == 'prepare-xwindow-id':
            print 'in prepare xwin'
            # Sync with the X server before giving the X-id to the sink
            
            #gobject.idle_add(self.videowidget.set_sink, message.src)
            #self.sink1.set_xwindow_id(self.winId)
            #message.src.set_property('force-aspect-ratio', True)
        if message.structure.get_name() == 'have-ns-view':
            if message.structure and message.structure.has_key("nsview"):
                pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
                print '##### NSVIEW POINTER ########', pointerAddress
                self.nsview = pointerAddress
                
                self.emit(QtCore.SIGNAL("updatePlayer(PyQt_PyObject)"),{'player_id' : self.container.index, 'window_xid' : self.nsview})
                print '@@@@@  AFTER EMIT @@@@@'
                pool = Foundation.NSAutoreleasePool.alloc().init()
                self.container.viewer.setMinimumWidth(640)
                self.container.viewer.setMaximumWidth(640)
                self.container.viewer.setMinimumHeight(480)
                self.container.viewer.setMaximumHeight(480)
                self.container.viewer.setCocoaView(self.nsview)
                del pool
                '''
        print 'after messages'
           
    def on_message(self, bus, message):
        print 'on message'
        
        if message.structure is None:
            return
        
        print '###-->on sync message: %s' %  message.structure.get_name()
        
        '''
        t = message.type
        if t == gst.MESSAGE_ERROR:
            err, debug = message.parse_error()
            print "Error: %s" % err, debug
            self.playing = False
        elif t == gst.MESSAGE_EOS:
            self.playing = False
        
        if message.structure and message.structure.has_key("nsview"):
            pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
            self.nsview = pointerAddress
            pool = Foundation.NSAutoreleasePool.alloc().init()
            self.container.viewer.setCocoaView(self.nsview)
            del pool
        '''
    

    def decoder_callback(self, decoder, pad, isLast=False):
        print '###-->IN DECODER CALLBACK: %s' % self.player_id
        caps = pad.get_caps()
        name = caps[0].get_name()
        print 'on_new_decoded_pad:', name
        print "Called decoder1_callback on template:%s" % pad.get_property("template")
        print "DECODER pad template:%s" % pad.get_property("template").name_template
        print '#### - ####'

        if name.startswith("video"):
            print 'IN VIDEO'
            pad.link(self.cspace.get_pad("sink"))

        elif name.startswith("audio"):
            print 'IN AUDIO'
            pad.link(self.aconvert.get_pad("sink"))
            print 'DONE AUDIO'

        else:
            print '###-->UNKNOWN STREAM'



    def on_rtsp_ready(self,decoder,pad):
         
        print "Chiamato rtsp ready 1"
        print "linking source to decoder"
        print "decoder 1 speed: %s kbps" % self.source.get_property("connection-speed")
        print "pad template:%s" % pad.get_property("name")
        try:
            if pad.get_property("name").startswith('recv_rtp_src_0'):
                self.dec0.connect("new-decoded-pad", self.decoder_callback)
                pad.link(self.dec0.get_pad("sink"))
                print '###-->After Connect Decoder DEC0'
            if pad.get_property("name").startswith('recv_rtp_src_1'):
                self.dec1.connect("new-decoded-pad", self.decoder_callback)
                pad.link(self.dec1.get_pad("sink"))
                print '###-->After Connect Decoder DEC1'
            
        except Exception,ex:
            print "Ex:" + str(ex)

    def stop(self):
        print 'before set exit'
        self.exit = True
        self.player.set_state(gst.STATE_NULL)
        print 'after set exit'

class RTSPPlayer(QtCore.QThread):

    have_ns_view = QtCore.pyqtSignal(int, str)

    def __init__(self, player_id, address,user,pwd, controller):
        
        print "@@@@@#######@@@@@@@ ISTANZIATO PLAYER SOLO VIDEO@@@@@@@@@@@@@@@@"
           
        QtCore.QThread.__init__(self)
        self.controller = controller
        self.player_id = player_id
        self.address = address
        self.user = user
        self.pwd = pwd
        
        
        self.exit = False
        
        #Connect signals
        #self.container.composite.gui.connect(self, QtCore.SIGNAL("updatePlayer(PyQt_PyObject)"), self.container.composite.gui.updatePlayer)
        
        
    def run(self):

        print 'in play thread'
        self.player = gst.Pipeline("singleplayer_%s" % self.player_id)
        self.source = gst.element_factory_make("rtspsrc","src_%s" % self.player_id)
        self.dec    = gst.element_factory_make("decodebin2", "dec_%s" % self.player_id)
        self.cspace = gst.element_factory_make("ffmpegcolorspace", "cspace_%s" % self.player_id)
        self.sink   = gst.element_factory_make("osxvideosink","videosink_%s" % self.player_id)
        
        self.player.add(self.source, self.dec, self.cspace, self.sink)
        gst.element_link_many(self.cspace, self.sink)
        
        self.source.set_property("location", self.address)
        self.source.set_property("latency", 50)
        self.source.set_property("debug", True)
        self.source.set_property("user-id", self.user)
        self.source.set_property("user-pw", self.pwd)
        self.source.set_property("port-range", '4000-4999')
        
        
        print 'settandone la property'
        self.sink.set_property("sync", False)
        
        self.source.connect("pad-added", self.on_rtsp_ready)
        self.dec.connect("new-decoded-pad", self.decoder_callback)
        #self.sink.connect("have-ns-view", self.on_ns_view)     
        
        bus = gst.Pipeline.get_bus(self.player)
        #bus.add_signal_watch()
        bus.enable_sync_message_emission()
        bus.connect("message", self.on_message)
        bus.connect("sync-message::element", self.on_sync_message)    
        
        
            
        self.player.set_state(gst.STATE_PLAYING)
        
        print 'after pipeline play'
        #get the current thread in Qt
        self.play_thread_id = self.currentThread        

        print 'after while for %s' % self.address
        print 'exit thread'
        self.controller.loop.timer_add(500, self.main_loop)



    def main_loop(self):
        #print 'IN LOOP'
        if not self.exit:
            self.controller.loop.timer_add(500, self.main_loop)

        else:
            print '<<<<---------------- EXITING LOOP ---------------->>>>'

        
    def on_sync_message(self, bus, message):
        
        if message.structure is None:
            return
        
        print 'on sync message: %s' %  message.structure.get_name()
        
        if message.structure.get_name() == 'have-ns-view':
            pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
            #self.emit(self.have_ns_view, self.player_id, pointerAddress)
            print 'pointeraddress: %s', pointerAddress
            self.have_ns_view.emit(self.player_id, str(pointerAddress))
            print 'after emit: %s ' % message.structure["nsview"]
            print 'after emit: %s ' % str(message.structure["nsview"])[13:-1]
        
        '''
        if message.structure.get_name() == 'prepare-xwindow-id':
            print 'in prepare xwin'
            # Sync with the X server before giving the X-id to the sink
            
            #gobject.idle_add(self.videowidget.set_sink, message.src)
            #self.sink1.set_xwindow_id(self.winId)
            #message.src.set_property('force-aspect-ratio', True)
        if message.structure.get_name() == 'have-ns-view':
            if message.structure and message.structure.has_key("nsview"):
                pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
                print '##### NSVIEW POINTER ########', pointerAddress
                self.nsview = pointerAddress
                
                self.emit(QtCore.SIGNAL("updatePlayer(PyQt_PyObject)"),{'player_id' : self.container.index, 'window_xid' : self.nsview})
                print '@@@@@  AFTER EMIT @@@@@'
                pool = Foundation.NSAutoreleasePool.alloc().init()
                self.container.viewer.setMinimumWidth(640)
                self.container.viewer.setMaximumWidth(640)
                self.container.viewer.setMinimumHeight(480)
                self.container.viewer.setMaximumHeight(480)
                self.container.viewer.setCocoaView(self.nsview)
                del pool
                '''
           
           
    def on_message(self, bus, message):
        print 'on message'
        
        if message.structure is None:
            return
        
        print 'on sync message: %s' %  message.structure.get_name()
        
        '''
        t = message.type
        if t == gst.MESSAGE_ERROR:
            err, debug = message.parse_error()
            print "Error: %s" % err, debug
            self.playing = False
        elif t == gst.MESSAGE_EOS:
            self.playing = False
        
        if message.structure and message.structure.has_key("nsview"):
            pointerAddress = int(str(message.structure["nsview"])[13:-1],16)
            self.nsview = pointerAddress
            pool = Foundation.NSAutoreleasePool.alloc().init()
            self.container.viewer.setCocoaView(self.nsview)
            del pool
        '''
    
    
    def decoder_callback(self, decoder, pad, isLast=False):
        caps = pad.get_caps()
        name = caps[0].get_name()
        print 'on_new_decoded_pad:', name
        print "Called decoder1_callback on template:%s" % pad.get_property("template")
        print "DECODER pad template:%s" % pad.get_property("template").name_template
        pad.link(self.cspace.get_pad("sink"))
#         if pad.get_property("template").name_template == "video_%02d":
#             queuev_pad = self.colorspace1.get_pad("sink")
#             pad.link(queuev_pad)
#         elif pad.get_property("template").name_template == "audio_%02d":
#             queuea_pad = self.colorspace1.get_pad("sink")
#             pad.link(queuea_pad)



    def on_rtsp_ready(self,decoder,pad):
        print "Chiamato rtsp ready 1"
        print "linking source to decoder"
        print "decoder 1 speed: %s kbps" % self.source.get_property("connection-speed")
        print "pad template:%s" % pad.get_property("name")
        try:
            pad.link(self.dec.get_pad("sink"))
        except Exception,ex:
            print "Ex:" + str(ex)
        


    def stop(self):
        print 'before set exit'
        self.exit = True
        self.player.set_state(gst.STATE_NULL)
        print 'after set exit'
