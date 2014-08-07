========
API Docs
========

.. py:module:: users

Models
======

.. py:class:: TaskGroup(title, description, task_group_type, [hospital, users, is_health_care_provider=True, is_active=True, related_task_groups])-> task_group object

   :param str title: the task group title
   :param str description: the task group specialization
   :param task_group_type: the task group type: 'SP' for Service Provider, 'HF' for Health Care Facilities.
   :type task_group_type: choice str :: 'SP' | 'HF'
   :param hospital: the task group hospital
   :type hospital: str or None
   :param users: list of MostUser belonging to the task group
   :type users: list of MostUser objects or None
   :param is_health_care_provider: True if the task group provides health care services, False otherwise
   :type is_health_care_provider: boolean or None
   :param is_active: True if the task group is active, False otherwise
   :type is_active: boolean or None
   :param related_task_groups: list of TaskGroup objects allowed to use the health care service
   :type related_task_groups: list of TaskGroup objects or None
   :return: created object
   :rtype: TaskGroup

.. py:class:: TaskGroup(django.db.models.Model)

   .. py:data:: TASK_GROUP_TYPES
   .. py:data:: ACTIVATION_STATES

   .. py:method:: clinicians_count()

   .. py:method:: clean()

   .. py:method:: __unicode__()

   .. py:method:: to_dictionary([exclude_users=False, exclude_related_task_groups=False])


.. py:class:: MostUserManager

   .. py:method:: create_user(username, first_name, last_name, email, user_type, [password=None])

      :param username:
      :param first_name:
      :param last_name:
      :param email:
      :param user_type:
      :param password:

   .. py:method:: create_superuser(username, first_name, last_name, email, user_type, password)

      :param username:
      :param first_name:
      :param last_name:
      :param email:
      :param user_type:
      :param password:


.. py:class:: MostUser extends django.db.models.Model

   :param username: the user nickname. Max length 30
   :param str first_name: the user first name. Max length 50
   :param str last_name: the user last name. Max length 50
   :param str email: the user email. Max length 250
   :param birth_date: the user birth date
   :type birth_date: datetime or None
   :param boolean is_staff: True if the user is staff member, False otherwise. Default to True
   :param boolean is_active: True if the user is active, False otherwise. Default to True
   :param boolean is_admin: True if the user has administrator privileges. Default to False
   :param str uid: the user Unique Identification Number. Auto-generated string of 40 hexadecimal digits
   :param numeric_password: the user pin. String of 4 numbers
   :type numeric_password: string or None. Max length 4
   :param str user_type: the user type: 'AD' for Administrative, 'TE' for Technician, 'CL' for Clinician, 'ST' for Student
   :type user_type: choice str :: 'AD' | 'TE' | 'CL' | 'ST'
   :param str gender: the user gender: 'M' for Male, 'F' for Female, 'U' for Unknown
   :type gender: choice str :: 'M' | 'F' | 'U'
   :param str phone: the user phone number. Max length 20
   :param str mobile: the user mobile phone number. Max length 20
   :param str certified_email: the user legal mail. Max length 255
   :param datetime creation_timestamp: the auto-generated user record creation timestamp
   :param datetime last_modified_timestamp: the auto-generated user record last modification timestamp
   :param datetime deactivation_timestamp: the auto-generated user record last deactivation timestamp
   :return: created object
   :rtype: MostUser

    .. py:data:: USER_TYPES

    .. py:data:: GENDER_CHOICES

    .. py:method:: get_full_name()

    .. py:method:: get_short_name()

    .. py:method:: __unicode__()

    .. py:method:: to_dictionary()

    .. py:staticmethod:: is_staff()

    .. py:method:: clean()

    .. py:method:: has_module_perms()


.. py:class:: ClinicianUser


REST API reference
==================

API Methods
-----------

