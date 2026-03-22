/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.util;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

import java.io.IOException;
import java.io.Writer;

/**
 * SitemeshBuffer that is a string.
 */
public class StringSitemeshBuffer implements SitemeshBuffer {

    /** The buffer. */
    private final String buffer;

    /**
     * Instantiates a new string sitemesh buffer.
     *
     * @param buffer
     *            the buffer
     */
    public StringSitemeshBuffer(String buffer) {
        this.buffer = buffer;
    }

    @Override
    public char[] getCharArray() {
        return buffer.toCharArray();
    }

    @Override
    public int getBufferLength() {
        return buffer.length();
    }

    @Override
    public int getTotalLength() {
        return buffer.length();
    }

    @Override
    public int getTotalLength(int start, int length) {
        return length;
    }

    @Override
    public void writeTo(Writer writer, int start, int length) throws IOException {
        writer.write(buffer, start, length);
    }

    @Override
    public boolean hasFragments() {
        return false;
    }

    /**
     * Creates the buffer fragment.
     *
     * @param buffer
     *            the buffer
     *
     * @return the sitemesh buffer fragment
     */
    public static SitemeshBufferFragment createBufferFragment(String buffer) {
        return new SitemeshBufferFragment(new StringSitemeshBuffer(buffer), 0, buffer.length());
    }

}
