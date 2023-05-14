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
package com.opensymphony.sitemesh.webapp.decorator;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

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

        if (webAppContext.isUsingStream()) {
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            content.writeOriginal(writer);
            writer.flush(); // flush writer to underlying outputStream
            response.getOutputStream().flush();
        } else {
            PrintWriter writer = response.getWriter();
            content.writeOriginal(writer);
            response.getWriter().flush();
        }
    }

}
