/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.html.TextFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextFilter that substitutes content using a JDK 1.4 regular expression.
 * <h2>Example</h2> This will substitute 'Google:Blah' for a link to a google search.
 *
 * <pre>
 * HTMLProcessor processor = new HTMLProcessor(in, out);
 * processor.addTextFilter(
 *         new RegexReplacementTextFilter("Google:([a-zA-Z]+)", "<a href='http://www.google.com/q=$1'>$1</a>"));
 * // add more TextFilters and TagRules
 * processor.process();
 * </pre>
 *
 * @author Joe Walnes
 */
public class RegexReplacementTextFilter implements TextFilter {

    /** The regex. */
    private final Pattern regex;

    /** The replacement. */
    private final String replacement;

    /**
     * Instantiates a new regex replacement text filter.
     *
     * @param regex
     *            the regex
     * @param replacement
     *            the replacement
     */
    public RegexReplacementTextFilter(String regex, String replacement) {
        this.regex = Pattern.compile(regex);
        this.replacement = replacement;
    }

    /**
     * Instantiates a new regex replacement text filter.
     *
     * @param regex
     *            the regex
     * @param replacement
     *            the replacement
     */
    public RegexReplacementTextFilter(Pattern regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String filter(String text) {
        Matcher matcher = regex.matcher(text);
        return matcher.replaceAll(replacement);
    }

}
