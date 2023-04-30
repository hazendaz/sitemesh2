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

import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.Tag;

public class ContentBlockExtractingRule extends BlockExtractingRule {

    private final PageBuilder page;

    private String contentBlockId;

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
