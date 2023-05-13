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
/*
 * Title:        FastByteArrayOutputStream
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A speedy implementation of ByteArrayOutputStream. It's not synchronized, and it does not copy buffers when it's
 * expanded. There's also no copying of the internal buffer if it's contents is extracted with the writeTo(stream)
 * method.
 *
 * @author Rickard �berg
 * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
 */
public class FastByteArrayOutputStream extends ByteArrayOutputStream {

    /** The Constant DEFAULT_BLOCK_SIZE. */
    private static final int DEFAULT_BLOCK_SIZE = 8192;

    /** Internal buffer. */
    private byte[] buffer;

    /** The buffers. */
    private LinkedList<byte[]> buffers;

    /** The index. */
    private int index;

    /** The size. */
    private int size;

    /** The block size. */
    private int blockSize;

    /**
     * Instantiates a new fast byte array output stream.
     */
    public FastByteArrayOutputStream() {
        this(DEFAULT_BLOCK_SIZE);
    }

    /**
     * Instantiates a new fast byte array output stream.
     *
     * @param aSize
     *            the a size
     */
    public FastByteArrayOutputStream(int aSize) {
        blockSize = aSize;
        buffer = new byte[blockSize];
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        // check if we have a list of buffers
        if (buffers != null) {
            Iterator<byte[]> iterator = buffers.iterator();
            while (iterator.hasNext()) {
                byte[] bytes = (byte[]) iterator.next();
                out.write(bytes, 0, blockSize);
            }
        }

        // write the internal buffer directly
        out.write(buffer, 0, index);
    }

    @Override
    public int size() {
        return size + index;
    }

    @Override
    public byte[] toByteArray() {
        byte[] data = new byte[size()];

        // check if we have a list of buffers
        int pos = 0;
        if (buffers != null) {
            Iterator<byte[]> iterator = buffers.iterator();
            while (iterator.hasNext()) {
                byte[] bytes = (byte[]) iterator.next();
                System.arraycopy(bytes, 0, data, pos, blockSize);
                pos += blockSize;
            }
        }

        // write the internal buffer directly
        System.arraycopy(buffer, 0, data, pos, index);

        return data;
    }

    @Override
    public void write(int datum) {
        if (index == blockSize) {
            // Create new buffer and store current in linked list
            if (buffers == null) {
                buffers = new LinkedList<byte[]>();
            }

            buffers.addLast(buffer);

            buffer = new byte[blockSize];
            size += index;
            index = 0;
        }

        // store the byte
        buffer[index++] = (byte) datum;
    }

    @Override
    public void write(byte[] data, int offset, int length) {
        if (data == null) {
            throw new NullPointerException();
        }
        if (offset < 0 || offset + length > data.length || length < 0) {
            throw new IndexOutOfBoundsException();
        } else if (index + length >= blockSize) {
            // Write byte by byte
            // FIXME optimize this to use arraycopy's instead
            for (int i = 0; i < length; i++) {
                write(data[offset + i]);
            }
        } else {
            // copy in the subarray
            System.arraycopy(data, offset, buffer, index, length);
            index += length;
        }
    }

    @Override
    public synchronized void reset() {
        buffer = new byte[blockSize];
        buffers = null;
    }

    @Override
    public String toString(String enc) throws UnsupportedEncodingException {
        return new String(toByteArray(), Charset.forName(enc));
    }

    @Override
    public String toString() {
        return new String(toByteArray(), StandardCharsets.UTF_8);
    }

    @Override
    public void flush() throws IOException {
        // does nothing
    }

    @Override
    public void close() throws IOException {
        // does nothing
    }
}
