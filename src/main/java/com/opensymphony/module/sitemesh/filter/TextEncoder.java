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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * Converts text stored in byte[] to char[] using specified encoding.
 *
 * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @author Joe Walnes
 */
public class TextEncoder {

    /** The Constant DEFAULT_ENCODING. */
    private static final String DEFAULT_ENCODING = Charset.defaultCharset().displayName();

    /**
     * Encode.
     *
     * @param data
     *            the data
     * @param encoding
     *            the encoding
     *
     * @return the char[]
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public char[] encode(byte[] data, String encoding) throws IOException {
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return getBuffer(data, encoding);
    }

    /**
     * Gets the buffer.
     *
     * @param data
     *            the data
     * @param encoding
     *            the encoding
     *
     * @return the buffer
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private char[] getBuffer(byte[] data, String encoding) throws IOException {
        if (!Charset.isSupported(encoding)) {
            throw new IOException("Unsupported encoding " + encoding);
        }
        Charset charset = Charset.forName(encoding);
        CharsetDecoder cd = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
        int en = (int) (cd.maxCharsPerByte() * data.length);
        char[] ca = new char[en];
        ByteBuffer bb = ByteBuffer.wrap(data);
        CharBuffer cb = CharBuffer.wrap(ca);
        CoderResult cr = cd.decode(bb, cb, true);
        if (!cr.isUnderflow()) {
            cr.throwException();
        }
        cr = cd.flush(cb);
        if (!cr.isUnderflow()) {
            cr.throwException();
        }
        return trim(ca, cb.position());
    }

    /**
     * Trim.
     *
     * @param ca
     *            the ca
     * @param len
     *            the len
     *
     * @return the char[]
     */
    private char[] trim(char[] ca, int len) {
        if (len == ca.length) {
            return ca;
        }
        char[] tca = new char[len];
        System.arraycopy(ca, 0, tca, 0, len);
        return tca;
    }

}
