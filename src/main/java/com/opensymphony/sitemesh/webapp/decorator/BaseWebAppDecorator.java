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
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Convenient base class for all Java web-app based decorators that make use of the Servlet API.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3.0
 */
public abstract class BaseWebAppDecorator implements Decorator {

    /**
     * More convenient version of
     * {@link #render(com.opensymphony.sitemesh.Content, com.opensymphony.sitemesh.SiteMeshContext)} suited for Servlet
     * API calls.
     */
    protected abstract void render(Content content, HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext, SiteMeshWebAppContext webAppContext) throws IOException, ServletException;

    public void render(Content content, SiteMeshContext context) {
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        try {
            render(content, webAppContext.getRequest(), webAppContext.getResponse(), webAppContext.getServletContext(),
                    webAppContext);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception during rendering", e);
        } catch (ServletException e) {
            throw new RuntimeException("Unexpected exception during rendering", e);
        }
    }
}
