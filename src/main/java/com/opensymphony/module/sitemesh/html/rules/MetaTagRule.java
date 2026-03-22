/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class MetaTagRule.
 */
public class MetaTagRule extends BasicRule {

    /** The page. */
    private final PageBuilder page;

    /**
     * Instantiates a new meta tag rule.
     *
     * @param page
     *            the page
     */
    public MetaTagRule(PageBuilder page) {
        super("meta");
        this.page = page;
    }

    @Override
    public void process(Tag tag) {
        if (tag.hasAttribute("name", false)) {
            page.addProperty("meta." + tag.getAttributeValue("name", false), tag.getAttributeValue("content", false));
        } else if (tag.hasAttribute("http-equiv", false)) {
            page.addProperty("meta.http-equiv." + tag.getAttributeValue("http-equiv", false),
                    tag.getAttributeValue("content", false));
        }
    }
}
