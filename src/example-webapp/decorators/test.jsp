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
<html>
	<head>
		<title><decorator:title default="Mysterious page..." /></title>
		<decorator:head />
	</head>
	<body>

		<decorator:body />

		<p>(Default version)</p>

	</body>
</html>
