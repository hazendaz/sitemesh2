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
        <title><decorator:title/></title>
    </head>
    <body>
        <!-- Decorator -->
        {<decorator:extractProperty property="div.c"/>}
        <hr/>
        <decorator:body/>
        <hr/>
        {<decorator:extractProperty property="div.a"/>}
        {<decorator:getProperty property="div.b"/>}
	</body>
</html>
