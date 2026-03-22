/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh;

import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;

/**
 * The Interface ContentProcessor.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface ContentProcessor {

    /**
     * Handles.
     *
     * @param context
     *            the context
     *
     * @return true, if successful
     */
    boolean handles(SiteMeshContext context);

    /**
     * Handles.
     *
     * @param contentType
     *            the content type
     *
     * @return true, if successful
     */
    boolean handles(String contentType);

    /**
     * Builds the.
     *
     * @param buffer
     *            the buffer
     * @param context
     *            the context
     *
     * @return the content
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    Content build(SitemeshBuffer buffer, SiteMeshContext context) throws IOException;

}
