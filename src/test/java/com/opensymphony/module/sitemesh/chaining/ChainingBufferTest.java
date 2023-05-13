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
package com.opensymphony.module.sitemesh.chaining;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.SitemeshBufferWriter;

import java.io.CharArrayWriter;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class ChainingBufferTest.
 */
public class ChainingBufferTest {

    /**
     * Test simple chain.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testSimpleChain() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("1234", 2, newBufferFragment("ab"));
        Assert.assertEquals("12ab34", getContent(buffer));
        assertCorrectLength(buffer);
    }

    /**
     * Test before.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testBefore() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("1234", 2, newBufferFragment("ab"));
        Assert.assertEquals("1", getContent(buffer, 0, 1));
        Assert.assertEquals("12ab", getContent(buffer, 0, 2));
    }

    /**
     * Test after.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testAfter() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("1234", 2, newBufferFragment("ab"));
        Assert.assertEquals("ab34", getContent(buffer, 2, 2));
        Assert.assertEquals("4", getContent(buffer, 3, 1));
    }

    /**
     * Test fragment.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFragment() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("1234", 2, newBufferFragment("abcd", 1, 2));
        Assert.assertEquals("12bc34", getContent(buffer));
        assertCorrectLength(buffer);
    }

    /**
     * Test deep fragments.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDeepFragments() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("123456789", 3,
                newBufferFragment("abcdefg", 4, newBufferFragment("hijklm", 1, 1), 5, newBufferFragment("nopqr", 1, 4)),
                8, newBufferFragment("tuzwx", 0, 2));
        Assert.assertEquals("123abcdieopqrfg45678tu9", getContent(buffer));
        assertCorrectLength(buffer);
    }

    /**
     * Test writer.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testWriter() throws Exception {
        SitemeshBuffer buffer = newSitemeshBuffer("123456");
        try (SitemeshBufferWriter writer = new SitemeshBufferWriter()) {
            writer.write("abc");
            writer.writeSitemeshBufferFragment(new SitemeshBufferFragment(buffer, 1, 4));
            writer.write("def");
            Assert.assertEquals("abcdef", writer.toString());
            Assert.assertEquals("abc2345def", getContent(writer.getSitemeshBuffer()));
        }
    }

    /**
     * Gets the content.
     *
     * @param buffer
     *            the buffer
     *
     * @return the content
     *
     * @throws Exception
     *             the exception
     */
    private String getContent(SitemeshBuffer buffer) throws Exception {
        CharArrayWriter writer = new CharArrayWriter();
        buffer.writeTo(writer, 0, buffer.getBufferLength());
        return writer.toString();
    }

    /**
     * Gets the content.
     *
     * @param buffer
     *            the buffer
     * @param start
     *            the start
     * @param length
     *            the length
     *
     * @return the content
     *
     * @throws Exception
     *             the exception
     */
    private String getContent(SitemeshBuffer buffer, int start, int length) throws Exception {
        CharArrayWriter writer = new CharArrayWriter();
        buffer.writeTo(writer, start, length);
        return writer.toString();
    }

    /**
     * Assert correct length.
     *
     * @param buffer
     *            the buffer
     *
     * @throws Exception
     *             the exception
     */
    private void assertCorrectLength(SitemeshBuffer buffer) throws Exception {
        Assert.assertEquals(getContent(buffer).length(), buffer.getTotalLength());
    }

    /**
     * New sitemesh buffer.
     *
     * @param content
     *            the content
     *
     * @return the sitemesh buffer
     */
    private SitemeshBuffer newSitemeshBuffer(String content) {
        return new DefaultSitemeshBuffer(content.toCharArray(), content.length());
    }

    /**
     * New sitemesh buffer.
     *
     * @param content
     *            the content
     * @param pos1
     *            the pos 1
     * @param frag1
     *            the frag 1
     *
     * @return the sitemesh buffer
     */
    private SitemeshBuffer newSitemeshBuffer(String content, int pos1, SitemeshBufferFragment frag1) {
        TreeMap<Integer, SitemeshBufferFragment> fragments = new TreeMap<>();
        fragments.put(pos1, frag1);
        return new DefaultSitemeshBuffer(content.toCharArray(), content.length(), fragments);
    }

    /**
     * New sitemesh buffer.
     *
     * @param content
     *            the content
     * @param pos1
     *            the pos 1
     * @param frag1
     *            the frag 1
     * @param pos2
     *            the pos 2
     * @param frag2
     *            the frag 2
     *
     * @return the sitemesh buffer
     */
    private SitemeshBuffer newSitemeshBuffer(String content, int pos1, SitemeshBufferFragment frag1, int pos2,
            SitemeshBufferFragment frag2) {
        TreeMap<Integer, SitemeshBufferFragment> fragments = new TreeMap<>();
        fragments.put(pos1, frag1);
        fragments.put(pos2, frag2);
        return new DefaultSitemeshBuffer(content.toCharArray(), content.length(), fragments);
    }

    /**
     * New buffer fragment.
     *
     * @param content
     *            the content
     *
     * @return the sitemesh buffer fragment
     */
    private SitemeshBufferFragment newBufferFragment(String content) {
        return new SitemeshBufferFragment(newSitemeshBuffer(content), 0, content.length());
    }

    /**
     * New buffer fragment.
     *
     * @param content
     *            the content
     * @param start
     *            the start
     * @param length
     *            the length
     *
     * @return the sitemesh buffer fragment
     */
    private SitemeshBufferFragment newBufferFragment(String content, int start, int length) {
        return new SitemeshBufferFragment(newSitemeshBuffer(content), start, length);
    }

    /**
     * New buffer fragment.
     *
     * @param content
     *            the content
     * @param pos1
     *            the pos 1
     * @param frag1
     *            the frag 1
     * @param pos2
     *            the pos 2
     * @param frag2
     *            the frag 2
     *
     * @return the sitemesh buffer fragment
     */
    private SitemeshBufferFragment newBufferFragment(String content, int pos1, SitemeshBufferFragment frag1, int pos2,
            SitemeshBufferFragment frag2) {
        return new SitemeshBufferFragment(newSitemeshBuffer(content, pos1, frag1, pos2, frag2), 0, content.length());
    }
}
