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
package com.opensymphony.module.sitemesh.html.util;

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

import java.io.IOException;
import java.io.Writer;

/**
 * SitemeshBuffer that is a string
 */
public class StringSitemeshBuffer implements SitemeshBuffer {
    private final String buffer;

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

    public static SitemeshBufferFragment createBufferFragment(String buffer) {
        return new SitemeshBufferFragment(new StringSitemeshBuffer(buffer), 0, buffer.length());
    }

}
