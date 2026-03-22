/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
/* This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file. */
package com.opensymphony.module.sitemesh.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Special request dispatcher that will include when an inline decorator includes a resource that uses an internal
 * forward.
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 *
 * @see com.opensymphony.module.sitemesh.taglib.page.ApplyDecoratorTag
 */
public class RequestDispatcherWrapper implements RequestDispatcher {

    /** The rd. */
    private RequestDispatcher rd;

    /** The done. */
    private boolean done;

    /**
     * Instantiates a new request dispatcher wrapper.
     *
     * @param rd
     *            the rd
     */
    public RequestDispatcherWrapper(RequestDispatcher rd) {
        this.rd = rd;
    }

    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse)
            throws ServletException, IOException {
        if (done) {
            throw new IllegalStateException("Response has already been committed");
        }
        include(servletRequest, servletResponse);
        done = true;
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse)
            throws ServletException, IOException {
        if (done) {
            throw new IllegalStateException("Response has already been committed");
        }
        rd.include(servletRequest, servletResponse);
    }
}
