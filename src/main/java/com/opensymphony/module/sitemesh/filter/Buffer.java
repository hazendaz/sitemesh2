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
/* This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file. */
package com.opensymphony.module.sitemesh.filter;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferWriter;
import com.opensymphony.module.sitemesh.util.FastByteArrayOutputStream;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * When SiteMesh is activated for a request, the contents of the response are stored in this buffer, where they can
 * later be accessed as a parsed Page object.
 *
 * @author Joe Walnes
 */
public class Buffer {

    /** The page parser. */
    private final PageParser pageParser;

    /** The encoding. */
    private final String encoding;

    /** The Constant TEXT_ENCODER. */
    private static final TextEncoder TEXT_ENCODER = new TextEncoder();

    /** The buffered writer. */
    private SitemeshBufferWriter bufferedWriter;

    /** The buffered stream. */
    private FastByteArrayOutputStream bufferedStream;

    /** The exposed writer. */
    private PrintWriter exposedWriter;

    /** The exposed stream. */
    private ServletOutputStream exposedStream;

    /**
     * Instantiates a new buffer.
     *
     * @param pageParser
     *            the page parser
     * @param encoding
     *            the encoding
     */
    public Buffer(PageParser pageParser, String encoding) {
        this.pageParser = pageParser;
        this.encoding = encoding;
    }

    /**
     * Gets the contents.
     *
     * @return the contents
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public SitemeshBuffer getContents() throws IOException {
        if (bufferedWriter != null) {
            return bufferedWriter.getSitemeshBuffer();
        }
        if (bufferedStream != null) {
            return new DefaultSitemeshBuffer(TEXT_ENCODER.encode(bufferedStream.toByteArray(), encoding));
        } else {
            return new DefaultSitemeshBuffer(new char[0]);
        }
    }

    /**
     * Parses the.
     *
     * @return the page
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Page parse() throws IOException {
        return pageParser.parse(getContents());
    }

    /**
     * Gets the writer.
     *
     * @return the writer
     */
    public PrintWriter getWriter() {
        if (bufferedWriter == null) {
            if (bufferedStream != null) {
                throw new IllegalStateException("response.getWriter() called after response.getOutputStream()");
            }
            bufferedWriter = new SitemeshBufferWriter(128);
            exposedWriter = new SitemeshPrintWriter(bufferedWriter);
        }
        return exposedWriter;
    }

    /**
     * Gets the output stream.
     *
     * @return the output stream
     */
    public ServletOutputStream getOutputStream() {
        if (bufferedStream == null) {
            if (bufferedWriter != null) {
                throw new IllegalStateException("response.getOutputStream() called after response.getWriter()");
            }
            bufferedStream = new FastByteArrayOutputStream();
            exposedStream = new ServletOutputStream() {
                @Override
                public void write(int b) {
                    bufferedStream.write(b);
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // TODO Not implemented
                }
            };
        }
        return exposedStream;
    }

    /**
     * Checks if is using stream.
     *
     * @return true, if is using stream
     */
    public boolean isUsingStream() {
        return bufferedStream != null;
    }
}
