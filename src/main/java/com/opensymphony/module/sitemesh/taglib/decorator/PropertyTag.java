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
 * Title:        PropertyTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.taglib.AbstractTag;

import java.io.Writer;

/**
 * Write property of Page to out.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see com.opensymphony.module.sitemesh.Page#getProperty(java.lang.String)
 */
public class PropertyTag extends AbstractTag {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The default value. */
    private String propertyName, defaultValue;

    /** The write entire property. */
    private boolean writeEntireProperty = false;

    /**
     * Key of property to write.
     *
     * @param propertyName
     *            the new property
     */
    public void setProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Gets the property.
     *
     * @return the property
     */
    protected String getProperty() {
        return propertyName;
    }

    /**
     * Value to write if no property matching key is found (optional).
     *
     * @param defaultValue
     *            the new default
     */
    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * When begins with y, t or 1, the full attribute (name + value) is written.
     *
     * @param writeEntireProperty
     *            the new write entire property
     */
    public final void setWriteEntireProperty(String writeEntireProperty) {
        if (writeEntireProperty == null || writeEntireProperty.trim().length() == 0) {
            return;
        }

        switch (writeEntireProperty.charAt(0)) {
            case '1':
            case 't':
            case 'T':
            case 'y':
            case 'Y':
                this.writeEntireProperty = true;
                break;
            default:
                this.writeEntireProperty = false;
        }
    }

    @Override
    public int doEndTag() {
        try {
            Page page = getPage();
            String propertyValue = page.getProperty(propertyName);

            if (propertyValue == null || propertyValue.trim().length() == 0) {
                propertyValue = defaultValue;
            }

            if (propertyValue != null) {
                Writer out = getOut();
                if (writeEntireProperty) {
                    out.write(" ");
                    out.write(propertyName.substring(propertyName.lastIndexOf('.') + 1));
                    out.write("=\"");
                    out.write(propertyValue);
                    out.write("\"");
                } else {
                    out.write(propertyValue);
                }
            }
        } catch (Exception e) {
            trace(e);
        }
        return EVAL_PAGE;
    }

}
