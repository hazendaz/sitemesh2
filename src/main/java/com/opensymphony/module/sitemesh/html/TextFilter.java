/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

/**
 * TextFilters can be added to the HTMLProcessor (or specific States) and allow a simple means of filtering text
 * content.
 * <p>
 * More than one TextFilter may be added to each HTMLProcessor/State and they will be called in the order they were
 * added.
 * </p>
 *
 * @author Joe Walnes
 *
 * @see HTMLProcessor
 * @see State
 * @see com.opensymphony.module.sitemesh.html.rules.RegexReplacementTextFilter
 */
public interface TextFilter {

    /**
     * Filter.
     *
     * @param content
     *            the content
     *
     * @return the string
     */
    String filter(String content);

}
