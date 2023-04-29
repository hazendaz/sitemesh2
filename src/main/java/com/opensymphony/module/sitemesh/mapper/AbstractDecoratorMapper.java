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
 * Title:        AbstractDecoratorMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * Abstract DecoratorMapper implementation for easy creation of new DecoratorMappers.
 * <p>
 * Typically, an implementation would override getNamedDecorator() <b>or</b> getDecorator(). If a Decorator cannot be
 * returned from either of these, then they should delegate to their superclass.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public abstract class AbstractDecoratorMapper implements DecoratorMapper {
    /** Parent DecoratorMapper. */
    protected DecoratorMapper parent = null;

    /** The config. */
    protected Config config = null;

    /** Set parent. */
    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        this.parent = parent;
        this.config = config;
    }

    /** Delegate to parent. */
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        return parent.getDecorator(request, page);
    }

    /** Delegate to parent. */
    public Decorator getNamedDecorator(HttpServletRequest request, String name) {
        return parent.getNamedDecorator(request, name);
    }
}
