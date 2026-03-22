/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.PageParserSelector;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The Class MultipassFilter.
 */
public class MultipassFilter extends SiteMeshFilter {

    /**
     * Write decorator.
     *
     * @param response
     *            the response
     * @param page
     *            the page
     * @param dispatcher
     *            the dispatcher
     * @param request
     *            the request
     *
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void writeDecorator(final HttpServletResponse response, final Page page, RequestDispatcher dispatcher,
            HttpServletRequest request) throws ServletException, IOException {
        PageResponseWrapper pageResponse = new PageResponseWrapper(response, new PageParserSelector() {
            @Override
            public boolean shouldParsePage(String contentType) {
                return true;
            }

            @Override
            public PageParser getPageParser(String contentType) {
                return new MultipassReplacementPageParser(page, response);
            }
        });
        pageResponse.activateSiteMesh("text/html", "");
        dispatcher.include(request, pageResponse);
        pageResponse.getPage();
    }

}
