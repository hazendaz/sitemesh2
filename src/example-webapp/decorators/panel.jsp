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

<p>
	<table width=250 border=0 cellpadding=0 cellspacing=0>
		<tr>
			<th class="panelTitle">
				<decorator:title default="Unknown panel" />
			</th>
		</tr>
		<tr>
			<td class="panelBody">
				<decorator:body />
			</td>
		</tr>
	</table>
</p>

