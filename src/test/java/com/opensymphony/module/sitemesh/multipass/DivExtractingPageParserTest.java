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
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class DivExtractingPageParserTest.
 *
 * @author Joe Walnes
 */
public class DivExtractingPageParserTest {

    /**
     * Test replaces top level divs with place holders.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testReplacesTopLevelDivsWithPlaceHolders() throws IOException {
        String input = "" + "<html>\n" + "  <head><title>Title</title></head>\n" + "  <body>\n"
                + "    <div id='one'>Hello</div>\n" + "    Blah\n"
                + "    <div id='two'>World<br><div id=inner>Great</div></div>\n" + "    <div>Bye</div>\n"
                + "  </body>\n" + "</html>";

        PageParser parser = new DivExtractingPageParser();
        Page page = parser.parse(new DefaultSitemeshBuffer(input.toCharArray()));

        String expectedBody = "" + "    <sitemesh:multipass id=\"div.one\"/>\n" + "    Blah\n"
                + "    <sitemesh:multipass id=\"div.two\"/>\n" + "    <div>Bye</div>\n";
        Assert.assertEquals("Title", page.getTitle());
        Assert.assertEquals(expectedBody.trim(), page.getBody().trim());
        Assert.assertEquals("<div id='one'>Hello</div>", page.getProperty("div.one"));
        Assert.assertEquals("<div id='two'>World<br><div id=inner>Great</div></div>", page.getProperty("div.two"));
    }

    /**
     * Test extract attributes.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testExtractAttributes() throws IOException {
        String input = "" + "<html>\n" + "  <head><title>Title</title></head>\n" + "  <body>\n"
                + "    <div id='one' class='c_one' align='center'>Hello</div>\n" + "    Blah\n"
                + "    <div id='two'>World<br><div id=inner>Great</div></div>\n" + "    <div>Bye</div>\n"
                + "  </body>\n" + "</html>";

        PageParser parser = new DivExtractingPageParser();
        Page page = parser.parse(input.toCharArray());

        Assert.assertEquals("c_one", page.getProperty("div.one.class"));
        Assert.assertEquals("center", page.getProperty("div.one.align"));
        Assert.assertEquals("two", page.getProperty("div.two.id"));
    }
}
