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
 * TextFilters can be added to the HTMLProcessor (or specific States) and allow a simple means of filtering text content.
 *
 * <p>More than one TextFilter may be added to each HTMLProcessor/State and they will be called in the order they were added.</p>
 *
 * @author Joe Walnes
 *
 * @see HTMLProcessor
 * @see State
 * @see com.opensymphony.module.sitemesh.html.rules.RegexReplacementTextFilter
 */
public interface TextFilter {

    String filter(String content);
    
}
