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
package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.SitemeshWriter;
import com.opensymphony.module.sitemesh.html.rules.PageBuilder;

import java.io.IOException;
import java.io.Writer;

/**
 * HTMLPage implementation that builds itself based on the incoming 'tag' and 'text' tokens fed to it from the
 * HTMLTagTokenizer.
 *
 * @author Joe Walnes
 *
 * @see com.opensymphony.module.sitemesh.parser.HTMLPageParser
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer
 */
public class TokenizedHTMLPage extends AbstractHTMLPage implements PageBuilder {

    /** The body. */
    private SitemeshBufferFragment body;

    /** The head. */
    private SitemeshBufferFragment head;

    /**
     * Instantiates a new tokenized HTML page.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     */
    public TokenizedHTMLPage(SitemeshBuffer sitemeshBuffer) {
        super(sitemeshBuffer);
        addProperty("title", "");
    }

    /**
     * Sets the body.
     *
     * @param body
     *            the new body
     */
    public void setBody(SitemeshBufferFragment body) {
        this.body = body;
    }

    /**
     * Sets the head.
     *
     * @param head
     *            the new head
     */
    public void setHead(SitemeshBufferFragment head) {
        this.head = head;
    }

    public void writeHead(Writer out) throws IOException {
        if (out instanceof SitemeshWriter) {
            ((SitemeshWriter) out).writeSitemeshBufferFragment(head);
        } else {
            head.writeTo(out);
        }
    }

    public void writeBody(Writer out) throws IOException {
        if (out instanceof SitemeshWriter) {
            ((SitemeshWriter) out).writeSitemeshBufferFragment(body);
        } else {
            body.writeTo(out);
        }
    }

    public String getHead() {
        return head.getStringContent();
    }

    public String getBody() {
        return body.getStringContent();
    }
}
