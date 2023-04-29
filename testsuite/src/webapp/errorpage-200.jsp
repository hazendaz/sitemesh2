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
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %><% response.setStatus(200); // as HttpWebUnit dies when you send a 500 error, then set to 200 just for testing!  %>
  <head><title>An error has occurred</title></head>
  <body><%= exception %></body>
</html>