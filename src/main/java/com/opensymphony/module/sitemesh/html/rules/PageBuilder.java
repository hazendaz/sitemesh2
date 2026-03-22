/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

/**
 * Allows a TagRule to add information to a Page object. The standard HTML processing rules bundled with SiteMesh use
 * this interface instead of direct coupling to the HTMLPage class, allowing the rules to be used for HTML processing in
 * applications outside of SiteMesh.
 *
 * @author Joe Walnes
 */
public interface PageBuilder {

    /**
     * Adds the property.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    void addProperty(String key, String value);
}