MostUser
````````

   .. http:method:: POST /users/user/new/

      Create new user.

      :parameter str username: the user nickname. Max length 30
      :parameter str first_name: the user first name. Max length 50
      :parameter str last_name: the user last name. Max length 50
      :parameter str email: the user email. Max length 250
      :parameter datetime birth_date: the user birth date
      :parameter boolean is_staff: True if the user is staff member, False otherwise. Default to True
      :parameter boolean is_active: True if the user is active, False otherwise. Default to True
      :parameter boolean is_admin: True if the user has administrator privileges. Default to False
      :parameter int numeric_password: the user pin. String of 4 numbers
      :parameter str user_type: the user type: 'AD' for Administrative, 'TE' for Technician, 'CL' for Clinician, 'ST' for Student
      :parameter str gender: the user gender: 'M' for Male, 'F' for Female, 'U' for Unknown
      :parameter str phone: the user phone number. Max length 20
      :parameter str mobile: the user mobile phone number. Max length 20
      :parameter str certified_email: the user legal mail. Max length 255
      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully created. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the created user data in json format


   .. http:method:: POST /users/user/login/

      Log a user in the system

      :parameter str username: the user nickname
      :parameter str password: the user password
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully logged in. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the logged user data in json format


   .. http:method:: GET /users/user/logout/

      Log a user out of the system

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully logged out. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems


   .. http:method:: GET /users/user/(user_id)/get_user_info/

      Get the information of the user identified by `user_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the data of user identified by `user_id`, in json format


   .. http:method:: GET /users/user/search/

      Get a list of users matching a query string in fields: username, last_name, first_name, email or certified_email

      :parameter str query_string: the query string to search

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if users matching the query string are found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the a list of
            data of users matching the query string, in json format


   .. http:method:: POST /users/user/(user_id)/edit/

      Edit the information of the user identified by `user_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully found and updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the updated data of user identified by `user_id`, in json format


   .. http:method:: POST /users/user/(user_id)/deactivate/

      Deactivate the user identified by `user_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully deactivated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `id` (for the user id) and `is_active` (for the activation state):


   .. http:method:: POST /users/user/(user_id)/activate/

      Activate the user identified by `user_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the user is successfully activated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `id` (for the user id) and `is_active` (for the activation state)


ClinicianUser
`````````````

   .. http:method:: POST /users/clinician_user/new/

      Create new clinician user.

      :parameter int user: the user id of the related user
      :parameter str clinician_type: the clinician user type: 'DR' for Doctor or 'OP' for Operator
      :parameter str specialization: the clinician user specialization. Max length 50
      :parameter boolean is_health_care_provider: True if the clinician user is health care provider, False otherwise
      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the clinician user is successfully created. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the created user data in json format


   .. http:method:: GET /users/clinician_user/(user_id)/is_provider/

      Investigate if the clinician user, with related user identified by `user_id`, is health care provider

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the clinician user is successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `user_id` (for the related user id) and `is_health_care_provider` (for the health care provider state)


   .. http:method:: POST /users/clinician_user/(user_id)/set_provider/

      Set the clinician user, with related user identified by `user_id`, health care provider state to True

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the clinician user is successfully updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `user_id` (for the related user id) and `is_health_care_provider` (for the health care provider state)


   .. http:method:: GET /users/clinician_user/search/

      Get a list of clinician users matching a query string in fields: username, last_name, first_name, email, certified_email or specialization

      :parameter str query_string: the query string to search

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if clinician users matching the query string are found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the a list of
            data of clinician users matching the query string, in json format


   .. http:method:: GET /users/clinician_user/(user_id)/get_user_info/

      Get the information of the clinician user, with related user identified by `user_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the clinician user is successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the data of clinician user, with related user identified by `user_id`, in json format


