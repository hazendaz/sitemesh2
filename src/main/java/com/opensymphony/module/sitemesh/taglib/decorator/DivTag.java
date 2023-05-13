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
package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.taglib.AbstractTag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * Write a HTMLPage div to out.
 *
 * @see HTMLPage#writeHead(java.io.Writer)
 */
public class DivTag extends AbstractTag {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The div id. */
    protected String divId;

    @Override
    public String getId() {
        return divId;
    }

    @Override
    public void setId(String divId) {
        this.divId = divId;
    }

    public final int doEndTag() throws JspException {
        try {
            String divBody = getPage().getProperty("div." + divId);
            if (divBody != null) {
                getOut().write(divBody.substring(divBody.indexOf('>') + 1, divBody.lastIndexOf('<')));
            }
        } catch (IOException e) {
            throw new JspException("Error writing head element: " + e.toString(), e);
        }
        return EVAL_PAGE;
    }
}
