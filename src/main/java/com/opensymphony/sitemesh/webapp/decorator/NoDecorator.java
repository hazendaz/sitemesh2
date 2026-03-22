/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh.webapp.decorator;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * If no decorator is to be applied to a page, this will ensure the original content gets written out.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3.0
 */
public class NoDecorator extends BaseWebAppDecorator {

    /**
     * Render.
     *
     * @param content
     *            the content
     * @param request
     *            the request
     * @param response
     *            the response
     * @param servletContext
     *            the servlet context
     * @param webAppContext
     *            the web app context
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ServletException
     *             the servlet exception
     */
    @Override
    protected void render(Content content, HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext, SiteMeshWebAppContext webAppContext) throws IOException, ServletException {

        PrintWriter writer;
        if (webAppContext.isUsingStream()) {
            writer = new PrintWriter(response.getOutputStream());
        } else {
            writer = response.getWriter();
        }
        content.writeOriginal(writer);
        writer.flush();
    }

}
