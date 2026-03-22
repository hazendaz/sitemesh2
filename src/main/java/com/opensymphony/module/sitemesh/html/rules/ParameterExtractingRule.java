/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class ParameterExtractingRule.
 */
public class ParameterExtractingRule extends BasicRule {

    /** The page. */
    private final PageBuilder page;

    /**
     * Instantiates a new parameter extracting rule.
     *
     * @param page
     *            the page
     */
    public ParameterExtractingRule(PageBuilder page) {
        super("parameter");
        this.page = page;
    }

    @Override
    public void process(Tag tag) {
        context.currentBuffer().delete(tag.getPosition(), tag.getLength());
        page.addProperty("page." + tag.getAttributeValue("name", false), tag.getAttributeValue("value", false));
    }
}
