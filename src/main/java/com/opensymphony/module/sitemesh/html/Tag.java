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
 * @author Joe Walnes
 *
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TokenHandler
 * @see com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer
 * @see CustomTag
 */
public interface Tag {

    /** The open. */
    int OPEN = 1;

    /** The close. */
    int CLOSE = 2;

    /** The empty. */
    int EMPTY = 3;

    /** The open magic comment. */
    int OPEN_MAGIC_COMMENT = 4;

    /** The close magic comment. */
    int CLOSE_MAGIC_COMMENT = 5;

    /**
     * Get the complete tag in its original form, preserving original formatting. This has a slight overhead in that it
     * needs to construct a String. For improved performance, use writeTo() instead.
     *
     * @return the contents
     */
    String getContents();

    /**
     * Write out the complete tag in its original form, preserving original formatting.
     *
     * @param fragment
     *            the fragment
     * @param position
     *            the position
     */
    void writeTo(SitemeshBufferFragment.Builder fragment, int position);

    /**
     * Name of tag (ie. element name).
     *
     * @return the name
     */
    String getName();

    /**
     * Type of tag: <br/>
     * &lt;blah&gt; - Tag.OPEN<br/>
     * &lt;/blah&gt; - Tag.CLOSE<br/>
     * &lt;blah/&gt; - Tag.EMPTY<br/>
     *
     * @return the type
     */
    int getType();

    /**
     * Number of attributes in tag.
     *
     * @return the attribute count
     */
    int getAttributeCount();

    /**
     * Determine which attribute has the specified name.
     *
     * @param name
     *            the name
     * @param caseSensitive
     *            the case sensitive
     *
     * @return the attribute index
     */
    int getAttributeIndex(String name, boolean caseSensitive);

    /**
     * Get name of attribute.
     *
     * @param index
     *            the index
     *
     * @return the attribute name
     */
    String getAttributeName(int index);

    /**
     * Get value of an attribute. If this is an empty attribute (i.e. just a name, without a value), null is returned.
     *
     * @param index
     *            the index
     *
     * @return the attribute value
     */
    String getAttributeValue(int index);

    /**
     * Get value of an attribute. If this is an empty attribute (i.e. just a name, without a value), null is returned.
     *
     * @param name
     *            the name
     * @param caseSensitive
     *            the case sensitive
     *
     * @return the attribute value
     */
    String getAttributeValue(String name, boolean caseSensitive);

    /**
     * Determine if an attribute is present.
     *
     * @param name
     *            the name
     * @param caseSensitive
     *            the case sensitive
     *
     * @return true, if successful
     */
    boolean hasAttribute(String name, boolean caseSensitive);

    /**
     * The position of the tag.
     *
     * @return the position
     */
    int getPosition();

    /**
     * The length of the tag.
     *
     * @return the length
     */
    int getLength();

}
