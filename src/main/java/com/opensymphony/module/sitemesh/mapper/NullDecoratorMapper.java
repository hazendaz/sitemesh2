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
 * Title:        NullDecoratorMapper
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
 * The NullDecoratorMapper represents the top-level DecoratorMapper that is finally delegated to if no other
 * DecoratorMapper has intervened. It is used so the parent property does not have to be checked by other
 * DecoratorMappers (null object pattern).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public class NullDecoratorMapper implements DecoratorMapper {

    /** Does nothing. */
    @Override
    public void init(Config config, Properties properties, DecoratorMapper parent) {
    }

    /** Returns null. */
    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        return null;
    }

    /** Returns null. */
    @Override
    public Decorator getNamedDecorator(HttpServletRequest request, String name) {
        return null;
    }
}
