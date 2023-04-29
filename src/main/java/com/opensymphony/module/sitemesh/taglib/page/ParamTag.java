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
/*
 * Title:        ParamTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.page;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * Add a parameter to the inline Decorator, as if specified in the Page.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @version $Revision: 1.2 $
 */
public class ParamTag extends BodyTagSupport {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public int doAfterBody() {
        Tag parent = findAncestorWithClass(this, ApplyDecoratorTag.class);
        if (parent instanceof ApplyDecoratorTag) {
            ApplyDecoratorTag t = (ApplyDecoratorTag)parent;
            t.addParam(name, getBodyContent().getString());
        }
        return SKIP_BODY;
    }
}