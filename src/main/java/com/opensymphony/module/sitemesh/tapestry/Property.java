/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2023 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     Hazendaz (Jeremy Landis).
 */
package com.opensymphony.module.sitemesh.tapestry;

import org.apache.tapestry.Tapestry;

/**
 * Because Tapestry templating works differently than JSP taglibs, the writeEntireProperty feature is not implemented
 * here. The built-in
 *
 * @Body component is most frequently used, to do something like this taglib example:
 *       <p/>
 *       <body bgcolor="White"<decorator:getProperty property="body.onload" writeEntireProperty="true" />>
 *       <p/>
 *       it would be done like this in Tapestry:
 *       <p/>
 *       <body jwcid="@Body" bgcolor="White" onload=
 *       "ognl:@org.opensymphony.module.sitemesh.tapestry@Util.getProperty('onload', requestCycle)"/>
 *
 * @author Erik Hatcher
 */
public abstract class Property extends SiteMeshBase {

    public abstract String getProperty();

    public abstract String getDefault();

    public String getValue() {
        String propertyName = getProperty();
        String propertyValue = getSiteMeshPage().getProperty(propertyName);

        if (Tapestry.isBlank(propertyValue)) {
            propertyValue = getDefault();
        }

        return propertyValue;
    }
}
