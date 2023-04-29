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

import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

/**
 * Tag returned by HTMLTagTokenizer. Allows easy access to element name and attributes. This interface supports
 * read-only operations on the tag. To change a tag, use {@link CustomTag}.
 *
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TokenHandler
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer
 * @see CustomTag
 *
 * @author Joe Walnes
 */
public interface Tag {

    int OPEN = 1;
    int CLOSE = 2;
    int EMPTY = 3;
    int OPEN_MAGIC_COMMENT = 4;
    int CLOSE_MAGIC_COMMENT = 5;

    /**
     * Get the complete tag in its original form, preserving original formatting. This has a slight overhead in that it
     * needs to construct a String. For improved performance, use writeTo() instead.
     */
    String getContents();

    /**
     * Write out the complete tag in its original form, preserving original formatting.
     */
    void writeTo(SitemeshBufferFragment.Builder fragment, int position);

    /**
     * Name of tag (ie. element name).
     */
    String getName();

    /**
     * Type of tag: <br/>
     * &lt;blah&gt; - Tag.OPEN<br/>
     * &lt;/blah&gt; - Tag.CLOSE<br/>
     * &lt;blah/&gt; - Tag.EMPTY<br/>
     */
    int getType();

    /**
     * Number of attributes in tag.
     */
    int getAttributeCount();

    /**
     * Determine which attribute has the specified name.
     */
    int getAttributeIndex(String name, boolean caseSensitive);

    /**
     * Get name of attribute.
     */
    String getAttributeName(int index);

    /**
     * Get value of an attribute. If this is an empty attribute (i.e. just a name, without a value), null is returned.
     */
    String getAttributeValue(int index);

    /**
     * Get value of an attribute. If this is an empty attribute (i.e. just a name, without a value), null is returned.
     */
    String getAttributeValue(String name, boolean caseSensitive);

    /**
     * Determine if an attribute is present.
     */
    boolean hasAttribute(String name, boolean caseSensitive);

    /**
     * The position of the tag
     */
    int getPosition();

    /**
     * The length of the tag
     */
    int getLength();

}
