/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
/*
 * Title:        UsePageTEI
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * TagExtraInfo implementation to expose Page object as variable.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see UsePageTag
 * @see UseHTMLPageTEI
 */
public class UsePageTEI extends TagExtraInfo {

    /**
     * Gets the type.
     *
     * @return the type
     */
    protected String getType() {
        return "com.opensymphony.module.sitemesh.Page";
    }

    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        String id = data.getAttributeString("id");
        return new VariableInfo[] { new VariableInfo(id, getType(), true, VariableInfo.AT_END) };
    }
}
