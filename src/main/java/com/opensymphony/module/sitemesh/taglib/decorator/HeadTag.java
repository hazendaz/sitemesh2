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
 * Title:        HeadTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.taglib.AbstractTag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * Write original HTMLPage head to out.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.HTMLPage#writeHead(java.io.Writer)
 */
public class HeadTag extends AbstractTag {
    public final int doEndTag() throws JspException {
        HTMLPage htmlPage = (HTMLPage) getPage();
        try {
            htmlPage.writeHead(getOut());
        } catch (IOException e) {
            throw new JspException("Error writing head element: " + e.toString(), e);
        }
        return EVAL_PAGE;
    }
}
