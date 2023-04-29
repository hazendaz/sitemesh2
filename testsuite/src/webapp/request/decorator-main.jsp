<%--

    sitemesh2 (https://github.com/hazendaz/sitemesh2)

    Copyright 2011-2023 Hazendaz.

    All rights reserved. This program and the accompanying materials
    are made available under the terms of The Apache Software License,
    Version 2.0 which accompanies this distribution, and is available at
    https://www.apache.org/licenses/LICENSE-2.0.txt

    Contributors:
        Hazendaz (Jeremy Landis).

--%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ include file="function.jsp"%>

<decorator:usePage id="p" />

decorator-main.jsp, request: <%= dumpRequest(request) %>

decorator-main.jsp, page.request before applyDecorator: <%= dumpRequest(p.getRequest()) %>

<decorator:body />
<page:applyDecorator page="/request/inline.jsp" name="/request/decorator-inline.jsp" />

decorator-main.jsp, page.request after applyDecorator: <%= dumpRequest(p.getRequest()) %>
