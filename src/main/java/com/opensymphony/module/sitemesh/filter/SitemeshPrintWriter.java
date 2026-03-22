/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.filter;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.SitemeshBufferWriter;
import com.opensymphony.module.sitemesh.SitemeshWriter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A sitemesh print writer.
 */
public class SitemeshPrintWriter extends PrintWriter implements SitemeshWriter {

    /** The sitemesh writer. */
    private final SitemeshWriter sitemeshWriter;

    /**
     * Instantiates a new sitemesh print writer.
     *
     * @param sitemeshWriter
     *            the sitemesh writer
     */
    public SitemeshPrintWriter(SitemeshBufferWriter sitemeshWriter) {
        super(sitemeshWriter);
        this.sitemeshWriter = sitemeshWriter;
    }

    @Override
    public boolean writeSitemeshBufferFragment(SitemeshBufferFragment bufferFragment) throws IOException {
        return sitemeshWriter.writeSitemeshBufferFragment(bufferFragment);
    }

    @Override
    public SitemeshBuffer getSitemeshBuffer() {
        return sitemeshWriter.getSitemeshBuffer();
    }
}
