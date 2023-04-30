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

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParserSelector;
import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Implementation of HttpServletResponseWrapper that captures page data instead of sending to the writer.
 * <p>
 * Should be used in filter-chains or when forwarding/including pages using a RequestDispatcher.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
 */
public class PageResponseWrapper extends HttpServletResponseWrapper {

    /** The routable print writer. */
    private final RoutablePrintWriter routablePrintWriter;

    /** The routable servlet output stream. */
    private final RoutableServletOutputStream routableServletOutputStream;

    /** The parser selector. */
    private final PageParserSelector parserSelector;

    /** The buffer. */
    private Buffer buffer;

    /** The aborted. */
    private boolean aborted;

    /** The parseable page. */
    private boolean parseablePage;

    /**
     * Instantiates a new page response wrapper.
     *
     * @param response
     *            the response
     * @param parserSelector
     *            the parser selector
     */
    public PageResponseWrapper(final HttpServletResponse response, PageParserSelector parserSelector) {
        super(response);
        this.parserSelector = parserSelector;

        routablePrintWriter = new RoutablePrintWriter(response::getWriter);
        routableServletOutputStream = new RoutableServletOutputStream(response::getOutputStream);

    }

    /**
     * Set the content-type of the request and store it so it can be passed to the
     * {@link com.opensymphony.module.sitemesh.PageParser}.
     */
    @Override
    public void setContentType(String type) {
        super.setContentType(type);

        if (type != null) {
            HttpContentType httpContentType = new HttpContentType(type);

            if (parserSelector.shouldParsePage(httpContentType.getType())) {
                activateSiteMesh(httpContentType.getType(), httpContentType.getEncoding());
            } else {
                deactivateSiteMesh();
            }
        }

    }

    /**
     * Activate site mesh.
     *
     * @param contentType
     *            the content type
     * @param encoding
     *            the encoding
     */
    public void activateSiteMesh(String contentType, String encoding) {
        if (parseablePage) {
            return; // already activated
        }
        buffer = new Buffer(parserSelector.getPageParser(contentType), encoding);
        routablePrintWriter.updateDestination(buffer::getWriter);
        routableServletOutputStream.updateDestination(buffer::getOutputStream);
        parseablePage = true;
    }

    /**
     * Deactivate site mesh.
     */
    private void deactivateSiteMesh() {
        parseablePage = false;
        buffer = null;
        routablePrintWriter.updateDestination(() -> getResponse().getWriter());
        routableServletOutputStream.updateDestination(() -> getResponse().getOutputStream());
    }

    /**
     * Prevent content-length being set if page is parseable.
     */
    @Override
    public void setContentLength(int contentLength) {
        if (!parseablePage) {
            super.setContentLength(contentLength);
        }
    }

    /**
     * Prevent buffer from being flushed if this is a page being parsed.
     */
    @Override
    public void flushBuffer() throws IOException {
        if (!parseablePage) {
            super.flushBuffer();
        }
    }

    /**
     * Prevent content-length being set if page is parseable.
     */
    @Override
    public void setHeader(String name, String value) {
        if (name.equalsIgnoreCase("content-type")) { // ensure ContentType is always set through setContentType()
            setContentType(value);
        } else if (!parseablePage || !name.equalsIgnoreCase("content-length")) {
            super.setHeader(name, value);
        }
    }

    /**
     * Prevent content-length being set if page is parseable.
     */
    @Override
    public void addHeader(String name, String value) {
        if (name.equalsIgnoreCase("content-type")) { // ensure ContentType is always set through setContentType()
            setContentType(value);
        } else if (!parseablePage || !name.equalsIgnoreCase("content-length")) {
            super.addHeader(name, value);
        }
    }

    /**
     * If 'not modified' (304) HTTP status is being sent - then abort parsing, as there shouldn't be any body
     */
    @Override
    public void setStatus(int sc) {
        if (sc == HttpServletResponse.SC_NOT_MODIFIED) {
            aborted = true;
            // route any content back to the original writer. There shouldn't be any content, but just to be safe
            deactivateSiteMesh();
        }
        super.setStatus(sc);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return routableServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return routablePrintWriter;
    }

    /**
     * Gets the page.
     *
     * @return the page
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Page getPage() throws IOException {
        if (aborted || !parseablePage) {
            return null;
        }
        return buffer.parse();
    }

    @Override
    public void sendError(int sc) throws IOException {
        aborted = true;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        aborted = true;
        super.sendError(sc, msg);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        aborted = true;
        super.sendRedirect(location);
    }

    /**
     * Checks if is using stream.
     *
     * @return true, if is using stream
     */
    public boolean isUsingStream() {
        return buffer != null && buffer.isUsingStream();
    }

    /**
     * Gets the contents.
     *
     * @return the contents
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public SitemeshBuffer getContents() throws IOException {
        if (aborted || !parseablePage) {
            return null;
        }
        return buffer.getContents();
    }

}
