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

import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.PageParserSelector;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.ContentProcessor;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * The Class ContentBufferingResponse.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class ContentBufferingResponse extends HttpServletResponseWrapper {

    // TODO: Temporary SM3 migration implementation! Wraps SM2 PageResponseWrapper. This class is an evil stopgap.
    // Eventually PageResponseWrapper will go away and the functionality will be rolled into this class.

    /** The page response wrapper. */
    private final PageResponseWrapper pageResponseWrapper;

    /** The content processor. */
    private final ContentProcessor contentProcessor;

    /** The web app context. */
    private final SiteMeshWebAppContext webAppContext;

    /**
     * Instantiates a new content buffering response.
     *
     * @param response
     *            the response
     * @param contentProcessor
     *            the content processor
     * @param webAppContext
     *            the web app context
     */
    public ContentBufferingResponse(HttpServletResponse response, final ContentProcessor contentProcessor,
            final SiteMeshWebAppContext webAppContext) {
        super(new PageResponseWrapper(response, new PageParserSelector() {
            public boolean shouldParsePage(String contentType) {
                return contentProcessor.handles(contentType);
            }

            public PageParser getPageParser(String contentType) {
                // Migration: Not actually needed by PageResponseWrapper, so long as getPage() isn't called.
                return null;
            }
        }) {
            public void setContentType(String contentType) {
                webAppContext.setContentType(contentType);
                super.setContentType(contentType);
            }
        });
        this.contentProcessor = contentProcessor;
        this.webAppContext = webAppContext;
        pageResponseWrapper = (PageResponseWrapper) getResponse();
    }

    /**
     * Checks if is using stream.
     *
     * @return true, if is using stream
     */
    public boolean isUsingStream() {
        return pageResponseWrapper.isUsingStream();
    }

    /**
     * Gets the content.
     *
     * @return the content
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Content getContent() throws IOException {
        SitemeshBuffer content = pageResponseWrapper.getContents();
        if (content != null) {
            return contentProcessor.build(content, webAppContext);
        }
        return null;
    }

}
