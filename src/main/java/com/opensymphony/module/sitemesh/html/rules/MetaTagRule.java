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

public class MetaTagRule extends BasicRule {

    private final PageBuilder page;

    public MetaTagRule(PageBuilder page) {
        super("meta");
        this.page = page;
    }

    public void process(Tag tag) {
        if (tag.hasAttribute("name", false)) {
            page.addProperty("meta." + tag.getAttributeValue("name", false), tag.getAttributeValue("content", false));
        } else if (tag.hasAttribute("http-equiv", false)) {
            page.addProperty("meta.http-equiv." + tag.getAttributeValue("http-equiv", false), tag.getAttributeValue("content", false));
        }
    }
}
