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
package com.opensymphony.module.sitemesh;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.TreeMap;

/**
 * The default implementation of sitemesh buffer.
 */
public class DefaultSitemeshBuffer implements SitemeshBuffer {

    /** The buffer. */
    private final char[] buffer;

    /** The length. */
    private final int length;

    /** The buffer fragments. */
    private final TreeMap<Integer, SitemeshBufferFragment> bufferFragments;

    /**
     * Instantiates a new default sitemesh buffer.
     *
     * @param buffer
     *            the buffer
     */
    public DefaultSitemeshBuffer(char[] buffer) {
        this(buffer, buffer.length);
    }

    /**
     * Instantiates a new default sitemesh buffer.
     *
     * @param buffer
     *            the buffer
     * @param length
     *            the length
     */
    public DefaultSitemeshBuffer(char[] buffer, int length) {
        this(buffer, length, new TreeMap<Integer, SitemeshBufferFragment>());
    }

    /**
     * Instantiates a new default sitemesh buffer.
     *
     * @param buffer
     *            the buffer
     * @param length
     *            the length
     * @param bufferFragments
     *            the buffer fragments
     */
    public DefaultSitemeshBuffer(char[] buffer, int length, TreeMap<Integer, SitemeshBufferFragment> bufferFragments) {
        this.buffer = buffer;
        this.length = length;
        this.bufferFragments = bufferFragments;
    }

    @Override
    public void writeTo(Writer writer, int start, int length) throws IOException {
        int pos = start;
        for (Map.Entry<Integer, SitemeshBufferFragment> entry : bufferFragments.entrySet()) {
            int fragmentPosition = entry.getKey();
            if (fragmentPosition < pos) {
                continue;
            }
            if (fragmentPosition > start + length) {
                break;
            }
            // Write the buffer up to the fragment
            writer.write(buffer, pos, fragmentPosition - pos);
            // Write the fragment
            entry.getValue().writeTo(writer);
            // increment pos
            pos = fragmentPosition;
        }
        // Write out the remaining buffer
        if (pos < start + length) {
            writer.write(buffer, pos, start + length - pos);
        }
    }

    @Override
    public int getTotalLength() {
        return getTotalLength(0, length);
    }

    @Override
    public int getTotalLength(int start, int length) {
        int total = length;

        for (Map.Entry<Integer, SitemeshBufferFragment> entry : bufferFragments.entrySet()) {
            int fragmentPosition = entry.getKey();
            if (fragmentPosition < start) {
                continue;
            }
            if (fragmentPosition > start + length) {
                break;
            }
            total += entry.getValue().getTotalLength();
        }
        return total;
    }

    @Override
    public int getBufferLength() {
        return length;
    }

    @Override
    public char[] getCharArray() {
        return buffer;
    }

    @Override
    public boolean hasFragments() {
        return !bufferFragments.isEmpty();
    }

    /**
     * Builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     *
     * @return the builder
     */
    public static Builder builder(SitemeshBuffer sitemeshBuffer) {
        if (sitemeshBuffer instanceof DefaultSitemeshBuffer) {
            return new Builder((DefaultSitemeshBuffer) sitemeshBuffer);
        }
        return new Builder(sitemeshBuffer);
    }

    /**
     * The Class Builder.
     */
    public static class Builder {

        /** The buffer. */
        private char[] buffer;

        /** The length. */
        private int length;

        /** The fragments. */
        private final TreeMap<Integer, SitemeshBufferFragment> fragments;

        /**
         * Instantiates a new builder.
         */
        private Builder() {
            this.fragments = new TreeMap<>();
        }

        /**
         * Instantiates a new builder.
         *
         * @param buffer
         *            the buffer
         */
        private Builder(DefaultSitemeshBuffer buffer) {
            this.buffer = buffer.buffer;
            this.length = buffer.length;
            this.fragments = new TreeMap<>(buffer.bufferFragments);
        }

        /**
         * Instantiates a new builder.
         *
         * @param buffer
         *            the buffer
         */
        private Builder(SitemeshBuffer buffer) {
            this.buffer = buffer.getCharArray();
            this.length = buffer.getBufferLength();
            this.fragments = new TreeMap<>();
        }

        /**
         * Sets the buffer.
         *
         * @param buffer
         *            the buffer
         *
         * @return the builder
         */
        public Builder setBuffer(char[] buffer) {
            this.buffer = buffer;
            return this;
        }

        /**
         * Sets the length.
         *
         * @param length
         *            the length
         *
         * @return the builder
         */
        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        /**
         * Insert.
         *
         * @param position
         *            the position
         * @param fragment
         *            the fragment
         *
         * @return the builder
         */
        public Builder insert(int position, SitemeshBufferFragment fragment) {
            this.fragments.put(position, fragment);
            return this;
        }

        /**
         * Builds the.
         *
         * @return the sitemesh buffer
         */
        public SitemeshBuffer build() {
            return new DefaultSitemeshBuffer(buffer, length, fragments);
        }
    }
}
