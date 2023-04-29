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
 * @see com.opensymphony.module.sitemesh.parser.HTMLPageParser
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer
 *
 * @author Joe Walnes
 */
public class TokenizedHTMLPage extends AbstractHTMLPage implements PageBuilder {

    private SitemeshBufferFragment body;
    private SitemeshBufferFragment head;

    public TokenizedHTMLPage(SitemeshBuffer sitemeshBuffer) {
        super(sitemeshBuffer);
        addProperty("title", "");
    }

    public void setBody(SitemeshBufferFragment body) {
        this.body = body;
    }

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
