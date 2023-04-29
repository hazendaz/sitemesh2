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
 * Title:        TitleTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.taglib.AbstractTag;

/**
 * Write the Page <code>&lt;title&gt;</code> value to out.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.HTMLPage#getTitle()
 */
public class TitleTag extends AbstractTag {
    private String defaultTitle = null;

    /** Value to write if no title is found (optional). */
    public void setDefault(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public final int doEndTag() {
        try {
            String title = getPage().getTitle();
            if (title == null || title.trim().length() == 0)
                title = defaultTitle;
            if (title != null)
                getOut().write(title);
        } catch (Exception e) {
            trace(e);
        }
        return EVAL_PAGE;
    }

}