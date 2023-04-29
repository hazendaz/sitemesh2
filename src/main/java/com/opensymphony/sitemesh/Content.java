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
package com.opensymphony.sitemesh;

import java.io.IOException;
import java.io.Writer;

/**
 * The Interface Content.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface Content {

    /**
     * Write out the original unprocessed content.
     *
     * @param writer
     *            the writer
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void writeOriginal(Writer writer) throws IOException;

    /**
     * Write the contents of the <code>&lt;body&gt;</code> tag.
     *
     * @param out
     *            the out
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void writeBody(Writer out) throws IOException;

    /**
     * Write the contents of the <code>&lt;head&gt;</code> tag.
     *
     * @param out
     *            the out
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void writeHead(Writer out) throws IOException;

    /**
     * Get the Title of the document.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Get a property embedded into the <code>Page</code> as a <code>String</code>.
     *
     * @param name
     *            Name of property
     *
     * @return Property value
     */
    String getProperty(String name);

    /**
     * Get all available property keys for the <code>Page</code>.
     *
     * @return Property keys
     */
    String[] getPropertyKeys();

    /**
     * Manually add a property to page.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    void addProperty(String name, String value);

}
