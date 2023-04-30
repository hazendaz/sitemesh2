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

public class TitleExtractingRule extends BlockExtractingRule {

    private final PageBuilder page;

    private boolean seenTitle;

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
