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

import com.opensymphony.module.sitemesh.Page;

import org.apache.tapestry.BaseComponent;

/**
 * Base class for Tapestry decorator components.
 *
 * @author Erik Hatcher
 */
public class SiteMeshBase extends BaseComponent {

    /**
     * Gets the site mesh page.
     *
     * @return the site mesh page
     */
    public Page getSiteMeshPage() {
        return Util.getPage(getPage().getRequestCycle());
    }
}
