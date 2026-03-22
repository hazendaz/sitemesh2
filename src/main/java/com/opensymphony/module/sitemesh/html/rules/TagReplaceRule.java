/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
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

    /** The new tag name. */
    private final String newTagName;

    /**
     * Instantiates a new tag replace rule.
     *
     * @param originalTagName
     *            the original tag name
     * @param newTagName
     *            the new tag name
     */
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
