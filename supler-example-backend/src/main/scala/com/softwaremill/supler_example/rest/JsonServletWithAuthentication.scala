package com.softwaremill.supler_example.rest

import com.softwaremill.supler_example.auth.RememberMeSupport

abstract class JsonServletWithAuthentication extends JsonServlet with RememberMeSupport {

}