TaskGroup
`````````

   .. http:method:: POST /users/task_group/new/

      Create new task group

      :parameter str title: the task group title. Max length 100
      :parameter str description: the task group description. Max length 100
      :parameter str task_group_type: the task group type: 'SP' for Service Provider and 'HF' for Health Care Facilities
      :parameter str hospital: the task group hospital. Max length 100
      :parameter array users: the list of users that belong to task group
      :parameter boolean is_health_care_provider: True if the task group is health care provider, False otherwise.
      :parameter boolean is_active: True if the user is active, False otherwise.
      :parameter array related_task_groups: the list of task groups that may benefit of health care services
      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the task group is successfully created. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the created task group data in json format


   .. http:method:: GET /users/task_group/search/

      Get a list of task group matching a query string in fields: title, description or hospital

      :parameter str query_string: the query string to search

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task groups matching the query string are found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the a list of
            data of task groups matching the query string, in json format


   .. http:method:: POST /users/task_group/(task_group_id)/edit/

      Edit the information of the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the task group is successfully found and updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the updated data of task group identified by `task_group_id`, in json format



   .. http:method:: GET /users/task_group/list_available_states/

      Get a list of available state of activation

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if states are successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains an array of available activation states in json format


   .. http:method:: POST /users/task_group/(task_group_id)/set_active_state/(active_state)/

      Set the activation state `active_state` (active or inactive) to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found and updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `id` (for the task group id) and `is_active` (for the activation state), in json format


   .. http:method:: GET /users/task_group/(task_group_id)/is_provider/

      Investigate if the task group identified by `task_group_id` is health care provider

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if the task group is successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `id` (for the task group id) and `is_health_care_provider` (for the health care provider state)


   .. http:method:: POST /users/task_group/(task_group_id)/set_provider/

      Set the task group identified by `task_group_id` as health care provider

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found and updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `id` (for the task group id) and `is_active` (for the activation state), in json format


   .. http:method:: POST /users/task_group/(task_group_id)/add_user/(user_id)/

      Add the user identified by `user_id` to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `user_id` (for the user just added to the task group), in json format


   .. http:method:: POST /users/task_group/(task_group_id)/remove_user/(user_id)/

      Remove the user identified by `user_id` from the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `user_id` (for the user just removed from the task group), in json format


   .. http:method:: GET /users/task_group/(task_group_id)/list_users/

      List all users that belong to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains an array of data of users that belong to the task group, in json format


   .. http:method:: POST /users/task_group/(task_group_id)/add_related_task_group/(related_task_group_id)/

      Add the related task group identified by `related_task_group_id` to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `related_task_group_id` (for the related task group just added to the task group), in json format


   .. http:method:: POST /users/task_group/(task_group_id)/remove_related_task_group/(related_task_group_id)/

      Remove the related task group identified by `related_task_group_id` from the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is updated. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `related_task_group_id` (for the related task group just removed from the task group), in json format


   .. http:method:: GET /users/task_group/(task_group_id)/list_related_task_groups/

      List all related task groups that belong to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains an array of data of related task groups that belong to the task group, in json format


   .. http:method:: GET /users/task_group/(task_group_id)/has_clinicians/

      Investigate if the task group identified by `task_group_id` has clinician users

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if clinician users are successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `clinicians_count` (for the number of clinician user that belong to task group)


   .. http:method:: GET /users/task_group/(task_group_id)/list_clinicians/

      List all related clinician users that belong to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains an array of data of clinician users that belong to the task group, in json format


   .. http:method:: GET /users/task_group/(task_group_id)/has_clinician_provider/

      Investigate if the task group identified by `task_group_id` has health care provider clinician users

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if health care providers are successfully found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains the keys `task_group_id` (for the task group id) and `clinicians_count` (for the number of health care provider clinician user that belong to task group)


   .. http:method:: GET /users/task_group/(task_group_id)/list_clinician_providers/

      List all health care provider clinician users that belong to the task group identified by `task_group_id`

      :requestheader Authorization: login required
      :responseheader Content-Type: application/json

         :parameter boolean `success`: True if task group is found. False otherwise
         :parameter str `message`: a feedback string that would be displayed to the connected user
         :parameter str `errors`: an error string that explains the raised problems
         :parameter json `data`: if success is True, it contains an array of data of health care provider clinician users that belong to the task group, in json format
