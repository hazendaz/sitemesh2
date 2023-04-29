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

    private final Pattern regex;
    private final String replacement;

    public RegexReplacementTextFilter(String regex, String replacement) {
        this.regex = Pattern.compile(regex);
        this.replacement = replacement;
    }

    public RegexReplacementTextFilter(Pattern regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    public String filter(String text) {
        Matcher matcher = regex.matcher(text);
        return matcher.replaceAll(replacement);
    }

}
