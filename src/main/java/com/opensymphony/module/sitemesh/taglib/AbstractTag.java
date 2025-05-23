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
 * Title:        AbstractTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.module.sitemesh.util.OutputConverter;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import jakarta.servlet.jsp.tagext.Tag;

import java.io.Writer;

/**
 * Convenience implementation of Tag containing generice methods required by all (or most) taglibs.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public abstract class AbstractTag extends BodyTagSupport implements RequestConstants {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The page context. */
    protected PageContext pageContext;

    /** The parent. */
    protected Tag parent;

    /** To be implemented by all empty tags. */
    @Override
    public abstract int doEndTag() throws JspException;

    /** Returns SKIP_BODY. */
    @Override
    public int doStartTag() {
        return SKIP_BODY;
    }

    @Override
    public void release() {
    }

    @Override
    public Tag getParent() {
        return parent;
    }

    @Override
    public void setParent(Tag parent) {
        this.parent = parent;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Return the Page object from the PAGE scope. If this is found in REQUEST scope instead, it will be moved into PAGE
     * scope - to handle multi-level includes.
     *
     * @return the page
     */
    protected Page getPage() {
        Page p = (Page) pageContext.getAttribute(PAGE, PageContext.PAGE_SCOPE);

        if (p == null) {
            p = (Page) pageContext.getAttribute(PAGE, PageContext.REQUEST_SCOPE);
            if (p == null) {
                pageContext.removeAttribute(PAGE, PageContext.PAGE_SCOPE);
            } else {
                pageContext.setAttribute(PAGE, p, PageContext.PAGE_SCOPE);
            }
            pageContext.removeAttribute(PAGE, PageContext.REQUEST_SCOPE);
        }
        return p;
    }

    /**
     * Log exception generated by taglib.
     *
     * @param e
     *            the e
     */
    protected static void trace(Exception e) {
        e.printStackTrace();
    }

    /**
     * Get the outputWriter. This method should be used in preference to <code>pageContext.getOut()</code>, as some
     * charset conversions may need to happen in some servers.
     *
     * @return the writer for use in the tag
     */
    protected Writer getOut() {
        return OutputConverter.getWriter(pageContext.getOut());
    }
}
