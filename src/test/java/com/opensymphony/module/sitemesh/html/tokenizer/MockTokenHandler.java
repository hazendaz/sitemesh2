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
package com.opensymphony.module.sitemesh.html.tokenizer;

import com.opensymphony.module.sitemesh.html.Tag;
import com.opensymphony.module.sitemesh.html.Text;

import org.junit.Assert;

/**
 * The Class MockTokenHandler.
 */
class MockTokenHandler implements TokenHandler {

    /** The expected. */
    private StringBuilder expected = new StringBuilder();

    /** The actual. */
    private StringBuilder actual = new StringBuilder();

    /**
     * Expect text.
     *
     * @param tag
     *            the tag
     */
    public void expectText(String tag) {
        expected.append(tag);
    }

    /**
     * Expect tag.
     *
     * @param type
     *            the type
     * @param tag
     *            the tag
     */
    public void expectTag(int type, String tag) {
        expectTag(type, tag, new String[0]);
    }

    /**
     * Expect tag.
     *
     * @param type
     *            the type
     * @param tag
     *            the tag
     * @param attributes
     *            the attributes
     */
    public void expectTag(int type, String tag, String[] attributes) {
        expected.append("{{TAG : ").append(tag);
        for (int i = 0; i < attributes.length; i += 2) {
            expected.append(' ').append(attributes[i]).append("=\"").append(attributes[i + 1]).append('"');
        }
        expected.append(' ').append(typeAsString(type)).append("}}");
    }

    @Override
    public boolean shouldProcessTag(String name) {
        Assert.assertNotNull("Name should not be null", name);
        return true;
    }

    @Override
    public void tag(Tag tag) {
        actual.append("{{TAG : ").append(tag.getName());
        for (int i = 0; i < tag.getAttributeCount(); i++) {
            actual.append(' ').append(tag.getAttributeName(i)).append("=\"").append(tag.getAttributeValue(i))
                    .append('"');
        }
        actual.append(' ').append(typeAsString(tag.getType())).append("}}");
    }

    @Override
    public void text(Text text) {
        actual.append(text.getContents());
    }

    @Override
    public void warning(String message, int line, int column) {
        Assert.fail("Encountered error: " + message);
    }

    /**
     * Verify.
     */
    public void verify() {
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Type as string.
     *
     * @param type
     *            the type
     *
     * @return the string
     */
    private String typeAsString(int type) {
        switch (type) {
            case Tag.OPEN:
                return "*open*";
            case Tag.CLOSE:
                return "*close*";
            case Tag.EMPTY:
                return "*empty*";
            default:
                return "*unknown*";
        }
    }

}
