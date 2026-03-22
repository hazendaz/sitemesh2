/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class ContentBlockExtractingRule.
 */
public class ContentBlockExtractingRule extends BlockExtractingRule {

    /** The page. */
    private final PageBuilder page;

    /** The content block id. */
    private String contentBlockId;

    /**
     * Instantiates a new content block extracting rule.
     *
     * @param page
     *            the page
     */
    public ContentBlockExtractingRule(PageBuilder page) {
        super(false, "content");
        this.page = page;
    }

    @Override
    protected void start(Tag tag) {
        contentBlockId = tag.getAttributeValue("tag", false);
    }

    @Override
    protected void end(Tag tag) {
        page.addProperty("page." + contentBlockId, getCurrentBufferContent());
    }

}
