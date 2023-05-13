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
package com.opensymphony.module.sitemesh.html;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class CustomTagTest.
 */
public class CustomTagTest {

    /**
     * Test writes out user defined tag.
     */
    @Test
    public void testWritesOutUserDefinedTag() {
        Assert.assertEquals("<hello/>", new CustomTag("hello", Tag.EMPTY).getContents());
        Assert.assertEquals("<hello>", new CustomTag("hello", Tag.OPEN).getContents());
        Assert.assertEquals("</hello>", new CustomTag("hello", Tag.CLOSE).getContents());
    }

    /**
     * Test writes attributes.
     */
    @Test
    public void testWritesAttributes() {
        CustomTag tag = new CustomTag("hello", Tag.EMPTY);
        tag.addAttribute("color", "green");
        tag.addAttribute("stuff", null);
        Assert.assertEquals("<hello color=\"green\" stuff/>", tag.getContents());
    }

    /**
     * Test allows attributes to be manipulated.
     */
    @Test
    public void testAllowsAttributesToBeManipulated() {
        CustomTag tag = new CustomTag("hello", Tag.OPEN);
        Assert.assertEquals("<hello>", tag.getContents());

        tag.addAttribute("a", "aaa");
        tag.addAttribute("b", "bbb");
        Assert.assertEquals("<hello a=\"aaa\" b=\"bbb\">", tag.getContents());

        tag.removeAttribute("b", false);
        Assert.assertEquals("<hello a=\"aaa\">", tag.getContents());

        tag.setAttributeValue("a", false, "zzz");
        Assert.assertEquals("<hello a=\"zzz\">", tag.getContents());

        tag.addAttribute("c", "ccc");
        int index = tag.getAttributeIndex("c", true);
        Assert.assertEquals(1, index);
        Assert.assertEquals("ccc", tag.getAttributeValue(index));
        Assert.assertEquals("c", tag.getAttributeName(index));
    }
}
