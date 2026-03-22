<%--

    SPDX-License-Identifier: Apache-2.0
    Copyright 2011-2026 Hazendaz

--%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ include file="function.jsp"%>
<decorator:usePage id="p" />

decorator-panel.jsp, request: <%= dumpRequest(request) %>

decorator-panel.jsp, page.request: <%= dumpRequest(p.getRequest()) %>

<decorator:body />
