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
