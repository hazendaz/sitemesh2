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
package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

/**
 * This performance test compares the three HTML parsers performance. It downloads a large real world HTML file (at time
 * of writing this file was 676KB and growing, this is a file that is generated using sitemesh), and then parses that
 * with each parser 1000 times over. Between each parse it does a System.gc(), to ensure garbage left over from the last
 * parser doesn't have to be collected during the next parsers run. And it runs each parser 3 times. This should ensure
 * that by the third time, all major JIT has been done, and so the system will be as close to a running production
 * server as possible.
 */
public class ParserPerformanceComparison {

    /** The Constant HTML_FILE. */
    private static final String HTML_FILE = "sitemesh-performance-test.html";

    /** The Constant HTML_URL. */
    private static final String HTML_URL = "https://jira.atlassian.com/browse/JRA-1330";

    /** The Constant PARSE_COUNT. */
    private static final int PARSE_COUNT = 1000;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     *
     * @throws Exception
     *             the exception
     */
    public static void main(String... args) throws Exception {
        // Download the file if it doesn't exist
        File file = new File(System.getProperty("java.io.tmpdir"), HTML_FILE);
        if (!file.exists()) {
            System.out.println("Downloading " + HTML_URL + " to use for performance test");
            URL url = new URL(HTML_URL);
            InputStream is = null;
            OutputStream os = null;
            try {
                is = url.openStream();
                os = new FileOutputStream(file);
                copy(is, os);
            } finally {
                closeQuietly(is);
                closeQuietly(os);
            }
        } else {
            System.out.println("Using cached file " + file);
        }
        // Read the cached file into a buffer
        CharArrayWriter writer = new CharArrayWriter();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            copy(reader, writer);
        } finally {
            closeQuietly(reader);
        }

        char[] page = writer.toCharArray();
        // Create the parsers
        PageParser normal = new HTMLPageParser();
        @SuppressWarnings("deprecation")
        PageParser fast = new FastPageParser();
        PageParser superfast = new PartialPageParser();

        System.out.println("Amount of data: " + page.length);
        System.gc();
        runPerformanceTest("Normal #1", page, normal, PARSE_COUNT);
        System.gc();
        runPerformanceTest("Fast #1", page, fast, PARSE_COUNT);
        System.gc();
        runPerformanceTest("Super Fast #1", page, superfast, PARSE_COUNT);
        System.gc();
        runPerformanceTest("Normal #2", page, normal, PARSE_COUNT);
        System.gc();
        runPerformanceTest("Fast #2", page, fast, PARSE_COUNT);
        System.gc();
        runPerformanceTest("Super Fast #2", page, superfast, PARSE_COUNT);
        System.gc();
        double normalTime = runPerformanceTest("Normal #3", page, normal, PARSE_COUNT);
        System.gc();
        double fastTime = runPerformanceTest("Fast #3", page, fast, PARSE_COUNT);
        System.gc();
        double superfastTime = runPerformanceTest("Super Fast #3", page, superfast, PARSE_COUNT);

        System.out.println("\nPerformance comparison %\n========================");
        System.out.println(String.format("%-10s%12s%12s%12s", "", "Normal", "Fast", "Super Fast"));
        System.out.println(String.format("%-10s%12s% 11.1f%%% 11.1f%%", "Normal", "X", normalTime / fastTime * 100,
                normalTime / superfastTime * 100));
        System.out.println(String.format("%-10s% 11.1f%%%12s% 11.1f%%", "Fast", fastTime / normalTime * 100, "X",
                fastTime / superfastTime * 100));
        System.out.println(String.format("%-10s% 11.1f%%% 11.1f%%%12s", "Super Fast", superfastTime / normalTime * 100,
                superfastTime / fastTime * 100, "X"));
    }

    /**
     * Run performance test.
     *
     * @param name
     *            the name
     * @param data
     *            the data
     * @param parser
     *            the parser
     * @param times
     *            the times
     *
     * @return the long
     *
     * @throws Exception
     *             the exception
     */
    public static long runPerformanceTest(String name, char[] data, PageParser parser, int times) throws Exception {
        Writer writer = new NullWriter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Page page = parser.parse(new DefaultSitemeshBuffer(data));
            page.writeBody(writer);
        }
        long finish = System.currentTimeMillis();
        long time = finish - start;
        System.out.println(name + " total: " + time + "ms");
        System.out.println(name + " average: " + (double) time / (double) times + "ms");
        return time;
    }

    /**
     * The Class NullWriter.
     */
    private static class NullWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            // do nothnig
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }

    /**
     * Copy.
     *
     * @param is
     *            the is
     * @param os
     *            the os
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[4096];
        int length;
        while ((length = is.read(buf)) > 0) {
            os.write(buf, 0, length);
        }
    }

    /**
     * Copy.
     *
     * @param reader
     *            the reader
     * @param writer
     *            the writer
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static void copy(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[4096];
        int length;
        while ((length = reader.read(buf)) > 0) {
            writer.write(buf, 0, length);
        }
    }

    /**
     * Close quietly.
     *
     * @param closeable
     *            the closeable
     */
    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // Ignore
        }
    }
}
