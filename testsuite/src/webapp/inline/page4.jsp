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