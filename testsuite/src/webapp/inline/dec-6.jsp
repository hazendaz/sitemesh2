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

<html>
	<head>
		<title>{inline6} <decorator:title /></title>
		<decorator:head />
	</head>
	<body>

    <h1><decorator:title /></h1>

    <table border><tr><td id="bod"><decorator:body /></td></tr></table>

    <h2 id="footer">footer</h2>

	</body>
</html>