/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

/**
 * Text returned by HTMLTagTokenizer.
 *
 * @author Joe Walnes
 *
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TokenHandler
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer
 */
public interface Text {

    /**
     * Get the complete contents of the text block, preserving original formatting. This has a slight overhead in that
     * it needs to construct a String. For improved performance, use writeTo() instead.
     *
     * @return the contents
     */
    String getContents();

    /**
     * Write out the complete contents of the text block, preserving original formatting.
     *
     * @param buffer
     *            the buffer
     * @param position
     *            the position
     */
    void writeTo(SitemeshBufferFragment.Builder buffer, int position);

    /**
     * The position of the text.
     *
     * @return the position
     */
    int getPosition();

    /**
     * The length of the text.
     *
     * @return the length
     */
    int getLength();

}
