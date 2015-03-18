#
# Project MOST - Moving Outcomes to Standard Telemedicine Practice
# http://most.crs4.it/
#
# Copyright 2014, CRS4 srl. (http://www.crs4.it/)
# Dual licensed under the MIT or GPL Version 2 licenses.
# See license-GPLv2.txt or license-MIT.txt
#


from django.shortcuts import render
import datetime, json
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse
from most.web.authentication.decorators import oauth2_required
from most.web.voip.models import Account, Buddy


def test(request):

    return HttpResponse(json.dumps({'success': True, 'data': {'message': 'Hello Voip'}}), content_type="application/json")


