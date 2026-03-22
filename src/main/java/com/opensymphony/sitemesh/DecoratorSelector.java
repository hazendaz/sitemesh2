/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh;

/**
 * Selects an appropriate Decorator for the Content.
 * <p>
 * Note: Since SiteMesh 3, this replaces the DecoratorMapper.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface DecoratorSelector {

    /**
     * Select decorator.
     *
     * @param content
     *            the content
     * @param context
     *            the context
     *
     * @return the decorator
     */
    Decorator selectDecorator(Content content, SiteMeshContext context);

}
