<%--

    SPDX-License-Identifier: Apache-2.0
    Copyright 2011-2026 Hazendaz

--%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<html>
	<head>
		<title><decorator:title default="Mysterious page..." /></title>
		<decorator:head />
	</head>
	<body>

		<decorator:body />

		<p>(IE version)</p>

	</body>
</html>
