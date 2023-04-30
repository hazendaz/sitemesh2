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

public class HtmlAttributesRule extends BasicRule {

    private final PageBuilder page;

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
