/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.taglib.AbstractTag;

import jakarta.servlet.jsp.JspException;

import java.io.IOException;

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

    @Override
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
