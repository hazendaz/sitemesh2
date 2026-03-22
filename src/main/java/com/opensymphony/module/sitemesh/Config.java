/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
/*
 * Title:        Config
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Common interface to ServletConfig and FilterConfig (since javax.servlet.Config was removed from 2.3 spec).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class Config {

    /** The servlet config. */
    private ServletConfig servletConfig;

    /** The filter config. */
    private FilterConfig filterConfig;

    /** The config file. */
    private String configFile;

    /**
     * Instantiates a new config.
     *
     * @param servletConfig
     *            the servlet config
     */
    public Config(ServletConfig servletConfig) {
        if (servletConfig == null) {
            throw new NullPointerException("ServletConfig cannot be null");
        }
        this.servletConfig = servletConfig;
        this.configFile = servletConfig.getInitParameter("configFile");
    }

    /**
     * Instantiates a new config.
     *
     * @param filterConfig
     *            the filter config
     */
    public Config(FilterConfig filterConfig) {
        if (filterConfig == null) {
            throw new NullPointerException("FilterConfig cannot be null");
        }
        this.filterConfig = filterConfig;
        this.configFile = filterConfig.getInitParameter("configFile");
    }

    /**
     * Gets the servlet context.
     *
     * @return the servlet context
     */
    public ServletContext getServletContext() {
        return servletConfig != null ? servletConfig.getServletContext() : filterConfig.getServletContext();
    }

    /**
     * Gets the config file.
     *
     * @return the config file
     */
    public String getConfigFile() {
        return configFile;
    }
}
