/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.BlockExtractingRule;

/**
 * The Class HeadExtractingRule.
 */
public class HeadExtractingRule extends BlockExtractingRule {

    /** The head. */
    private final SitemeshBufferFragment.Builder head;

    /**
     * Instantiates a new head extracting rule.
     *
     * @param head
     *            the head
     */
    public HeadExtractingRule(SitemeshBufferFragment.Builder head) {
        super(false, "head");
        this.head = head;
    }

    @Override
    protected SitemeshBufferFragment.Builder createBuffer(SitemeshBuffer sitemeshBuffer) {
        return head;
    }

}
