/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
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
