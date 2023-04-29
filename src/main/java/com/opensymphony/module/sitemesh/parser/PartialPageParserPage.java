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
