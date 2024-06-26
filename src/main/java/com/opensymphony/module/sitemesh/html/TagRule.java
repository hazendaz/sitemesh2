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

/**
 * The Interface TagRule.
 */
public interface TagRule {

    /**
     * Sets the context.
     *
     * @param context
     *            the new context
     */
    void setContext(HTMLProcessorContext context);

    /**
     * Called by the HTMLProcessor to determine if a rule should be called for a given tag. The name parameter will
     * always be passed in lowercase.
     *
     * @param name
     *            the name
     *
     * @return true, if successful
     */
    boolean shouldProcess(String name);

    /**
     * Process.
     *
     * @param tag
     *            the tag
     */
    void process(Tag tag);
}
