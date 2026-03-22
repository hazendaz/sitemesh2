/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh;

/**
 * The Interface Decorator.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface Decorator {

    /**
     * Render.
     *
     * @param content
     *            the content
     * @param context
     *            the context
     */
    void render(Content content, SiteMeshContext context);

}
