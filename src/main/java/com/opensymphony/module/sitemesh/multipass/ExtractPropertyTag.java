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
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.taglib.decorator.PropertyTag;
import com.opensymphony.module.sitemesh.Page;

public class ExtractPropertyTag extends PropertyTag {

    public int doEndTag() {
        Page page = getPage();
        page.addProperty("_sitemesh.removefrompage." + getProperty(), "true");
        return super.doEndTag();
    }

}
