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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

/**
 * @author Joe Walnes
 * @since SiteMesh 3
 */
public class SiteMeshWebAppContext implements SiteMeshContext {

    private static final String IS_USING_STRING_KEY = "com.opensymphony.sitemesh.USINGSTREAM";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;

    private String contentType;

    public SiteMeshWebAppContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public boolean isUsingStream() {
        return request.getAttribute(IS_USING_STRING_KEY) == Boolean.TRUE;
    }

    public void setUsingStream(boolean isUsingStream) {
        request.setAttribute(IS_USING_STRING_KEY, isUsingStream ? Boolean.TRUE : Boolean.FALSE); // JDK 1.3 friendly
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
