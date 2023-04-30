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

import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.BaseWebAppDecorator;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adapts a SiteMesh 2 {@link com.opensymphony.module.sitemesh.Decorator} to a SiteMesh 3
 * {@link com.opensymphony.sitemesh.Decorator}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class OldDecorator2NewDecorator extends BaseWebAppDecorator implements RequestConstants {

    /** The old decorator. */
    private final com.opensymphony.module.sitemesh.Decorator oldDecorator;

    /**
     * Instantiates a new old decorator 2 new decorator.
     *
     * @param oldDecorator
     *            the old decorator
     */
    public OldDecorator2NewDecorator(com.opensymphony.module.sitemesh.Decorator oldDecorator) {
        this.oldDecorator = oldDecorator;
    }

    @Override
    protected void render(Content content, HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext, SiteMeshWebAppContext webAppContext) throws IOException, ServletException {

        request.setAttribute(PAGE, new Content2HTMLPage(content, request));

        // see if the URI path (webapp) is set
        // in a security conscious environment, the servlet container
        // may return null for a given URL
        if ((oldDecorator.getURIPath() != null) && (servletContext.getContext(oldDecorator.getURIPath()) != null)) {
            servletContext = servletContext.getContext(oldDecorator.getURIPath());
        }
        // get the dispatcher for the decorator
        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(oldDecorator.getPage());
        dispatcher.include(request, response);

        request.removeAttribute(PAGE);
    }

}
