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

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Decorator that dispatches to another path in the Servlet Container (such as a JSP or path mapped to a Servlet).
 * <p>
 * The Content and SiteMeshContext objects are passed to the decorator using the HttpServletRequest attributes
 * {@link #CONTENT_KEY} and {@link #CONTEXT_KEY}.
 * <p>
 * To dispatch to a decorator in another web-app on the same server, use {@link ExternalDispatchedDecorator}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3.0
 */
public class DispatchedDecorator extends BaseWebAppDecorator {

    /** The Constant CONTENT_KEY. */
    public static final String CONTENT_KEY = "com.opensymphony.sitemesh.CONTENT";

    /** The Constant CONTEXT_KEY. */
    public static final String CONTEXT_KEY = "com.opensymphony.sitemesh.CONTEXT";

    /** The path. */
    private final String path;

    /**
     * Instantiates a new dispatched decorator.
     *
     * @param path
     *            the path
     */
    public DispatchedDecorator(String path) {
        this.path = path;
    }

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
        Object oldContent = request.getAttribute(CONTENT_KEY);
        Object oldWebAppContext = request.getAttribute(CONTEXT_KEY);

        request.setAttribute(CONTENT_KEY, content);
        request.setAttribute(CONTEXT_KEY, webAppContext);

        try {
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher(path);
            dispatcher.include(request, response);
        } finally {
            request.setAttribute(CONTENT_KEY, oldContent);
            request.setAttribute(CONTEXT_KEY, oldWebAppContext);
        }
    }

    /**
     * Locate web app.
     *
     * @param context
     *            the context
     *
     * @return the servlet context
     */
    protected ServletContext locateWebApp(ServletContext context) {
        // Overriden by ExternalDispatchedDecorator, which finds the context of another web-app.
        return context;
    }

}
