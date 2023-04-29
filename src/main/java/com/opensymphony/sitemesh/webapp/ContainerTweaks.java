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
package com.opensymphony.sitemesh.webapp;

import com.opensymphony.module.sitemesh.util.Container;

/**
 * Provides details of which tweaks should be used in SiteMesh - necessary because containers behave subtly different.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class ContainerTweaks {

    // TODO: Externalize these into a config file (optional of course!), allowing users to change them at runtime if
    // needed.

    /** The container. */
    private final int container = Container.get();

    /**
     * Should auto create session.
     *
     * @return true, if successful
     */
    public boolean shouldAutoCreateSession() {
        return false;
        // return container == Container.TOMCAT; - this is removed due to SIM-151.
    }

    /**
     * Should log unhandled exceptions.
     *
     * @return true, if successful
     */
    public boolean shouldLogUnhandledExceptions() {
        return container == Container.TOMCAT;
    }

    /**
     * Should ignore illegal state exception on error page.
     *
     * @return true, if successful
     */
    public boolean shouldIgnoreIllegalStateExceptionOnErrorPage() {
        return container == Container.WEBLOGIC;
    }

}
