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
 * Title:        FrameSetDecoratorMapper
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
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * The FrameSetDecoratorMapper will use the specified decorator when the Page is an instance of
 * {@link com.opensymphony.module.sitemesh.HTMLPage} and <code>isFrameSet()</code> returns true.
 * <p>
 * The name of this decorator should be supplied in the <code>decorator</code> property - if no decorator property is
 * supplied, no decorator is applied to frame based pages.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public class FrameSetDecoratorMapper extends AbstractDecoratorMapper {

    /** The decorator. */
    private String decorator = null;

    @Override
    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        super.init(config, properties, parent);
        decorator = properties.getProperty("decorator");
    }

    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        if (page instanceof HTMLPage && ((HTMLPage) page).isFrameSet()) {
            return getNamedDecorator(request, decorator);
        }
        return super.getDecorator(request, page);
    }
}
