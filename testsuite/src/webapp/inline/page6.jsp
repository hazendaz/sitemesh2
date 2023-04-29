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
		<title>content 6</title>
	</head>
	<body>

		<p>page 6 content</p>

		<page:applyDecorator name="inline-panel" id="inline1" title="Inline external content 6">
			<%= "\u0126\u0118\u0139\u0139\u0150" %>
		</page:applyDecorator>

		<p >Bye</p>

	</body>
</html>
