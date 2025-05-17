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

import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.filter.HttpContentType;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Adapts a SiteMesh 2 {@link PageParser} to a SiteMesh 3 {@link ContentProcessor}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class PageParser2ContentProcessor implements ContentProcessor {

    /** The factory. */
    private final Factory factory;

    /**
     * Instantiates a new page parser 2 content processor.
     *
     * @param factory
     *            the factory
     */
    public PageParser2ContentProcessor(Factory factory) {
        this.factory = factory;
    }

    /**
     * Handles.
     *
     * @param context
     *            the context
     *
     * @return true, if successful
     */
    @Override
    public boolean handles(SiteMeshContext context) {
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        return !factory.isPathExcluded(extractRequestPath(webAppContext.getRequest()));
    }

    /**
     * Extract request path.
     *
     * @param request
     *            the request
     *
     * @return the string
     */
    private String extractRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();
        return (servletPath == null ? "" : servletPath) + (pathInfo == null ? "" : pathInfo)
                + (query == null ? "" : "?" + query);
    }

    @Override
    public boolean handles(String contentType) {
        return factory.shouldParsePage(contentType);
    }

    /**
     * Builds the.
     *
     * @param buffer
     *            the buffer
     * @param context
     *            the context
     *
     * @return the content
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Override
    public Content build(SitemeshBuffer buffer, SiteMeshContext context) throws IOException {
        HttpContentType httpContentType = new HttpContentType(context.getContentType());
        PageParser pageParser = factory.getPageParser(httpContentType.getType());
        Page page = pageParser.parse(buffer);
        return new HTMLPage2Content((HTMLPage) page);
    }

}
