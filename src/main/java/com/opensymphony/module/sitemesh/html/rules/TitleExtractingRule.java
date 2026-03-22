/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class TitleExtractingRule.
 */
public class TitleExtractingRule extends BlockExtractingRule {

    /** The page. */
    private final PageBuilder page;

    /** The seen title. */
    private boolean seenTitle;

    /**
     * Instantiates a new title extracting rule.
     *
     * @param page
     *            the page
     */
    public TitleExtractingRule(PageBuilder page) {
        super(false, "title");
        this.page = page;
    }

    @Override
    protected void end(Tag tag) {
        if (!seenTitle) {
            page.addProperty("title", getCurrentBufferContent());
            seenTitle = true;
        }
    }
}
