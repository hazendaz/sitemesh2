/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

/**
 * The Interface HTMLProcessorContext.
 */
public interface HTMLProcessorContext {

    /**
     * Gets the sitemesh buffer.
     *
     * @return the sitemesh buffer
     */
    SitemeshBuffer getSitemeshBuffer();

    /**
     * Current state.
     *
     * @return the state
     */
    State currentState();

    /**
     * Change state.
     *
     * @param newState
     *            the new state
     */
    void changeState(State newState);

    /**
     * Push buffer.
     *
     * @param fragment
     *            the fragment
     */
    void pushBuffer(SitemeshBufferFragment.Builder fragment);

    /**
     * Current buffer.
     *
     * @return the sitemesh buffer fragment. builder
     */
    SitemeshBufferFragment.Builder currentBuffer();

    /**
     * Pop buffer.
     *
     * @return the sitemesh buffer fragment. builder
     */
    SitemeshBufferFragment.Builder popBuffer();
}
