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

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * The Class DebugResponseWrapper.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 */
public class DebugResponseWrapper extends HttpServletResponseWrapper {

    /** The last count. */
    private static int lastCount;

    /** The count. */
    private int count;

    /**
     * Instantiates a new debug response wrapper.
     *
     * @param response
     *            the response
     */
    public DebugResponseWrapper(HttpServletResponse response) {
        super(response);
        if (enabled()) {
            lastCount++;
            count = lastCount;
            debug("<CONSTRUCT>", null, null);
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (enabled()) {
            debug("addCookie", cookie.getName(), cookie.toString());
        }
        super.addCookie(cookie);
    }

    @Override
    public void addDateHeader(String name, long date) {
        if (enabled()) {
            debug("addDateHeader", name, String.valueOf(date));
        }
        super.addDateHeader(name, date);
    }

    @Override
    public void addHeader(String name, String value) {
        if (enabled()) {
            debug("addHeader", name, value);
        }
        super.addHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        if (enabled()) {
            debug("addIntHeader", name, String.valueOf(value));
        }
        super.addIntHeader(name, value);
    }

    @Override
    public boolean containsHeader(String name) {
        return super.containsHeader(name);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return super.encodeRedirectURL(url);
    }

    @Override
    public void sendError(int sc) throws IOException {
        if (enabled()) {
            debug("sendError", String.valueOf(sc), null);
        }
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        if (enabled()) {
            debug("sendError", String.valueOf(sc), msg);
        }
        super.sendError(sc, msg);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        if (enabled()) {
            debug("sendRedirect", location, null);
        }
        super.sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        if (enabled()) {
            debug("setDateHeader", name, String.valueOf(date));
        }
        super.setDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        if (enabled()) {
            debug("setHeader", name, value);
        }
        super.setHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (enabled()) {
            debug("setIntHeader", name, String.valueOf(value));
        }
        super.setIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        if (enabled()) {
            debug("setStatus", String.valueOf(sc), null);
        }
        super.setStatus(sc);
    }

    @Override
    public void flushBuffer() throws IOException {
        if (enabled()) {
            debug("flushBuffer", null, null);
        }
        super.flushBuffer();
    }

    @Override
    public int getBufferSize() {
        //
        return super.getBufferSize();
    }

    @Override
    public String getCharacterEncoding() {
        //
        return super.getCharacterEncoding();
    }

    @Override
    public Locale getLocale() {
        //
        return super.getLocale();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (enabled()) {
            debug("getOutputStream", null, null);
        }
        return super.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (enabled()) {
            debug("getWriter", null, null);
        }
        return super.getWriter();
    }

    @Override
    public boolean isCommitted() {
        //
        return super.isCommitted();
    }

    @Override
    public void reset() {
        if (enabled()) {
            debug("reset", null, null);
        }
        super.reset();
    }

    /*
     * public void resetBuffer() { super.resetBuffer(); }
     */

    @Override
    public void setBufferSize(int size) {
        if (enabled()) {
            debug("setBufferSize", String.valueOf(size), null);
        }
        super.setBufferSize(size);
    }

    @Override
    public void setContentLength(int len) {
        if (enabled()) {
            debug("setContentLength", String.valueOf(len), null);
        }
        super.setContentLength(len);
    }

    @Override
    public void setContentType(String type) {
        if (enabled()) {
            debug("setContentType", type, null);
        }
        super.setContentType(type);
    }

    @Override
    public void setLocale(Locale locale) {
        if (enabled()) {
            debug("setBufferSize", locale.getDisplayName(), null);
        }
        super.setLocale(locale);
    }

    /**
     * Enabled.
     *
     * @return true, if successful
     */
    private boolean enabled() {
        return true;
    }

    /**
     * Debug.
     *
     * @param methodName
     *            the method name
     * @param arg1
     *            the arg 1
     * @param arg2
     *            the arg 2
     */
    private void debug(String methodName, String arg1, String arg2) {
        StringBuilder s = new StringBuilder();
        s.append("[debug ");
        s.append(count);
        s.append("] ");
        s.append(methodName);
        s.append("()");
        if (arg1 != null) {
            s.append(" : '");
            s.append(arg1);
            s.append("'");
        }
        if (arg2 != null) {
            s.append(" = '");
            s.append(arg2);
            s.append("'");
        }
        System.out.println(s);
    }
}
