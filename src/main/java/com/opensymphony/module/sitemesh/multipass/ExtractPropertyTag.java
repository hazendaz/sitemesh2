/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.multipass;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.taglib.decorator.PropertyTag;

/**
 * The Class ExtractPropertyTag.
 */
public class ExtractPropertyTag extends PropertyTag {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Override
    public int doEndTag() {
        Page page = getPage();
        page.addProperty("_sitemesh.removefrompage." + getProperty(), "true");
        return super.doEndTag();
    }

}
