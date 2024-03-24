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
package com.opensymphony.module.sitemesh.html;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class CustomTagTest.
 */
class CustomTagTest {

    /**
     * Test writes out user defined tag.
     */
    @Test
    void writesOutUserDefinedTag() {
        Assertions.assertEquals("<hello/>", new CustomTag("hello", Tag.EMPTY).getContents());
        Assertions.assertEquals("<hello>", new CustomTag("hello", Tag.OPEN).getContents());
        Assertions.assertEquals("</hello>", new CustomTag("hello", Tag.CLOSE).getContents());
    }

    /**
     * Test writes attributes.
     */
    @Test
    void writesAttributes() {
        CustomTag tag = new CustomTag("hello", Tag.EMPTY);
        tag.addAttribute("color", "green");
        tag.addAttribute("stuff", null);
        Assertions.assertEquals("<hello color=\"green\" stuff/>", tag.getContents());
    }

    /**
     * Test allows attributes to be manipulated.
     */
    @Test
    void allowsAttributesToBeManipulated() {
        CustomTag tag = new CustomTag("hello", Tag.OPEN);
        Assertions.assertEquals("<hello>", tag.getContents());

        tag.addAttribute("a", "aaa");
        tag.addAttribute("b", "bbb");
        Assertions.assertEquals("<hello a=\"aaa\" b=\"bbb\">", tag.getContents());

        tag.removeAttribute("b", false);
        Assertions.assertEquals("<hello a=\"aaa\">", tag.getContents());

        tag.setAttributeValue("a", false, "zzz");
        Assertions.assertEquals("<hello a=\"zzz\">", tag.getContents());

        tag.addAttribute("c", "ccc");
        int index = tag.getAttributeIndex("c", true);
        Assertions.assertEquals(1, index);
        Assertions.assertEquals("ccc", tag.getAttributeValue(index));
        Assertions.assertEquals("c", tag.getAttributeName(index));
    }
}
