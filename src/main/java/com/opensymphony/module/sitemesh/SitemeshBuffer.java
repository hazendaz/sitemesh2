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

/**
 * A potentially chained sitemesh buffer.
 */
public interface SitemeshBuffer {

    /**
     * Get the char array for this buffer. This array may be longer than the length of the content, you must use
     * getBufferLength() in combination with this method.
     *
     * @return The char array for this buffer
     */
    char[] getCharArray();

    /**
     * Get the length of the buffered content.
     *
     * @return The length of the buffered content.
     */
    int getBufferLength();

    /**
     * Get the total length of the buffered content, including the length of any chained buffers.
     *
     * @return The total length.
     */
    int getTotalLength();

    /**
     * Get the total length of the buffered content, including chained buffers from start to length.
     *
     * @param start
     *            Where to start counting the length from
     * @param length
     *            Where to finish
     *
     * @return THe total length in the given range
     */
    int getTotalLength(int start, int length);

    /**
     * Write this buffer, and any chained sub buffers in the given range, out to the given writer.
     *
     * @param writer
     *            The writer to write to
     * @param start
     *            The position to start writing from
     * @param length
     *            The length to write
     *
     * @throws IOException
     *             If an error occurred
     */
    void writeTo(Writer writer, int start, int length) throws IOException;

    /**
     * Whether the buffer has fragments or not.
     *
     * @return True if it has fragments
     */
    boolean hasFragments();
}
