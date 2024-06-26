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
