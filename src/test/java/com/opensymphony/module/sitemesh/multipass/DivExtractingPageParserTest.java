/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2024 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     Hazendaz (Jeremy Landis).
 */
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class DivExtractingPageParserTest.
 *
 * @author Joe Walnes
 */
class DivExtractingPageParserTest {

    /**
     * Test replaces top level divs with place holders.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void replacesTopLevelDivsWithPlaceHolders() throws IOException {
        String input = "" + "<html>\n" + "  <head><title>Title</title></head>\n" + "  <body>\n"
                + "    <div id='one'>Hello</div>\n" + "    Blah\n"
                + "    <div id='two'>World<br><div id=inner>Great</div></div>\n" + "    <div>Bye</div>\n"
                + "  </body>\n" + "</html>";

        PageParser parser = new DivExtractingPageParser();
        Page page = parser.parse(new DefaultSitemeshBuffer(input.toCharArray()));

        String expectedBody = "" + "    <sitemesh:multipass id=\"div.one\"/>\n" + "    Blah\n"
                + "    <sitemesh:multipass id=\"div.two\"/>\n" + "    <div>Bye</div>\n";
        Assertions.assertEquals("Title", page.getTitle());
        Assertions.assertEquals(expectedBody.trim(), page.getBody().trim());
        Assertions.assertEquals("<div id='one'>Hello</div>", page.getProperty("div.one"));
        Assertions.assertEquals("<div id='two'>World<br><div id=inner>Great</div></div>", page.getProperty("div.two"));
    }

    /**
     * Test extract attributes.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void extractAttributes() throws IOException {
        String input = "" + "<html>\n" + "  <head><title>Title</title></head>\n" + "  <body>\n"
                + "    <div id='one' class='c_one' align='center'>Hello</div>\n" + "    Blah\n"
                + "    <div id='two'>World<br><div id=inner>Great</div></div>\n" + "    <div>Bye</div>\n"
                + "  </body>\n" + "</html>";

        PageParser parser = new DivExtractingPageParser();
        Page page = parser.parse(input.toCharArray());

        Assertions.assertEquals("c_one", page.getProperty("div.one.class"));
        Assertions.assertEquals("center", page.getProperty("div.one.align"));
        Assertions.assertEquals("two", page.getProperty("div.two.id"));
    }
}
