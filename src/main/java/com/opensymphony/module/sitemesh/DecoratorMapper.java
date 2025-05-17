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
 * Title:        DecoratorMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Properties;

/**
 * The DecoratorMapper is responsible for determining which {@link com.opensymphony.module.sitemesh.Decorator} should be
 * used for a {@link com.opensymphony.module.sitemesh.Page}.
 * <p>
 * Implementations of this are returned by the {@link com.opensymphony.module.sitemesh.Factory}, and should be
 * thread-safe.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public interface DecoratorMapper {

    /**
     * Initialize the mapper. This is always called before the other methods.
     *
     * @param config
     *            Config supplied by Servlet or Filter.
     * @param properties
     *            Any initialization properties (specific to implementation).
     * @param parent
     *            the parent
     *
     * @throws InstantiationException
     *             the instantiation exception
     */
    void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException;

    /**
     * Return appropriate {@link com.opensymphony.module.sitemesh.Decorator} for a certain Page.
     * <p>
     * The implementation can determine the result based on the actual request or the data of the parsed page. Typically
     * this would call <code>getNamedDecorator()</code> which would delegate to a parent DecoratorMapper.
     * </p>
     *
     * @param request
     *            the request
     * @param page
     *            the page
     *
     * @return the decorator
     */
    Decorator getDecorator(HttpServletRequest request, Page page);

    /**
     * Return a {@link com.opensymphony.module.sitemesh.Decorator} with given name.
     *
     * @param request
     *            the request
     * @param name
     *            the name
     *
     * @return the named decorator
     */
    Decorator getNamedDecorator(HttpServletRequest request, String name);
}
