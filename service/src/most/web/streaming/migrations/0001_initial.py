# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import most.web.utils


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='DeviceModel',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(unique=True, max_length=100)),
                ('description', models.CharField(max_length=200, blank=True)),
                ('streaming_url_template', models.CharField(max_length=250, blank=True)),
                ('web_url_template', models.CharField(max_length=250, blank=True)),
                ('shot_url_template', models.CharField(max_length=250, blank=True)),
                ('ptz_url_template', models.CharField(max_length=250, blank=True)),
                ('has_streaming', models.BooleanField(default=True)),
                ('has_web', models.BooleanField(default=False)),
                ('has_shot', models.BooleanField(default=False)),
                ('has_ptz', models.BooleanField(default=False)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='StreamingDevice',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('uuid', models.CharField(default=most.web.utils.pkgen, unique=True, max_length=40)),
                ('name', models.CharField(max_length=100)),
                ('ip_address', models.CharField(max_length=100)),
                ('type', models.CharField(max_length=20, verbose_name='Device Type', choices=[(b'AV', b'Audio Video'), (b'VIDEO', b'Video Only')])),
                ('user', models.CharField(max_length=100)),
                ('password', models.CharField(max_length=100)),
                ('model', models.ForeignKey(related_name='devices', to='streaming.DeviceModel')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
