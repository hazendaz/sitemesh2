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

public abstract class BasicRule implements TagRule {

    private final String[] acceptableTagNames;

    protected HTMLProcessorContext context;

    protected BasicRule(String[] acceptableTagNames) {
        this.acceptableTagNames = acceptableTagNames;
    }

    protected BasicRule(String acceptableTagName) {
        this.acceptableTagNames = new String[] { acceptableTagName };
    }

    protected BasicRule() {
        this.acceptableTagNames = null;
    }

    public void setContext(HTMLProcessorContext context) {
        this.context = context;
    }

    public boolean shouldProcess(String name) {
        if (acceptableTagNames == null || acceptableTagNames.length < 1) {
            throw new UnsupportedOperationException(getClass().getName()
                    + " should be constructed with acceptableTagNames OR should implement shouldProcess()");
        }

        for (int i = 0; i < acceptableTagNames.length; i++) {
            if (name.equals(acceptableTagNames[i]))
                return true;
        }
        return false;
    }

    public abstract void process(Tag tag);

    protected SitemeshBufferFragment.Builder currentBuffer() {
        return context.currentBuffer();
    }

    protected String getCurrentBufferContent() {
        return context.currentBuffer().build().getStringContent();
    }

}
