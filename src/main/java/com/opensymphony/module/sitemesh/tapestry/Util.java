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
package com.opensymphony.module.sitemesh.tapestry;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.RequestConstants;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.RenderString;

/**
 * This utility class gives easy access to the SiteMesh page, with convenience methods for title and property. A common
 * usage would be with OGNL expressions like this:
 *
 * <pre>
 * {@code
 * <html jwcid="@Shell" title="ognl:@com.opensymphony.module.sitemesh.tapestry.Util@getTitle()">
 * <p>
 * In future versions of Tapestry, thanks to HiveMind integration, this will become a lot cleaner, probably like this:
 * <p>
 * <html jwcid="@Shell" title="sitemesh:title">
 * }
 * </pre>
 *
 * @author Erik Hatcher
 */
public final class Util {

    /**
     * Gets the title.
     *
     * @param cycle
     *            the cycle
     *
     * @return the title
     */
    public static String getTitle(IRequestCycle cycle) {
        return getPage(cycle).getTitle();
    }

    /**
     * Gets the property.
     *
     * @param name
     *            the name
     * @param cycle
     *            the cycle
     *
     * @return the property
     */
    public static String getProperty(String name, IRequestCycle cycle) {
        return getPage(cycle).getProperty(name);
    }

    /**
     * Gets the page.
     *
     * @param cycle
     *            the cycle
     *
     * @return the page
     */
    public static Page getPage(IRequestCycle cycle) {
        return (Page) cycle.getRequestContext().getRequest().getAttribute(RequestConstants.PAGE);
    }

    /**
     * Gets the head renderer.
     *
     * @param cycle
     *            the cycle
     *
     * @return the head renderer
     */
    public static IRender getHeadRenderer(IRequestCycle cycle) {
        return new RenderString(((HTMLPage) getPage(cycle)).getHead(), true);
    }

    /**
     * Instantiates a new util.
     */
    private Util() {
        // Do not allow instantiation
    }
}
