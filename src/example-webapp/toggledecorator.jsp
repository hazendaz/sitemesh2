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
<%! boolean toggle; %>
<html>
	<head>
		<title>Toggled Decorator</title>
    <meta name="decorator" content="<%= toggle ? "black" : "main" %>">
    <% toggle = !toggle; %>
	</head>
	<body>

		<p>This page is an example of how a Decorator can be chosen by the actual page itself.</p>

		<p>Within the HTML is a <code>&lt;meta&gt;</code> tag that states which Decorator to use.
		A bit of JSP alternates the value of this tag on each request.</p>

		<p>The top of the page looks like this:</p>

		<code><pre>
&lt;%! boolean toggle; %&gt;
&lt;html&gt;
  &lt;head&gt;
    &lt;title&gt;Toggled Decorator&lt;/title&gt;
    &lt;meta name="decorator" content="&lt;%= toggle ? "black" : "main" %&gt;"&gt;
    &lt;% toggle = !toggle; %&gt;
  &lt;/head&gt;
  ...
		</pre></code>

		<h3>Refresh this page! (and again and again)</h3>

	</body>
</html>
