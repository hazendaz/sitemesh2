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
		<title><decorator:title default="Mysterious page..." /></title>
		<link href="<%= request.getContextPath() %>/decorators/main.css" rel="stylesheet" type="text/css">
		<decorator:head />
	</head>

	<body>
		<table width="100%" height="100%">
			<tr>
				<td valign="top">
					<page:applyDecorator page="/tiny-panel.html" name="panel" />
					<page:applyDecorator page="/counter.jsp" name="panel" />
					<page:applyDecorator page="/google.html" name="panel" />
					<%--page:applyDecorator page="/random.pl" name="panel" /--%>
				</td>
				<td width="100%">
					<table width="100%" height="100%">
						<tr>
							<td id="pageTitle">
								<decorator:title />
							</td>
						</tr>
						<tr>
							<td valign="top" height="100%">
								<decorator:body />
							</td>
						</tr>
						<tr>
							<td id="footer">
								<b>Disclaimer:</b>
								This site is an example site to demonstrate SiteMesh. It serves no other purpose.
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<%-- Construct URL from page request and append 'printable=true' param --%>
		<decorator:usePage id="p" />
		<%
			StringBuilder printUrl = new StringBuilder();
			printUrl.append(request.getRequestURI());
			printUrl.append("?printable=true");
			if (request.getQueryString()!=null) {
				printUrl.append('&');
				printUrl.append(request.getQueryString());
			}
		%>
		<p align="right">[ <a href="<%= printUrl %>">printable version</a> ]</p>

	</body>
</html>
