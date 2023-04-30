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
import com.opensymphony.module.sitemesh.html.CustomTag;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * Very simple rule for replacing all occurences of one tag with another.
 * <p>
 * For example, to convert all &lt;b&gt; tags to &lt;strong&gt;:
 * </p>
 * <p>
 * html.addRule(new TagReplaceRule("b", "strong"));
 * </p>
 *
 * @author Joe Walnes
 */
public class TagReplaceRule extends BasicRule {

    private final String newTagName;

    public TagReplaceRule(String originalTagName, String newTagName) {
        super(originalTagName);
        this.newTagName = newTagName;
    }

    @Override
    public void process(Tag tag) {
        currentBuffer().delete(tag.getPosition(), tag.getLength());
        CustomTag customTag = new CustomTag(tag);
        customTag.setName(newTagName);
        customTag.writeTo(currentBuffer(), tag.getPosition());
    }
}
