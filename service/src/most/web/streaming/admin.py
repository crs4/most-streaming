#
# Project MOST - Moving Outcomes to Standard Telemedicine Practice
# http://most.crs4.it/
#
# Copyright 2014, CRS4 srl. (http://www.crs4.it/)
# Dual licensed under the MIT or GPL Version 2 licenses.
# See license-GPLv2.txt or license-MIT.txt
#


from django.contrib import admin
from most.web.streaming.models import StreamingDevice, DeviceModel


class StreamingDeviceAdmin(admin.ModelAdmin):
    pass


class DeviceModelAdmin(admin.ModelAdmin):
    pass


# Register your models here.
admin.site.register(StreamingDevice, StreamingDeviceAdmin)
admin.site.register(DeviceModel, DeviceModelAdmin)
