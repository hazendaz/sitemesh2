/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class HtmlAttributesRule.
 */
public class HtmlAttributesRule extends BasicRule {

    /** The page. */
    private final PageBuilder page;

    /**
     * Instantiates a new html attributes rule.
     *
     * @param page
     *            the page
     */
    public HtmlAttributesRule(PageBuilder page) {
        super("html");
        this.page = page;
    }

    @Override
    public void process(Tag tag) {
        if (tag.getType() == Tag.OPEN) {
            context.currentBuffer().markStart(tag.getPosition() + tag.getLength());
            for (int i = 0; i < tag.getAttributeCount(); i++) {
                page.addProperty(tag.getAttributeName(i), tag.getAttributeValue(i));
            }
        } else {
            context.currentBuffer().end(tag.getPosition());
        }
    }

}
