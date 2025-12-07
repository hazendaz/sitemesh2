/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2025 Hazendaz.
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
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test case for HTMLPageParser implementations. See parser-tests/readme.txt.
 *
 * @author Joe Walnes
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HTMLPageParserTest {

    /** The Constant PARSER_PATTERN. */
    private static final Pattern PARSER_PATTERN = Pattern.compile("parser\\.(.+)\\.class");

    /** The page. */
    private Page page;

    /** The blocks. */
    private Map<Object, Object> blocks;

    /** The encoding. */
    private String encoding;

    /** The parser. */
    private PageParser parser;

    /** The file. */
    private File file;

    /**
     * The Class TestParams.
     */
    static class TestParams {

        /** The parser. */
        final PageParser parser;

        /** The file. */
        final File file;

        /** The encoding. */
        final String encoding;

        /**
         * Instantiates a new test params.
         *
         * @param parser
         *            the parser
         * @param file
         *            the file
         * @param encoding
         *            the encoding
         */
        TestParams(PageParser parser, File file, String encoding) {
            this.parser = parser;
            this.file = file;
            this.encoding = encoding;
        }
    }

    /**
     * Test params provider.
     *
     * @return the stream
     *
     * @throws Exception
     *             the exception
     */
    static Stream<TestParams> testParamsProvider() throws Exception {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("src/parser-tests/parsers.properties")));
        Collection<String> parsers = new ArrayList<>();
        for (String key : props.stringPropertyNames()) {
            Matcher matcher = PARSER_PATTERN.matcher(key);
            if (matcher.matches()) {
                parsers.add(matcher.group(1));
            }
        }
        List<TestParams> params = new ArrayList<>();
        for (String p : parsers) {
            String name = props.getProperty("parser." + p + ".class");
            Class<? extends PageParser> parserClass = Class.forName(name).asSubclass(PageParser.class);
            PageParser parser = parserClass.getDeclaredConstructor().newInstance();
            String filesPath = props.getProperty("parser." + p + ".tests", "src/parser-tests");
            List<File> files = new ArrayList<>(Arrays.asList(listParserTests(Path.of(filesPath).toFile())));
            Collections.sort(files);
            for (File file : files) {
                params.add(new TestParams(parser, file, "UTF8"));
            }
        }
        return params.stream();
    }

    /**
     * Inits the.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    void init(TestParams params) throws Exception {
        this.parser = params.parser;
        this.file = params.file;
        this.encoding = params.encoding;
        this.blocks = readBlocks(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8));
        String input = (String) blocks.get("INPUT");
        this.page = parser.parse(new DefaultSitemeshBuffer(input.toCharArray()));
    }

    /**
     * Test title.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testTitle")
    @MethodSource("testParamsProvider")
    void testTitle(TestParams params) throws Exception {
        init(params);
        Assertions.assertEquals(((String) blocks.get("TITLE")).trim(), page.getTitle().trim(),
                file.getName() + " : Block did not match");
    }

    /**
     * Test body.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testBody")
    @MethodSource("testParamsProvider")
    void testBody(TestParams params) throws Exception {
        init(params);
        StringWriter body = new StringWriter();
        page.writeBody(body);
        body.flush();
        Assertions.assertEquals(((String) blocks.get("BODY")).trim(), body.toString().trim(),
                file.getName() + " : Block did not match");
    }

    /**
     * Test head.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testHead")
    @MethodSource("testParamsProvider")
    void testHead(TestParams params) throws Exception {
        init(params);
        String head;
        if (page instanceof HTMLPage) {
            StringWriter headWriter = new StringWriter();
            ((HTMLPage) page).writeHead(headWriter);
            headWriter.flush();
            head = headWriter.toString();
        } else {
            head = "";
        }
        Assertions.assertEquals(((String) blocks.get("HEAD")).trim(), head.trim(),
                file.getName() + " : Block did not match");
    }

    /**
     * Test full page.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testFullPage")
    @MethodSource("testParamsProvider")
    void testFullPage(TestParams params) throws Exception {
        init(params);
        StringWriter fullPage = new StringWriter();
        page.writePage(fullPage);
        fullPage.flush();
        Assertions.assertEquals(((String) blocks.get("INPUT")).trim(), fullPage.toString().trim(),
                file.getName() + " : Block did not match");
    }

    /**
     * Test properties.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testProperties")
    @MethodSource("testParamsProvider")
    void testProperties(TestParams params) throws Exception {
        init(params);
        Properties props = new Properties();
        String propsString = (String) blocks.get("PROPERTIES");
        ByteArrayInputStream input = new ByteArrayInputStream(propsString.trim().getBytes(Charset.forName(encoding)));
        props.load(input);
        String[] pageKeys = page.getPropertyKeys();
        Assertions.assertEquals(props.size(), pageKeys.length,
                file.getName() + " : Unexpected number of page properties [" + join(pageKeys) + "]");
        for (String pageKey : pageKeys) {
            String blockValue = props.getProperty(pageKey);
            String pageValue = page.getProperty(pageKey);
            Assertions.assertEquals(blockValue == null ? null : blockValue.trim(),
                    pageValue == null ? null : pageValue.trim(), file.getName() + ": " + pageKey);
        }
    }

    /**
     * Test content sanity.
     *
     * @param params
     *            the params
     *
     * @throws Exception
     *             the exception
     */
    @ParameterizedTest(name = "{1} - testContentSanity")
    @MethodSource("testParamsProvider")
    void testContentSanity(TestParams params) throws Exception {
        init(params);
        String input = (String) blocks.get("INPUT");
        final char[] chars = input.toCharArray();
        final char[] bigChars = new char[chars.length * 2 + 10];
        System.arraycopy(chars, 0, bigChars, 0, chars.length);
        Page bigPage = parser.parse(new DefaultSitemeshBuffer(bigChars, chars.length));
        Assertions.assertEquals(bigPage.getPage(), page.getPage());
    }

    /**
     * Join.
     *
     * @param values
     *            the values
     *
     * @return the string
     */
    private String join(String[] values) {
        return String.join(",", values);
    }

    /**
     * List parser tests.
     *
     * @param dir
     *            the dir
     *
     * @return the file[]
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static File[] listParserTests(File dir) throws IOException {
        final List<String> ignoreFileNames = new ArrayList<>();
        String line;
        try (LineNumberReader ignoreReader = new LineNumberReader(
                Files.newBufferedReader(dir.toPath().resolve("ignore.txt"), StandardCharsets.UTF_8))) {
            while ((line = ignoreReader.readLine()) != null) {
                ignoreFileNames.add(line);
            }
        }
        return dir.listFiles(
                (FilenameFilter) (currentDir, name) -> name.startsWith("test") && !ignoreFileNames.contains(name));
    }

    /**
     * Read blocks.
     *
     * @param input
     *            the input
     *
     * @return the map
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private Map<Object, Object> readBlocks(Reader input) throws IOException {
        Map<Object, Object> blocks = new HashMap<>();
        LineNumberReader reader = new LineNumberReader(input);
        String line;
        String blockName = null;
        StringBuilder blockContents = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("~~~ ") && line.endsWith(" ~~~")) {
                if (blockName != null) {
                    blocks.put(blockName, blockContents.toString());
                }
                blockName = line.substring(4, line.length() - 4);
                blockContents = new StringBuilder();
            } else if (blockName != null) {
                blockContents.append(line);
                blockContents.append('\n');
            }
        }
        if (blockName != null) {
            blocks.put(blockName, blockContents.toString());
        }
        return blocks;
    }

}
