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
 * Title:        Factory
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh;

import com.opensymphony.module.sitemesh.factory.FactoryException;
import com.opensymphony.module.sitemesh.util.ClassLoaderUtil;
import com.opensymphony.module.sitemesh.util.Container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

/**
 * Factory responsible for creating appropriate instances of implementations. This is specific to a web context and is
 * obtained through {@link #getInstance(com.opensymphony.module.sitemesh.Config)}.
 * <p>
 * The actual Factory method used is determined by the enviroment entry <code>sitemesh.factory</code>. If this doesn't
 * exist, it defaults to {@link com.opensymphony.module.sitemesh.factory.DefaultFactory} .
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public abstract class Factory implements PageParserSelector {

    /** Web context lookup key. */
    private static final String SITEMESH_FACTORY = "sitemesh.factory";

    /**
     * Entry-point for obtaining singleton instance of Factory. The default factory class that will be instantiated can
     * be overridden with the environment entry <code>sitemesh.factory</code>.
     *
     * @param config
     *            the config
     *
     * @return single instance of Factory
     */
    public static Factory getInstance(Config config) {
        Factory instance = (Factory) config.getServletContext().getAttribute(SITEMESH_FACTORY);
        if (instance == null) {
            String factoryClass = getEnvEntry("sitemesh.factory",
                    "com.opensymphony.module.sitemesh.factory.DefaultFactory");
            try {
                Class cls = ClassLoaderUtil.loadClass(factoryClass, config.getClass());
                Constructor con = cls.getConstructor(Config.class);
                instance = (Factory) con.newInstance(config);
                config.getServletContext().setAttribute(SITEMESH_FACTORY, instance);
            } catch (InvocationTargetException e) {
                throw new FactoryException("Cannot construct Factory : " + factoryClass, e.getTargetException());

            } catch (Exception e) {
                throw new FactoryException("Cannot construct Factory : " + factoryClass, e);
            }
        }
        instance.refresh();
        return instance;
    }

    /**
     * Refresh.
     */
    public abstract void refresh();

    /**
     * Return instance of DecoratorMapper.
     *
     * @return the decorator mapper
     */
    public abstract DecoratorMapper getDecoratorMapper();

    /**
     * Create a PageParser suitable for the given content-type.
     * <p>
     * For example, if the supplied parameter is <code>text/html</code> a parser shall be returned that can parse HTML
     * accordingly.
     * </p>
     * Never returns null.
     *
     * @param contentType
     *            The MIME content-type of the data to be parsed
     *
     * @return Appropriate <code>PageParser</code> for reading data
     */
    @Override
    public abstract PageParser getPageParser(String contentType);

    /** Determine whether a Page of given content-type should be parsed or not. */
    @Override
    public abstract boolean shouldParsePage(String contentType);

    /**
     * Determine whether the given path should be excluded from decoration or not.
     *
     * @param path
     *            the path
     *
     * @return true, if is path excluded
     */
    public abstract boolean isPathExcluded(String path);

    /**
     * Find String environment entry, or return default if not found.
     *
     * @param envEntry
     *            the env entry
     * @param defaultValue
     *            the default value
     *
     * @return the env entry
     */
    private static String getEnvEntry(String envEntry, String defaultValue) {
        String result = null;
        try {
            if (Container.get() != Container.JRUN) {
                // TODO: JRun really isn't happy with this
                InitialContext ctx = new InitialContext();
                Object o = ctx.lookup("java:comp/env/" + envEntry);
                ctx.close();
                result = (String) PortableRemoteObject.narrow(o, String.class); // rmi-iiop friendly.
            }
        } catch (Exception | NoClassDefFoundError e) {
            // failed - don't moan, just return default.
        }
        // to deal with restricted class loaders (i.e. on AppEngine).
        return result == null || result.trim().length() == 0 ? defaultValue : result;
    }
}
