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
 * Title:        EnvEntryDecoratorMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import com.opensymphony.module.sitemesh.Decorator;

import jakarta.servlet.http.HttpServletRequest;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The EnvEntryDecoratorMapper allows the reference to a web-app environment entry for the decorator name, and falls
 * back to ConfigDecoratorMapper's behavior if no matching environment entry is found.
 * <p>
 * In some cases, it's desirable to allow a deployer, as opposed to a developer, to specify a decorator. In a .WAR file,
 * this can be very difficult, since decorator mappings are specified in <code>decorators.xml</code> (more or less).
 * This mapper corrects that by allowing two types of mapping. If the decorator name is found in an
 * <code>&lt;env-entry&gt;</code>, the entry's value is used as the decorator reference.
 * <p>
 * Known Issues:
 * <ol>
 * <li>It still uses the decorator path (from <code>decorators.xml</code>). This needs to be corrected for full
 * functionality. If anyone has a suggestion on how...</li>
 * </ol>
 *
 * @author <a href="mailto:joeo@enigmastation.com">Joseph B. Ottinger</a>
 *
 * @see com.opensymphony.module.sitemesh.mapper.ConfigDecoratorMapper
 */
public class EnvEntryDecoratorMapper extends ConfigDecoratorMapper {

    /** The Logger. */
    private static final Logger logger = LoggerFactory.getLogger(EnvEntryDecoratorMapper.class);

    /**
     * Retrieves the {@link com.opensymphony.module.sitemesh.Decorator} specified by the decorator name. If it's not in
     * the environment entries of the web application, assume it's a named decorator from <code>decorators.xml</code>.
     */
    @Override
    public Decorator getNamedDecorator(HttpServletRequest request, String name) {
        String resourceValue = getStringResource(name);
        if (resourceValue == null) {
            return super.getNamedDecorator(request, name);
        }
        return new DefaultDecorator(name, resourceValue, null);
    }

    /**
     * This pulls a value out of the web-app environment. If the value isn't there, returns null.
     *
     * @param name
     *            the name
     *
     * @return the string resource
     */
    public static String getStringResource(String name) {
        String value = null;
        Context ctx = null;
        try {
            ctx = new InitialContext();
            Object o = ctx.lookup("java:comp/env/" + name);
            if (o != null) {
                value = o.toString();
            }
        } catch (NamingException e) {
            logger.error("", e);
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                }
            } catch (NamingException e) {
                logger.error("", e);
            }
        }
        return value;
    }
}
