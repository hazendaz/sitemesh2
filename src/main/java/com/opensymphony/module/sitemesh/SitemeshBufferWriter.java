/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh;

import com.opensymphony.module.sitemesh.util.CharArrayWriter;

import java.io.IOException;
import java.util.TreeMap;

/**
 * A char array writer that caches other sitemesh buffers written to it, so that it doesn't have to continually copy
 * them from buffer to buffer.
 */
public class SitemeshBufferWriter extends CharArrayWriter implements SitemeshWriter {

    /** The fragments. */
    private final TreeMap<Integer, SitemeshBufferFragment> fragments = new TreeMap<>();

    /**
     * Instantiates a new sitemesh buffer writer.
     */
    public SitemeshBufferWriter() {
    }

    /**
     * Instantiates a new sitemesh buffer writer.
     *
     * @param initialSize
     *            the initial size
     */
    public SitemeshBufferWriter(int initialSize) {
        super(initialSize);
    }

    @Override
    public boolean writeSitemeshBufferFragment(SitemeshBufferFragment bufferFragment) throws IOException {
        fragments.put(count, bufferFragment);
        return false;
    }

    @Override
    public SitemeshBuffer getSitemeshBuffer() {
        return new DefaultSitemeshBuffer(buf, count, fragments);
    }
}
