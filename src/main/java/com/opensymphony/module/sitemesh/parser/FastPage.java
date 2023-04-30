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
/*
 * Title:        FastPage
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

/**
 * HTMLPage implementation produced by FastPageParser.
 *
 * @author <a href="mailto:salaman@qoretech.com">Victor Salaman</a>
 */
public final class FastPage extends AbstractHTMLPage {

    /** The head. */
    private String head;

    /** The body. */
    private String body;

    /**
     * Instantiates a new fast page.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     * @param sitemeshProps
     *            the sitemesh props
     * @param htmlProps
     *            the html props
     * @param metaProps
     *            the meta props
     * @param bodyProps
     *            the body props
     * @param title
     *            the title
     * @param head
     *            the head
     * @param body
     *            the body
     * @param frameSet
     *            the frame set
     */
    public FastPage(SitemeshBuffer sitemeshBuffer, Map sitemeshProps, Map htmlProps, Map metaProps, Map bodyProps,
            String title, String head, String body, boolean frameSet) {
        super(sitemeshBuffer);
        this.head = head;
        this.body = body;
        setFrameSet(frameSet);
        addAttributeList("", htmlProps);
        addAttributeList("page.", sitemeshProps);
        addAttributeList("body.", bodyProps);
        addAttributeList("meta.", metaProps);
        addProperty("title", title);
    }

    @Override
    public void writeHead(Writer out) throws IOException {
        out.write(head);
    }

    @Override
    public void writeBody(Writer out) throws IOException {
        out.write(body);
    }

    /**
     * Adds the attribute list.
     *
     * @param prefix
     *            the prefix
     * @param attributes
     *            the attributes
     */
    private void addAttributeList(String prefix, Map attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }

        String name, value;
        Iterator i = attributes.entrySet().iterator();

        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            name = (String) entry.getKey();
            value = (String) entry.getValue();

            if (value != null && value.trim().length() > 0) {
                addProperty(prefix + name, value);
            }
        }
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getHead() {
        return head;
    }
}
