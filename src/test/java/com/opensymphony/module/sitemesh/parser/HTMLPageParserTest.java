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
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test case for HTMLPageParser implementations. See parser-tests/readme.txt.
 *
 * @author Joe Walnes
 */
public class HTMLPageParserTest extends TestCase {

    /** The Constant PARSER_PATTERN. */
    private static final Pattern PARSER_PATTERN = Pattern.compile("parser\\.(.+)\\.class");

    /**
     * This test case builds a custom suite, containing a collection of smaller suites (one for each file in
     * src/parser-tests).
     *
     * @return the test
     *
     * @throws Exception
     *             the exception
     */
    public static Test suite() throws Exception {
        TestSuite result = new TestSuite(HTMLPageParserTest.class.getName());

        Properties props = new Properties();
        props.load(new FileInputStream("src/parser-tests/parsers.properties"));

        Collection<String> parsers = new ArrayList<>();
        for (String key : props.stringPropertyNames()) {
            Matcher matcher = PARSER_PATTERN.matcher(key);
            if (matcher.matches()) {
                parsers.add(matcher.group(1));
            }
        }

        for (String p : parsers) {
            String name = props.getProperty("parser." + p + ".class");
            Class<? extends PageParser> parserClass = Class.forName(name).asSubclass(PageParser.class);
            PageParser parser = parserClass.newInstance();

            String filesPath = props.getProperty("parser." + p + ".tests", "src/parser-tests");
            List<File> files = new ArrayList<>(Arrays.asList(listParserTests(new File(filesPath))));
            Collections.sort(files);

            TestSuite suiteForParser = new TestSuite(name);
            for (File file : files) {
                TestSuite suiteForFile = new TestSuite(file.getName().replace('.', '_'));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testTitle"));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testBody"));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testHead"));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testFullPage"));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testProperties"));
                suiteForFile.addTest(new HTMLPageParserTest(parser, file, "testContentSanity"));
                suiteForParser.addTest(suiteForFile);
            }
            result.addTest(suiteForParser);
        }

        return result;
    }

    /** The page. */
    private Page page;

    /** The blocks. */
    private Map<Object, Object> blocks;

    /** The encoding. */
    private String encoding;

    /** The parser. */
    private final PageParser parser;

    /** The file. */
    private File file;

    /**
     * Instantiates a new HTML page parser test.
     *
     * @param parser
     *            the parser
     * @param inputFile
     *            the input file
     * @param test
     *            the test
     */
    public HTMLPageParserTest(PageParser parser, File inputFile, String test) {
        super(test);
        this.parser = parser;
        file = inputFile;
        encoding = "UTF8";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // read blocks from input file.
        this.blocks = readBlocks(new FileReader(file));
        // create PageParser and parse input block into HTMLPage object.
        String input = (String) blocks.get("INPUT");
        this.page = parser.parse(new DefaultSitemeshBuffer(input.toCharArray()));
    }

    /**
     * Test title.
     *
     * @throws Exception
     *             the exception
     */
    public void testTitle() throws Exception {
        assertBlock("TITLE", page.getTitle());
    }

    /**
     * Test body.
     *
     * @throws Exception
     *             the exception
     */
    public void testBody() throws Exception {
        StringWriter body = new StringWriter();
        page.writeBody(body);
        body.flush();
        assertBlock("BODY", body.toString());
    }

    /**
     * Test head.
     *
     * @throws Exception
     *             the exception
     */
    public void testHead() throws Exception {
        String head;
        if (page instanceof HTMLPage) {
            StringWriter headWriter = new StringWriter();
            ((HTMLPage) page).writeHead(headWriter);
            headWriter.flush();
            head = headWriter.toString();
        } else {
            head = "";
        }
        assertBlock("HEAD", head.toString());
    }

    /**
     * Test full page.
     *
     * @throws Exception
     *             the exception
     */
    public void testFullPage() throws Exception {
        StringWriter fullPage = new StringWriter();
        page.writePage(fullPage);
        fullPage.flush();
        assertBlock("INPUT", fullPage.toString());
    }

    /**
     * Test properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testProperties() throws Exception {
        Properties props = new Properties();
        String propsString = (String) blocks.get("PROPERTIES");
        ByteArrayInputStream input = new ByteArrayInputStream(propsString.trim().getBytes(Charset.forName(encoding)));
        props.load(input);

        String[] pageKeys = page.getPropertyKeys();
        assertEquals(file.getName() + " : Unexpected number of page properties [" + join(pageKeys) + "]", props.size(),
                pageKeys.length);

        for (String pageKey : pageKeys) {
            String blockValue = props.getProperty(pageKey);
            String pageValue = page.getProperty(pageKey);
            assertEquals(file.getName() + ": " + pageKey, blockValue == null ? null : blockValue.trim(),
                    pageValue == null ? null : pageValue.trim());
        }
    }

    /**
     * compare difference between using parse(char[]) and parse(char[], length).
     *
     * @throws Exception
     *             the exception
     */
    public void testContentSanity() throws Exception {
        String input = (String) blocks.get("INPUT");
        final char[] chars = input.toCharArray();
        final char[] bigChars = new char[chars.length * 2 + 10]; // make it bigger
        System.arraycopy(chars, 0, bigChars, 0, chars.length);
        Page bigPage = parser.parse(new DefaultSitemeshBuffer(bigChars, chars.length));

        assertEquals(bigPage.getPage(), page.getPage());
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

    // -------------------------------------------------

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
        // get list of files to ignore
        final List<String> ignoreFileNames = new ArrayList<>();
        String line;
        try (LineNumberReader ignoreReader = new LineNumberReader(new FileReader(new File(dir, "ignore.txt")))) {
            while ((line = ignoreReader.readLine()) != null) {
                ignoreFileNames.add(line);
            }
        }
        return dir.listFiles(
                (FilenameFilter) (currentDir, name) -> name.startsWith("test") && !ignoreFileNames.contains(name));
    }

    /**
     * Assert block.
     *
     * @param blockName
     *            the block name
     * @param result
     *            the result
     *
     * @throws Exception
     *             the exception
     */
    private void assertBlock(String blockName, String result) throws Exception {
        String expected = (String) blocks.get(blockName);
        assertEquals(file.getName() + " : Block did not match", expected.trim(), result.trim());
    }

    /**
     * Read input to test and break down into blocks. See parser-tests/readme.txt
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
