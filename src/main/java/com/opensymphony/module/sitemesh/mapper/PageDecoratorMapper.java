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
 * Title:        PageDecoratorMapper
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

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * The PageDecoratorMapper allows the actual Page to determine the Decorator to be used.
 * <p>
 * The 'meta.decorator' and 'decorator' properties of the page are accessed and if any of them contain the name of a
 * valid Decorator, that Decorator shall be applied.
 * <p>
 * As an example, if HTML is being used, the Decorator could be chosen by using a
 * <code>&lt;html decorator="mydecorator"&gt;</code> root tag <i>or</i> by using a
 * <code>&lt;meta name="decorator" content="mydecorator"&gt;</code> tag in the header.
 * <p>
 * The actual properties to query are specified by passing properties to the mapper using the <code>property.?</code>
 * prefix. As the properties are stored in a Map, each key has to be unique. Example: property.1=decorator,
 * property.2=meta.decorator .
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public class PageDecoratorMapper extends AbstractDecoratorMapper {

    /** The page props. */
    private List<String> pageProps;

    @Override
    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        super.init(config, properties, parent);
        pageProps = new ArrayList<>();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith("property")) {
                pageProps.add((String) entry.getValue());
            }
        }
    }

    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        Decorator result = null;
        for (String entry : pageProps) {
            String propName = entry;
            result = getByProperty(request, page, propName);
            if (result != null) {
                break;
            }
        }
        return result == null ? super.getDecorator(request, page) : result;
    }

    /**
     * Gets the by property.
     *
     * @param request
     *            the request
     * @param p
     *            the p
     * @param name
     *            the name
     *
     * @return the by property
     */
    private Decorator getByProperty(HttpServletRequest request, Page p, String name) {
        if (p.isPropertySet(name)) {
            return getNamedDecorator(request, p.getProperty(name));
        }
        return null;
    }
}
