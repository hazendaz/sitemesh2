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
 * Title:        Decorator
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh;

import java.util.Iterator;

/**
 * Representation of a Decorator.
 * <p>
 * A Decorator is infact a Servlet/JSP, and this is a wrapper to reference it. An implementation is returned by the
 * {@link com.opensymphony.module.sitemesh.DecoratorMapper}.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public interface Decorator {

    /**
     * URI of the Servlet/JSP to dispatch the request to (relative to the web-app context).
     *
     * @return the page
     */
    String getPage();

    /**
     * Name of the Decorator. For informational purposes only.
     *
     * @return the name
     */
    String getName();

    /**
     * URI path of the Decorator. Enables support for decorators defined in seperate web-apps.
     *
     * @return the URI path
     */
    String getURIPath();

    /**
     * Role the user has to be in to get this decorator applied.
     *
     * @return the role
     */
    String getRole();

    /**
     * Returns a String containing the value of the named initialization parameter, or null if the parameter does not
     * exist.
     *
     * @param paramName
     *            Key of parameter.
     *
     * @return Value of the parameter or null if not found.
     */
    String getInitParameter(String paramName);

    /**
     * Returns the names of the Decorator's initialization parameters as an Iterator of String objects, or an empty
     * Iterator if the Decorator has no initialization parameters.
     *
     * @return the inits the parameter names
     */
    Iterator<Object> getInitParameterNames();
}
