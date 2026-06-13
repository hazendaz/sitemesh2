/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.tapestry;

import org.apache.hivemind.HiveMind;

/**
 * The Class Title.
 */
public abstract class Title extends SiteMeshBase {

    /**
     * Gets the default.
     *
     * @return the default
     */
    public abstract String getDefault();

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        String title = getSiteMeshPage().getTitle();
        return HiveMind.isBlank(title) ? getDefault() : title;
    }
}
