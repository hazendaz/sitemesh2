<%--

    SPDX-License-Identifier: Apache-2.0
    Copyright 2011-2026 Hazendaz

--%>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>

<html>
	<head>
		<title>Inline 4</title>
	</head>
	<body>

		<p>Hello</p>

		<page:applyDecorator name="inline-panel" page="/default.jsp" />

		<p>Bye</p>

	</body>
</html>
