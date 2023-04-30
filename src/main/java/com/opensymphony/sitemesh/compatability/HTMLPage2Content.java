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
package com.opensymphony.sitemesh.compatability;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.sitemesh.Content;

import java.io.IOException;
import java.io.Writer;

/**
 * Adapts a SiteMesh 2 {@link HTMLPage} to a SiteMesh 3 {@link Content}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class HTMLPage2Content implements Content {

    /** The page. */
    private final HTMLPage page;

    /**
     * Instantiates a new HTML page 2 content.
     *
     * @param page
     *            the page
     */
    public HTMLPage2Content(HTMLPage page) {
        this.page = page;
    }

    @Override
    public void writeOriginal(Writer out) throws IOException {
        page.writePage(out);
    }

    @Override
    public void writeBody(Writer out) throws IOException {
        page.writeBody(out);
    }

    @Override
    public void writeHead(Writer out) throws IOException {
        page.writeHead(out);
    }

    @Override
    public String getTitle() {
        return page.getTitle();
    }

    @Override
    public String getProperty(String name) {
        return page.getProperty(name);
    }

    @Override
    public String[] getPropertyKeys() {
        return page.getPropertyKeys();
    }

    @Override
    public void addProperty(String name, String value) {
        page.addProperty(name, value);
    }

}
