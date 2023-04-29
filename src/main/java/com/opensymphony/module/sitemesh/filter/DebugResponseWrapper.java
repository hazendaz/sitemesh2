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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * The Class DebugResponseWrapper.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 */
public class DebugResponseWrapper extends HttpServletResponseWrapper {

    /** The last count. */
    private static int lastCount = 0;

    /** The count. */
    private int count = 0;

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

    public void addCookie(Cookie cookie) {
        if (enabled())
            debug("addCookie", cookie.getName(), cookie.toString());
        super.addCookie(cookie);
    }

    public void addDateHeader(String name, long date) {
        if (enabled())
            debug("addDateHeader", name, String.valueOf(date));
        super.addDateHeader(name, date);
    }

    public void addHeader(String name, String value) {
        if (enabled())
            debug("addHeader", name, value);
        super.addHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        if (enabled())
            debug("addIntHeader", name, String.valueOf(value));
        super.addIntHeader(name, value);
    }

    public boolean containsHeader(String name) {
        return super.containsHeader(name);
    }

    public String encodeRedirectUrl(String url) {
        return super.encodeRedirectUrl(url);
    }

    public String encodeRedirectURL(String url) {
        return super.encodeRedirectURL(url);
    }

    public void sendError(int sc) throws IOException {
        if (enabled())
            debug("sendError", String.valueOf(sc), null);
        super.sendError(sc);
    }

    public void sendError(int sc, String msg) throws IOException {
        if (enabled())
            debug("sendError", String.valueOf(sc), msg);
        super.sendError(sc, msg);
    }

    public void sendRedirect(String location) throws IOException {
        if (enabled())
            debug("sendRedirect", location, null);
        super.sendRedirect(location);
    }

    public void setDateHeader(String name, long date) {
        if (enabled())
            debug("setDateHeader", name, String.valueOf(date));
        super.setDateHeader(name, date);
    }

    public void setHeader(String name, String value) {
        if (enabled())
            debug("setHeader", name, value);
        super.setHeader(name, value);
    }

    public void setIntHeader(String name, int value) {
        if (enabled())
            debug("setIntHeader", name, String.valueOf(value));
        super.setIntHeader(name, value);
    }

    public void setStatus(int sc) {
        if (enabled())
            debug("setStatus", String.valueOf(sc), null);
        super.setStatus(sc);
    }

    public void setStatus(int sc, String msg) {
        if (enabled())
            debug("setStatus", String.valueOf(sc), msg);
        super.setStatus(sc, msg);
    }

    public void flushBuffer() throws IOException {
        if (enabled())
            debug("flushBuffer", null, null);
        super.flushBuffer();
    }

    public int getBufferSize() {
        //
        return super.getBufferSize();
    }

    public String getCharacterEncoding() {
        //
        return super.getCharacterEncoding();
    }

    public Locale getLocale() {
        //
        return super.getLocale();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (enabled())
            debug("getOutputStream", null, null);
        return super.getOutputStream();
    }

    public PrintWriter getWriter() throws IOException {
        if (enabled())
            debug("getWriter", null, null);
        return super.getWriter();
    }

    public boolean isCommitted() {
        //
        return super.isCommitted();
    }

    public void reset() {
        if (enabled())
            debug("reset", null, null);
        super.reset();
    }

    /*
     * public void resetBuffer() { super.resetBuffer(); }
     */

    public void setBufferSize(int size) {
        if (enabled())
            debug("setBufferSize", String.valueOf(size), null);
        super.setBufferSize(size);
    }

    public void setContentLength(int len) {
        if (enabled())
            debug("setContentLength", String.valueOf(len), null);
        super.setContentLength(len);
    }

    public void setContentType(String type) {
        if (enabled())
            debug("setContentType", type, null);
        super.setContentType(type);
    }

    public void setLocale(Locale locale) {
        if (enabled())
            debug("setBufferSize", locale.getDisplayName(), null);
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
