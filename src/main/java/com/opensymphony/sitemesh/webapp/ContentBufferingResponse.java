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
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class ContentBufferingResponse extends HttpServletResponseWrapper {

    // TODO: Temporary SM3 migration implementation! Wraps SM2 PageResponseWrapper. This class is an evil stopgap.
    // Eventually PageResponseWrapper will go away and the functionality will be rolled into this class.

    private final PageResponseWrapper pageResponseWrapper;
    private final ContentProcessor contentProcessor;
    private final SiteMeshWebAppContext webAppContext;

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

    public boolean isUsingStream() {
        return pageResponseWrapper.isUsingStream();
    }

    public Content getContent() throws IOException {
        SitemeshBuffer content = pageResponseWrapper.getContents();
        if (content != null) {
            return contentProcessor.build(content, webAppContext);
        } else {
            return null;
        }
    }
}