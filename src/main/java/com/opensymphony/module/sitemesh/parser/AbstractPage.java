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
 * Title:        AbstractPage
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Abstract implementation of {@link com.opensymphony.module.sitemesh.Page} .
 * <p>
 * Contains base methods for storing and accessing page properties. *
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.Page
 */
public abstract class AbstractPage implements Page {
    /**
     * Map of all properties. Key is String. Value is java.util.List of multiple String values.
     */
    private final Map<String, String> properties = new HashMap<String, String>();

    /** Date of page contents. */
    private final SitemeshBuffer sitemeshBuffer;

    /** RequestURI of original Page. */
    private HttpServletRequest request;

    /**
     * Instantiates a new abstract page.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     */
    protected AbstractPage(SitemeshBuffer sitemeshBuffer) {
        this.sitemeshBuffer = sitemeshBuffer;
    }

    @Override
    public void writePage(Writer out) throws IOException {
        sitemeshBuffer.writeTo(out, 0, sitemeshBuffer.getBufferLength());
    }

    @Override
    public String getPage() {
        try {
            StringWriter writer = new StringWriter();
            writePage(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not get page " + e.getMessage(), e);
        }
    }

    /**
     * Write data of html <code>&lt;body&gt;</code> tag.
     * <p>
     * Must be implemented. Data written should not actually contain the body tags, but all the data in between.
     */
    @Override
    public abstract void writeBody(Writer out) throws IOException;

    @Override
    public String getBody() {
        try {
            StringWriter writer = new StringWriter();
            writeBody(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not get body " + e.getMessage(), e);
        }
    }

    /** Return title of from "title" property. Never returns null. */
    @Override
    public String getTitle() {
        return noNull(getProperty("title"));
    }

    @Override
    public String getProperty(String name) {
        if (!isPropertySet(name)) {
            return null;
        }
        return (String) properties.get(name);
    }

    @Override
    public int getIntProperty(String name) {
        try {
            return Integer.parseInt(noNull(getProperty(name)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public long getLongProperty(String name) {
        try {
            return Long.parseLong(noNull(getProperty(name)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean getBooleanProperty(String name) {
        String property = getProperty(name);
        if (property == null || property.trim().length() == 0) {
            return false;
        }
        switch (property.charAt(0)) {
            case '1':
            case 't':
            case 'T':
            case 'y':
            case 'Y':
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isPropertySet(String name) {
        return properties.containsKey(name);
    }

    @Override
    public String[] getPropertyKeys() {
        synchronized (properties) {
            Set<String> keys = properties.keySet();
            return (String[]) keys.toArray(new String[keys.size()]);
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    /** @see com.opensymphony.module.sitemesh.Page#getRequest() */
    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Create snapshot of Request.
     *
     * @see com.opensymphony.module.sitemesh.Page#getRequest()
     */
    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = new PageRequest(request);
    }

    /**
     * Add a property to the properties list.
     *
     * @param name
     *            Name of property
     * @param value
     *            Value of property
     */
    @Override
    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    /**
     * Return String as is, or "" if null. (Prevents NullPointerExceptions)
     *
     * @param in
     *            the in
     *
     * @return the string
     */
    protected static String noNull(String in) {
        return in == null ? "" : in;
    }

}

class PageRequest extends HttpServletRequestWrapper {

    private String requestURI, method, pathInfo, pathTranslated, queryString, servletPath;

    public PageRequest(HttpServletRequest request) {
        super(request);
        requestURI = request.getRequestURI();
        method = request.getMethod();
        pathInfo = request.getPathInfo();
        pathTranslated = request.getPathTranslated();
        queryString = request.getQueryString();
        servletPath = request.getServletPath();
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    @Override
    public String getPathTranslated() {
        return pathTranslated;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }
}
