#
# Project MOST - Moving Outcomes to Standard Telemedicine Practice
# http://most.crs4.it/
#
# Copyright 2014, CRS4 srl. (http://www.crs4.it/)
# Dual licensed under the MIT or GPL Version 2 licenses.
# See license-GPLv2.txt or license-MIT.txt
#


from django.db import models
from most.web.utils import pkgen
from django.utils.translation import ugettext_lazy as _

DEVICE_TYPES = (
    ('AV', 'Audio Video'),
    ('VIDEO', 'Video Only'),
)


class DeviceModel(models.Model):
    name = models.CharField(max_length=100, unique=True)
    description = models.CharField(max_length=200, blank=True)
    streaming_url_template = models.CharField(max_length=250, blank=True)
    web_url_template = models.CharField(max_length=250, blank=True)
    shot_url_template = models.CharField(max_length=250, blank=True)
    ptz_url_template = models.CharField(max_length=250, blank=True)
    has_streaming = models.BooleanField(default=True)
    has_web = models.BooleanField(default=False)
    has_shot = models.BooleanField(default=False)
    has_ptz = models.BooleanField(default=False)

    def __unicode__(self):
        return '[Device Model: {name} - {description}]'.format(name=self.name, description=self.description)


# Create your models here.
class StreamingDevice(models.Model):
    uuid = models.CharField(max_length=40, unique=True, default=pkgen)
    name = models.CharField(max_length=100)
    ip_address = models.CharField(max_length=100)
    type = models.CharField(_('Device Type'), choices=DEVICE_TYPES, max_length=20)
    model = models.ForeignKey(DeviceModel, related_name="devices")
    user = models.CharField(max_length=100)
    password = models.CharField(max_length=100)

    def __unicode__(self):
        return '[Device: {name}]'.format(name=self.name)


    def _get_streaming_url(self):

        if self.model.has_streaming:
            streaming_template = self.model.streaming_url_template
            return streaming_template.format(ip_address=self.ip_address, user=self.user, password=self.password)
        else:
            return None
    streaming_url = property(_get_streaming_url)

    def _get_web_url(self):

        if self.model.has_web:
            web_template = self.model.web_url_template
            return web_template.format(ip_address=self.ip_address, user=self.user, password=self.password)
        else:
            return None
    web_url = property(_get_web_url)

    def _get_shot_url(self):

        if self.model.has_shot:
            shot_template = self.model.shot_url_template
            return shot_template.format(ip_address=self.ip_address, user=self.user, password=self.password)
        else:
            return None
    shot_url = property(_get_shot_url)

    def _get_ptz_url(self):

        if self.model.has_ptz:
            ptz_template = self.model.ptz_url_template
            return ptz_template.format(ip_address=self.ip_address, user=self.user, password=self.password)
        else:
            return None
    ptz_url = property(_get_ptz_url)

    def _get_json_dict(self):

        capabilities = {}
        #if self.model.has_streaming:
        capabilities['streaming'] = self._get_streaming_url()
        #if self.model.has_web:
        capabilities['web'] = self._get_web_url()
        #if self.model.has_shot:
        capabilities['shot'] = self._get_shot_url()
        #if self.model.has_shot:
        capabilities['ptz'] = self._get_ptz_url()

        return {
            'uuid': self.uuid,
            'name': self.name,
            'ip_address': self.ip_address,
            'user': self.user,
            'password': self.password,
            'type': self.get_type_display(),
            'model': self.model.name,
            'capabilities': capabilities,

        }
    json_dict = property(_get_json_dict)