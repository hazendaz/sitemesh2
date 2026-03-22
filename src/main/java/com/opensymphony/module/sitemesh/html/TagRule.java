/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

/**
 * The Interface TagRule.
 */
public interface TagRule {

    /**
     * Sets the context.
     *
     * @param context
     *            the new context
     */
    void setContext(HTMLProcessorContext context);

    /**
     * Called by the HTMLProcessor to determine if a rule should be called for a given tag. The name parameter will
     * always be passed in lowercase.
     *
     * @param name
     *            the name
     *
     * @return true, if successful
     */
    boolean shouldProcess(String name);

    /**
     * Process.
     *
     * @param tag
     *            the tag
     */
    void process(Tag tag);
}
