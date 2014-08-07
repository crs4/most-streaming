===============
Getting started
===============

Installation
~~~~~~~~~~~~

**Users** is a Django application, indipendent from any Django project.

To use **Users** in your project, edit your settings.py file adding it
to INSTALLED\_APPS and properly setting AUTH\_USER\_MODEL, LOGIN\_URL
and LOGOUT\_URL as follow:

.. code:: python

    INSTALLED_APPS = (
        'django.contrib.admin',
        'django.contrib.auth',
        'django.contrib.contenttypes',
        'django.contrib.sessions',
        'django.contrib.messages',
        'django.contrib.staticfiles',
        # ...
        # your apps go here
        # ...
        'users',
    )
    # ...
    AUTH_USER_MODEL = 'users.MostUser'

    LOGIN_URL = '/users/user/login/'
    LOGOUT_URL = '/users/user/logout/'
