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
