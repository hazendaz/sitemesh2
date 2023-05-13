/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2023 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     Hazendaz (Jeremy Landis).
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * The Class BodyTagRule.
 */
public class BodyTagRule extends BasicRule {

    /** The page. */
    private final PageBuilder page;

    /** The body. */
    private final SitemeshBufferFragment.Builder body;

    /**
     * Instantiates a new body tag rule.
     *
     * @param page
     *            the page
     * @param body
     *            the body
     */
    public BodyTagRule(PageBuilder page, SitemeshBufferFragment.Builder body) {
        super("body");
        this.page = page;
        this.body = body;
    }

    @Override
    public void process(Tag tag) {
        if (tag.getType() == Tag.OPEN || tag.getType() == Tag.EMPTY) {
            context.currentBuffer().setStart(tag.getPosition() + tag.getLength());
            for (int i = 0; i < tag.getAttributeCount(); i++) {
                page.addProperty("body." + tag.getAttributeName(i), tag.getAttributeValue(i));
            }
            body.markStart(tag.getPosition() + tag.getLength());
        } else {
            body.end(tag.getPosition());
            context.pushBuffer(SitemeshBufferFragment.builder()); // unused buffer: everything after </body> is
            // discarded.
        }
    }

}
