/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.SitemeshWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * The Class PartialPageParserPage.
 */
public class PartialPageParserPage extends AbstractPage {

    /** The body. */
    private final SitemeshBufferFragment body;

    /**
     * Instantiates a new partial page parser page.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     * @param body
     *            the body
     */
    public PartialPageParserPage(SitemeshBuffer sitemeshBuffer, SitemeshBufferFragment body) {
        super(sitemeshBuffer);
        this.body = body;
    }

    @Override
    public void writeBody(Writer out) throws IOException {
        if (out instanceof SitemeshWriter) {
            ((SitemeshWriter) out).writeSitemeshBufferFragment(body);
        } else {
            body.writeTo(out);
        }
    }
}
