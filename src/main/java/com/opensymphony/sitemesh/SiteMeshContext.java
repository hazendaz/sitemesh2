/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh;

/**
 * The Interface SiteMeshContext.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface SiteMeshContext {

    /**
     * Sets the content type.
     *
     * @param contentType
     *            the new content type
     */
    void setContentType(String contentType);

    /**
     * Gets the content type.
     *
     * @return the content type
     */
    String getContentType();

}
