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
 * Title:        CookieDecoratorMapper
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * The CookieDecoratorMapper will map a suitable decorator based on a cookie value.
 * <p>
 * The CookieDecoratorMapper is configured via one properties. <code>cookie.name</code> - the cookie which contains the
 * name of the decorator which will be mapped.
 * </p>
 *
 * @author Paul Hammant
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 */
public class CookieDecoratorMapper extends AbstractDecoratorMapper {
    private String cookieName;

    public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        super.init(config, properties, parent);
        cookieName = properties.getProperty("cookie.name", null);
        if (cookieName == null) {
            throw new InstantiationException("'cookie.name' name parameter not set for this decorator mapper");
        }
    }

    public Decorator getDecorator(HttpServletRequest request, Page page) {
        Decorator result = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(cookieName)) {
                    result = getNamedDecorator(request, cookie.getValue());
                }
            }
        }
        return result == null ? super.getDecorator(request, page) : result;
    }
}
