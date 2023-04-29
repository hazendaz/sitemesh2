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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Will wrap a request for the {@link RequestDispatcherWrapper}.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 *
 * @version $Revision: 1.4 $
 */
public class PageRequestWrapper extends HttpServletRequestWrapper {
    private static final boolean SUPPRESS_IF_MODIFIED_HEADER = true; // todo - pull this from a config file

    public PageRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return new RequestDispatcherWrapper(super.getRequestDispatcher(s));
    }

    public String getHeader(String string) {
        // suppress 'if-modified-since' header, so that decorators can modify the response.
        if (SUPPRESS_IF_MODIFIED_HEADER && "IF-MODIFIED-SINCE".equalsIgnoreCase(string)) {
            return "";
        } else {
            return super.getHeader(string);
        }
    }
}
