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
/*
 * Title:        AbstractHTMLPage
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;
import java.io.Writer;

/**
 * Abstract implementation of {@link com.opensymphony.module.sitemesh.HTMLPage}.
 * <p>
 * Adds to {@link com.opensymphony.module.sitemesh.parser.AbstractPage} some HTML methods. To implement, follow
 * guidelines of super-class, and implement the 2 abstract methods states below.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.parser.AbstractPage
 * @see com.opensymphony.module.sitemesh.HTMLPage
 */
public abstract class AbstractHTMLPage extends AbstractPage implements HTMLPage {

    /**
     * Instantiates a new abstract HTML page.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     */
    protected AbstractHTMLPage(SitemeshBuffer sitemeshBuffer) {
        super(sitemeshBuffer);
    }

    /**
     * Write data of html <code>&lt;head&gt;</code> tag.
     * <p>
     * Must be implemented. Data written should not actually contain the head tags, but all the data in between.
     */
    @Override
    public abstract void writeHead(Writer out) throws IOException;

    @Override
    public boolean isFrameSet() {
        return isPropertySet("frameset") && getProperty("frameset").equalsIgnoreCase("true");
    }

    @Override
    public void setFrameSet(boolean frameset) {
        if (frameset) {
            addProperty("frameset", "true");
        } else if (isPropertySet("frameset")) {
            addProperty("frameset", "false");
        }
    }
}
