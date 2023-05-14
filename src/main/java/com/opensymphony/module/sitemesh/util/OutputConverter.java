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
package com.opensymphony.module.sitemesh.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A converter from one character type to another
 * <p>
 * This class is not threadsafe.
 */
public class OutputConverter {

    /** The Logger. */
    private static final Logger logger = LoggerFactory.getLogger(OutputConverter.class);

    /**
     * Resin versions 2.1.12 and previously have encoding problems for internationalization.
     * <p>
     * This is fixed in Resin 2.1.13. Once 2.1.13 is used more widely, this parameter (and class) can be removed.
     */
    public static final String WORK_AROUND_RESIN_I18N_BUG = "sitemesh.resin.i18n.workaround";

    /**
     * Gets the writer.
     *
     * @param out
     *            the out
     *
     * @return the writer
     */
    public static Writer getWriter(Writer out) {
        if (Boolean.getBoolean(WORK_AROUND_RESIN_I18N_BUG)) {
            return new ResinWriter(out);
        }
        return out;
    }

    /**
     * Convert.
     *
     * @param inputString
     *            the input string
     *
     * @return the string
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String convert(String inputString) {
        if (Boolean.getBoolean(WORK_AROUND_RESIN_I18N_BUG)) {
            StringWriter sr = new StringWriter();
            resinConvert(inputString, sr);
            return sr.getBuffer().toString();
        }
        return inputString;
    }

    /**
     * To get internationalized characters to work on Resin, some conversions need to take place.
     */
    static class ResinWriter extends Writer {

        /** The target. */
        private final Writer target;

        /** The buffer. */
        private final CharArrayWriter buffer = new CharArrayWriter();

        /**
         * Instantiates a new resin writer.
         *
         * @param target
         *            the target
         */
        public ResinWriter(Writer target) {
            this.target = target;
        }

        @Override
        public void close() throws IOException {
            flush();
        }

        @Override
        public void flush() throws IOException {
            resinConvert(buffer.toString(), target);
            buffer.reset();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            buffer.write(cbuf, off, len);
            flush();
        }
    }

    /**
     * Resin convert.
     *
     * @param inputString
     *            the input string
     * @param writer
     *            the writer
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void resinConvert(String inputString, Writer writer) {
        // does this need to be made configurable? Or are these two always correct?
        try (InputStreamReader reader = new InputStreamReader(
                new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.ISO_8859_1)) {
            int i;
            while ((i = reader.read()) != -1) {
                writer.write(i);
            }
        } catch (IOException e) {
            logger.error("Unable to perform resinConvert", e);
        }
    }

}
