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
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.State;
import com.opensymphony.module.sitemesh.html.Tag;
import com.opensymphony.module.sitemesh.html.rules.PageBuilder;
import com.opensymphony.module.sitemesh.parser.HTMLPageParser;

/**
 * The Class DivExtractingPageParser.
 */
public class DivExtractingPageParser extends HTMLPageParser {

    @Override
    protected void addUserDefinedRules(State html, final PageBuilder page) {
        super.addUserDefinedRules(html, page);
        html.addRule(new TopLevelDivExtractingRule(page));
    }

    /**
     * The Class TopLevelDivExtractingRule.
     */
    private static class TopLevelDivExtractingRule extends BasicRule {

        /** The block id. */
        private String blockId;

        /** The depth. */
        private int depth;

        /** The page. */
        private final PageBuilder page;

        /**
         * Instantiates a new top level div extracting rule.
         *
         * @param page
         *            the page
         */
        public TopLevelDivExtractingRule(PageBuilder page) {
            super("div");
            this.page = page;
        }

        @Override
        public void process(Tag tag) {
            if (tag.getType() == Tag.OPEN) {
                String id = tag.getAttributeValue("id", false);
                if (depth == 0 && id != null) {
                    currentBuffer().insert(tag.getPosition(), "<sitemesh:multipass id=\"div." + id + "\"/>");
                    blockId = id;
                    for (int i = 0; i < tag.getAttributeCount(); i++) {
                        page.addProperty(String.format("div.%s.%s", blockId, tag.getAttributeName(i)),
                                tag.getAttributeValue(i));
                    }
                    currentBuffer().markStartDelete(tag.getPosition());
                    context.pushBuffer(SitemeshBufferFragment.builder().setBuffer(context.getSitemeshBuffer()));
                    currentBuffer().markStart(tag.getPosition());
                }
                depth++;
            } else if (tag.getType() == Tag.CLOSE) {
                depth--;
                if (depth == 0 && blockId != null) {
                    currentBuffer().end(tag.getPosition() + tag.getLength());
                    page.addProperty("div." + blockId, getCurrentBufferContent());
                    blockId = null;
                    context.popBuffer();
                    currentBuffer().endDelete(tag.getPosition() + tag.getLength());
                }
            }
        }
    }
}
