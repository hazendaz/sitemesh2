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
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.PageParserSelector;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
