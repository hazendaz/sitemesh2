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

<table width="200" border="1" cellspacing="0">
    <tr>
        <td bgcolor="darkblue" align="center">
            <font color="white" face="arial"><b>
                <span id="<decorator:getProperty property="id" default="unknown" />"><decorator:title /></span>
            </b></font>
        </td>
    </tr>
    <tr>
        <td bgcolor="lightgrey">
            <font size="1" face="arial" id="inline-content">
                <decorator:body />
            </font>
        </td>
    </tr>
</table>
<br>