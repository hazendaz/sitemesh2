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

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.HTMLProcessor;
import com.opensymphony.module.sitemesh.html.Tag;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * The Class MultipassReplacementPageParser.
 */
public class MultipassReplacementPageParser implements PageParser {

    /** The page. */
    private final Page page;

    /** The response. */
    private final HttpServletResponse response;

    /**
     * Instantiates a new multipass replacement page parser.
     *
     * @param page
     *            the page
     * @param response
     *            the response
     */
    public MultipassReplacementPageParser(Page page, HttpServletResponse response) {
        this.page = page;
        this.response = response;
    }

    @Override
    public Page parse(char[] buffer) throws IOException {
        return parse(new DefaultSitemeshBuffer(buffer));
    }

    @Override
    public Page parse(SitemeshBuffer sitemeshBuffer) throws IOException {
        SitemeshBufferFragment.Builder builder = SitemeshBufferFragment.builder().setBuffer(sitemeshBuffer);
        HTMLProcessor processor = new HTMLProcessor(sitemeshBuffer, builder);
        processor.addRule(new BasicRule("sitemesh:multipass") {
            @Override
            public void process(Tag tag) {
                currentBuffer().delete(tag.getPosition(), tag.getLength());
                String id = tag.getAttributeValue("id", true);
                if (!page.isPropertySet("_sitemesh.removefrompage." + id)) {
                    currentBuffer().insert(tag.getPosition(), page.getProperty(id));
                }
            }
        });
        processor.process();

        builder.build().writeTo(response.getWriter());
        return null;
    }
}
