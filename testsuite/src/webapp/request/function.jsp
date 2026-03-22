<%--

    SPDX-License-Identifier: Apache-2.0
    Copyright 2011-2026 Hazendaz

--%>
<%!
  String dumpRequest(HttpServletRequest req) {
    return req.getRequestURI() + "|" + req.getQueryString();
  }
%>
