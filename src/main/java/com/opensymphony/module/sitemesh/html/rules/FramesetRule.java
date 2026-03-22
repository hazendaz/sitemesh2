/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class FramesetRule.
 */
public class FramesetRule extends BasicRule {

    /** The page. */
    private final PageBuilder page;

    /**
     * Instantiates a new frameset rule.
     *
     * @param page
     *            the page
     */
    public FramesetRule(PageBuilder page) {
        super(new String[] { "frame", "frameset" });
        this.page = page;
    }

    @Override
    public void process(Tag tag) {
        context.currentBuffer().delete(tag.getPosition(), tag.getLength());
        page.addProperty("frameset", "true");
    }

}
