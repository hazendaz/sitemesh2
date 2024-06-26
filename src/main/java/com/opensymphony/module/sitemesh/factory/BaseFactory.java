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
 * Title:        BaseFactory
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.factory;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.mapper.PathMapper;
import com.opensymphony.module.sitemesh.util.ClassLoaderUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Base Factory implementation. Provides utility methods for implementation.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public abstract class BaseFactory extends Factory {

    /** The config is of type ServletConfig or FilterConfig. */
    protected Config config;

    /**
     * Instance of {@link com.opensymphony.module.sitemesh.DecoratorMapper}. Because it is thread-safe, it can be shared
     * by multiple clients. This is only the last DecoratorMapper in the chain, and all parents will be automatically
     * delegated to it.
     */
    protected DecoratorMapper decoratorMapper;

    /** Map that associates content-types with PageParser instances. */
    protected Map<Object, Object> pageParsers;

    /** A map of paths that are excluded from decoration. */
    protected PathMapper excludeUrls;

    /**
     * Constructor for default implementation of Factory. Should never be called by client. Singleton instance should be
     * obtained instead.
     *
     * @param config
     *            the config
     *
     * @see #getInstance(com.opensymphony.module.sitemesh.Config config)
     */
    protected BaseFactory(Config config) {
        this.config = config;
        clearDecoratorMappers();
        clearParserMappings();
        clearExcludeUrls();
    }

    /** Return instance of DecoratorMapper. */
    @Override
    public DecoratorMapper getDecoratorMapper() {
        return decoratorMapper;
    }

    /**
     * Create a PageParser suitable for the given content-type.
     * <p>
     * For example, if the supplied parameter is <code>text/html</code> a parser shall be returned that can parse HTML
     * accordingly. Returns null if no parser can be found for the supplied content type.
     * </p>
     *
     * @param contentType
     *            The MIME content-type of the data to be parsed
     *
     * @return Appropriate <code>PageParser</code> for reading data, or <code>null</code> if no suitable parser was
     *         found.
     */
    @Override
    public PageParser getPageParser(String contentType) {
        return (PageParser) pageParsers.get(contentType);
    }

    /**
     * Determine whether a Page of given content-type should be parsed or not.
     */
    @Override
    public boolean shouldParsePage(String contentType) {
        return pageParsers.containsKey(contentType);
    }

    /**
     * Returns <code>true</code> if the supplied path matches one of the exclude URLs specified in sitemesh.xml,
     * otherwise returns <code>false</code>.
     *
     * @param path
     *            The path.
     *
     * @return whether the path is excluded
     */
    @Override
    public boolean isPathExcluded(String path) {
        return excludeUrls.get(path) != null;
    }

    /**
     * Clear all current DecoratorMappers.
     */
    protected void clearDecoratorMappers() {
        decoratorMapper = null;
    }

    /**
     * Push new DecoratorMapper onto end of chain.
     *
     * @param className
     *            the class name
     * @param properties
     *            the properties
     */
    protected void pushDecoratorMapper(String className, Properties properties) {
        try {
            Class<?> decoratorMapperClass = ClassLoaderUtil.loadClass(className, getClass());
            DecoratorMapper newMapper = getDecoratorMapper(decoratorMapperClass);
            newMapper.init(config, properties, decoratorMapper);
            decoratorMapper = newMapper;
        } catch (ClassNotFoundException e) {
            throw new FactoryException("Could not load DecoratorMapper class : " + className, e);
        } catch (Exception e) {
            throw new FactoryException("Could not initialize DecoratorMapper : " + className, e);
        }
    }

    /**
     * Gets the decorator mapper.
     *
     * @param decoratorMapperClass
     *            the decorator mapper class
     *
     * @return the decorator mapper
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    protected DecoratorMapper getDecoratorMapper(Class<?> decoratorMapperClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return (DecoratorMapper) decoratorMapperClass.getDeclaredConstructor().newInstance();
    }

    /** Clear all PageParser mappings. */
    protected void clearParserMappings() {
        pageParsers = new HashMap<>();
    }

    /**
     * Map new PageParser to given content-type. contentType = null signifies default PageParser for unknown
     * content-types.
     *
     * @param contentType
     *            the content type
     * @param className
     *            the class name
     */
    protected void mapParser(String contentType, String className) {
        if (className.endsWith(".DefaultPageParser")) {
            return; // Backwards compatability - this can safely be ignored.
        }
        try {
            PageParser pp = (PageParser) ClassLoaderUtil.loadClass(className, getClass()).getDeclaredConstructor()
                    .newInstance();
            // Store the parser even if the content type is NULL. [This
            // is most probably the legacy default page parser which
            // we no longer have a use for]
            pageParsers.put(contentType, pp);
        } catch (ClassNotFoundException e) {
            throw new FactoryException("Could not load PageParser class : " + className, e);
        } catch (Exception e) {
            throw new FactoryException("Could not instantiate PageParser : " + className, e);
        }
    }

    /**
     * Adds the exclude url.
     *
     * @param path
     *            the path
     */
    protected void addExcludeUrl(String path) {
        excludeUrls.put("", path);
    }

    /**
     * Clears all exclude URLs.
     */
    protected void clearExcludeUrls() {
        excludeUrls = new PathMapper();
    }

}
