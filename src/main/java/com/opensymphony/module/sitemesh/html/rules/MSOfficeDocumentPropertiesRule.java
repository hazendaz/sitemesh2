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
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.BlockExtractingRule;
import com.opensymphony.module.sitemesh.html.Tag;

/**
 * Extracts the extra properties saved in HTML from MS Office applications (Word and Excel), such as Author, Company,
 * Version, etc.
 *
 * @author Joe Walnes
 */
public class MSOfficeDocumentPropertiesRule extends BlockExtractingRule {

    /** The page. */
    private final PageBuilder page;

    /** The in document properties. */
    private boolean inDocumentProperties;

    /**
     * Instantiates a new MS office document properties rule.
     *
     * @param page
     *            the page
     */
    public MSOfficeDocumentPropertiesRule(PageBuilder page) {
        super(true);
        this.page = page;
    }

    @Override
    public boolean shouldProcess(String name) {
        return inDocumentProperties && name.startsWith("o:") || name.equals("o:documentproperties");
    }

    @Override
    public void process(Tag tag) {
        if (tag.getName().equals("o:DocumentProperties")) {
            inDocumentProperties = tag.getType() == Tag.OPEN;
        } else {
            super.process(tag);
        }
    }

    @Override
    protected void start(Tag tag) {
    }

    @Override
    protected void end(Tag tag) {
        String name = tag.getName().substring(2);
        page.addProperty("office.DocumentProperties." + name, getCurrentBufferContent());
    }

}
