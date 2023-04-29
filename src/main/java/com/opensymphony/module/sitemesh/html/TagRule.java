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

public interface TagRule {
    void setContext(HTMLProcessorContext context);

    /**
     * Called by the HTMLProcessor to determine if a rule should be called for a given tag.
     *
     * The name parameter will always be passed in lowercase. 
     */
    boolean shouldProcess(String name);

    void process(Tag tag);
}
