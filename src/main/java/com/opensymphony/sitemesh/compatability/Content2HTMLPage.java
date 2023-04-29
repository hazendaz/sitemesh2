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
package com.opensymphony.sitemesh.compatability;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.sitemesh.Content;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Adapts a SiteMesh 3 {@link Content} to a SiteMesh 2 {@link HTMLPage}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class Content2HTMLPage implements HTMLPage {

    /** The content. */
    private final Content content;

    /** The request. */
    private HttpServletRequest request;

    /**
     * Instantiates a new content 2 HTML page.
     *
     * @param content
     *            the content
     * @param request
     *            the request
     */
    public Content2HTMLPage(Content content, HttpServletRequest request) {
        this.content = content;
        this.request = request;
    }

    @Override
    public void writePage(Writer out) throws IOException {
        content.writeOriginal(out);
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

    @Override
    public void writeBody(Writer out) throws IOException {
        content.writeBody(out);
    }

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

    @Override
    public void writeHead(Writer out) throws IOException {
        content.writeHead(out);
    }

    @Override
    public String getHead() {
        try {
            StringWriter writer = new StringWriter();
            writeHead(writer);
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not get head " + e.getMessage(), e);
        }
    }

    @Override
    public String getTitle() {
        return content.getTitle();
    }

    @Override
    public String getProperty(String name) {
        return content.getProperty(name);
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

    /**
     * No null.
     *
     * @param property
     *            the property
     *
     * @return the string
     */
    private String noNull(String property) {
        return property == null ? "" : property;
    }

    @Override
    public boolean getBooleanProperty(String name) {
        String property = getProperty(name);
        if (property == null || property.trim().length() == 0)
            return false;
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
        return getProperty(name) != null;
    }

    @Override
    public String[] getPropertyKeys() {
        return content.getPropertyKeys();
    }

    @Override
    public Map getProperties() {
        Map result = new HashMap();
        String[] keys = content.getPropertyKeys();
        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], content.getProperty(keys[i]));
        }
        return result;
    }

    @Override
    public boolean isFrameSet() {
        return isPropertySet("frameset") && getProperty("frameset").equalsIgnoreCase("true");
    }

    @Override
    public void setFrameSet(boolean frameset) {
        addProperty("frameset", frameset ? "true" : "false");
    }

    /**
     * @see com.opensymphony.module.sitemesh.Page#getRequest()
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Create snapshot of Request.
     *
     * @see com.opensymphony.module.sitemesh.Page#getRequest()
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void addProperty(String name, String value) {
        content.addProperty(name, value);
    }

}
