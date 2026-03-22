/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

/**
 * The Class BasicRule.
 */
public abstract class BasicRule implements TagRule {

    /** The acceptable tag names. */
    private final String[] acceptableTagNames;

    /** The context. */
    protected HTMLProcessorContext context;

    /**
     * Instantiates a new basic rule.
     *
     * @param acceptableTagNames
     *            the acceptable tag names
     */
    protected BasicRule(String[] acceptableTagNames) {
        this.acceptableTagNames = acceptableTagNames;
    }

    /**
     * Instantiates a new basic rule.
     *
     * @param acceptableTagName
     *            the acceptable tag name
     */
    protected BasicRule(String acceptableTagName) {
        this.acceptableTagNames = new String[] { acceptableTagName };
    }

    /**
     * Instantiates a new basic rule.
     */
    protected BasicRule() {
        this.acceptableTagNames = null;
    }

    @Override
    public void setContext(HTMLProcessorContext context) {
        this.context = context;
    }

    @Override
    public boolean shouldProcess(String name) {
        if (acceptableTagNames == null || acceptableTagNames.length < 1) {
            throw new UnsupportedOperationException(getClass().getName()
                    + " should be constructed with acceptableTagNames OR should implement shouldProcess()");
        }

        for (String acceptableTagName : acceptableTagNames) {
            if (name.equals(acceptableTagName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Current buffer.
     *
     * @return the sitemesh buffer fragment. builder
     */
    protected SitemeshBufferFragment.Builder currentBuffer() {
        return context.currentBuffer();
    }

    /**
     * Gets the current buffer content.
     *
     * @return the current buffer content
     */
    protected String getCurrentBufferContent() {
        return context.currentBuffer().build().getStringContent();
    }

}
