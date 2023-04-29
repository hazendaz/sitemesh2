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

import javax.servlet.ServletContext;

/**
 * Decorator that dispatches to another path in A DIFFERENT WEB-APP in the same Servlet Container (such as a JSP or path
 * mapped to a Servlet).
 * <p>
 * The Content and SiteMeshContext objects are passed to the decorator using the HttpServletRequest attributes
 * {@link #CONTENT_KEY} and {@link #CONTEXT_KEY}.
 * <p>
 * To dispatch to a decorator in the same web-app, use {@link DispatchedDecorator}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3.0
 */
public class ExternalDispatchedDecorator extends DispatchedDecorator {

    /** The web app. */
    private final String webApp;

    /**
     * Instantiates a new external dispatched decorator.
     *
     * @param path
     *            the path
     * @param webApp
     *            the web app
     */
    public ExternalDispatchedDecorator(String path, String webApp) {
        super(path);
        this.webApp = webApp;
    }

    @Override
    protected ServletContext locateWebApp(ServletContext context) {
        ServletContext externalContext = context.getContext(webApp);
        if (externalContext != null) {
            return externalContext;
        } else {
            // in a security conscious environment, the servlet container
            // may return null for a given URL
            throw new SecurityException("Cannot obtain ServletContext for web-app : " + webApp);
        }
    }
}
