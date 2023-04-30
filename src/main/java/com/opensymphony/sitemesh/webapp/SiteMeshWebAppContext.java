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
package com.opensymphony.sitemesh.webapp;

import com.opensymphony.sitemesh.SiteMeshContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class SiteMeshWebAppContext.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class SiteMeshWebAppContext implements SiteMeshContext {

    /** The Constant IS_USING_STRING_KEY. */
    private static final String IS_USING_STRING_KEY = "com.opensymphony.sitemesh.USINGSTREAM";

    /** The request. */
    private final HttpServletRequest request;

    /** The response. */
    private final HttpServletResponse response;

    /** The servlet context. */
    private final ServletContext servletContext;

    /** The content type. */
    private String contentType;

    /**
     * Instantiates a new site mesh web app context.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param servletContext
     *            the servlet context
     */
    public SiteMeshWebAppContext(HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }

    /**
     * Gets the request.
     *
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Gets the servlet context.
     *
     * @return the servlet context
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Checks if is using stream.
     *
     * @return true, if is using stream
     */
    public boolean isUsingStream() {
        return request.getAttribute(IS_USING_STRING_KEY) == Boolean.TRUE;
    }

    /**
     * Sets the using stream.
     *
     * @param isUsingStream
     *            the new using stream
     */
    public void setUsingStream(boolean isUsingStream) {
        request.setAttribute(IS_USING_STRING_KEY, isUsingStream ? Boolean.TRUE : Boolean.FALSE); // JDK 1.3 friendly
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
