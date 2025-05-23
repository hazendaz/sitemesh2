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
/* This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file. */
package com.opensymphony.module.sitemesh.filter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Will wrap a request for the {@link RequestDispatcherWrapper}.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 */
public class PageRequestWrapper extends HttpServletRequestWrapper {

    // TODO - pull this from a config file
    /** The Constant SUPPRESS_IF_MODIFIED_HEADER. */
    private static final boolean SUPPRESS_IF_MODIFIED_HEADER = true;

    /**
     * Instantiates a new page request wrapper.
     *
     * @param request
     *            the request
     */
    public PageRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return new RequestDispatcherWrapper(super.getRequestDispatcher(s));
    }

    @Override
    public String getHeader(String string) {
        // suppress 'if-modified-since' header, so that decorators can modify the response.
        if (SUPPRESS_IF_MODIFIED_HEADER && "IF-MODIFIED-SINCE".equalsIgnoreCase(string)) {
            return "";
        }
        return super.getHeader(string);
    }
}
