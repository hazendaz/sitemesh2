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
 * Title:        ParameterDecoratorMapper
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

import java.util.Properties;

/**
 * The ParameterDecoratorMapper will map a suitable decorator based on request parameters.
 * <p>
 * The ParameterDecoratorMapper is configured via three properties.
 * <p>
 * <code>decorator.parameter</code> - the parameter which contains the name of the decorator which will be mapped. The
 * default is "decorator".
 * <p>
 * For example if <code>decorator.parameter</code> is "foobar" then myurl.jsp?foobar=mydecorator will map to the
 * decorator named "mydecorator".
 * <p>
 * You can also supply an optional 'confirmation parameter'. The decorator will only be mapped if the parameter named
 * <code>parameter.name</code> is in the request URI and the value of that parameter is equal to the
 * <code>parameter.value</code> property.
 * <p>
 * For example assuming parameter.name=confirm and parameter.value=true the URI
 * myurl.jsp?decorator=mydecorator&amp;confirm=true will map the decorator mydecorator. where as the URIs
 * myurl.jsp?decorator=mydecorator and myurl.jsp?decorator=mydecorator&amp;confirm=false will not return any decorator.
 *
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public class ParameterDecoratorMapper extends AbstractDecoratorMapper {

    /** The param value. */
    private String decoratorParameter = null, paramName = null, paramValue = null;

    @Override
    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        super.init(config, properties, parent);
        decoratorParameter = properties.getProperty("decorator.parameter", "decorator");
        paramName = properties.getProperty("parameter.name", null);
        paramValue = properties.getProperty("parameter.value", null);
    }

    @Override
    public Decorator getDecorator(HttpServletRequest request, Page page) {
        Decorator result = null;
        String decoratorParamValue = request.getParameter(decoratorParameter);

        if ((paramName == null || paramValue.equals(request.getParameter(paramName))) && decoratorParamValue != null
                && !decoratorParamValue.trim().equals("")) {
            result = getNamedDecorator(request, decoratorParamValue);
        }
        return result == null ? super.getDecorator(request, page) : result;
    }
}
