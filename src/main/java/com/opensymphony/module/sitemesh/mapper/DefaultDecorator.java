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
/*
 * Title:        DefaultDecorator
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import com.opensymphony.module.sitemesh.Decorator;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of Decorator. All properties are set by the constructor.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.Decorator
 */
public class DefaultDecorator implements Decorator {

    /**
     * The page.
     *
     * @see #getPage()
     */
    protected String page;

    /**
     * The name.
     *
     * @see #getName()
     */
    protected String name;

    /**
     * The uri path.
     *
     * @see #getURIPath()
     */
    protected String uriPath;

    /**
     * The role.
     *
     * @see #getRole()
     */
    protected String role;

    /**
     * The parameters.
     *
     * @see #getInitParameter(java.lang.String)
     */
    protected Map<Object, Object> parameters;

    /**
     * Constructor to set name, page and parameters.
     *
     * @param name
     *            the name
     * @param page
     *            the page
     * @param parameters
     *            the parameters
     */
    public DefaultDecorator(String name, String page, Map<Object, Object> parameters) {
        this(name, page, null, null, parameters);
    }

    /**
     * Constructor to set all properties.
     *
     * @param name
     *            the name
     * @param page
     *            the page
     * @param uriPath
     *            the uri path
     * @param parameters
     *            the parameters
     */
    public DefaultDecorator(String name, String page, String uriPath, Map<Object, Object> parameters) {
        this(name, page, uriPath, null, parameters);
    }

    /**
     * Constructor to set all properties.
     *
     * @param name
     *            the name
     * @param page
     *            the page
     * @param uriPath
     *            the uri path
     * @param role
     *            the role
     * @param parameters
     *            the parameters
     */
    public DefaultDecorator(String name, String page, String uriPath, String role, Map<Object, Object> parameters) {
        this.name = name;
        this.page = page;
        this.uriPath = uriPath;
        this.role = role;
        this.parameters = parameters;
    }

    /**
     * URI of the Servlet/JSP to dispatch the request to (relative to the web-app context).
     */
    @Override
    public String getPage() {
        return page;
    }

    /** Name of Decorator. For information purposes only. */
    @Override
    public String getName() {
        return name;
    }

    /** URI path of the Decorator. Enables support for decorators defined in seperate web-apps. */
    @Override
    public String getURIPath() {
        return uriPath;
    }

    /** Role the user has to be in to get this decorator applied. */
    @Override
    public String getRole() {
        return role;
    }

    /**
     * Returns a String containing the value of the named initialization parameter, or null if the parameter does not
     * exist.
     *
     * @param paramName
     *            Key of parameter.
     *
     * @return Value of parameter or null if not found.
     */
    @Override
    public String getInitParameter(String paramName) {
        if (parameters == null || !parameters.containsKey(paramName)) {
            return null;
        }

        return (String) parameters.get(paramName);
    }

    /**
     * Returns the names of the Decorator's initialization parameters as an Iterator of String objects, or an empty
     * Iterator if the Decorator has no initialization parameters.
     */
    @Override
    public Iterator<Object> getInitParameterNames() {
        if (parameters == null) {
            // make sure we always return an empty iterator
            return Collections.emptyMap().keySet().iterator();
        }

        return parameters.keySet().iterator();
    }
}
