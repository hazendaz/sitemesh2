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

import com.opensymphony.module.sitemesh.html.util.StringSitemeshBuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Class State.
 */
public final class State {

    /** The rules. */
    private TagRule[] rules = new TagRule[16]; // List is too slow, according to profiler

    /** The rule count. */
    private int ruleCount = 0;

    /** The listeners. */
    private List<StateChangeListener> listeners;

    /** The text filters. */
    private List<TextFilter> textFilters; // lazily instantiated to reduce overhead for most cases where it's not
                                          // needed.

    /**
     * Adds the rule.
     *
     * @param rule
     *            the rule
     */
    public void addRule(TagRule rule) {
        if (ruleCount == rules.length) {
            // grow array if necessary
            TagRule[] longerArray = new TagRule[rules.length * 2];
            System.arraycopy(rules, 0, longerArray, 0, ruleCount);
            rules = longerArray;
        }
        rules[ruleCount++] = rule;
    }

    /**
     * Adds the text filter.
     *
     * @param textFilter
     *            the text filter
     */
    public void addTextFilter(TextFilter textFilter) {
        if (textFilters == null) {
            textFilters = new ArrayList<TextFilter>(); // lazy instantiation
        }
        textFilters.add(textFilter);
    }

    /**
     * Should process tag.
     *
     * @param tagName
     *            the tag name
     *
     * @return true, if successful
     */
    public boolean shouldProcessTag(String tagName) {
        for (int i = ruleCount - 1; i >= 0; i--) { // reverse iteration to so most recently added rule matches
            if (rules[i].shouldProcess(tagName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the rule.
     *
     * @param tagName
     *            the tag name
     *
     * @return the rule
     */
    public TagRule getRule(String tagName) {
        for (int i = ruleCount - 1; i >= 0; i--) { // reverse iteration to so most recently added rule matches
            if (rules[i].shouldProcess(tagName)) {
                return rules[i];
            }
        }
        return null;
    }

    /**
     * Adds the listener.
     *
     * @param listener
     *            the listener
     */
    public void addListener(StateChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<StateChangeListener>();
        }
        listeners.add(listener);
    }

    /**
     * End of state.
     */
    public void endOfState() {
        if (listeners == null) {
            return;
        }
        for (Iterator<StateChangeListener> iter = listeners.iterator(); iter.hasNext();) {
            StateChangeListener listener = (StateChangeListener) iter.next();
            listener.stateFinished();
        }
    }

    /**
     * Handle text.
     *
     * @param text
     *            the text
     * @param context
     *            the context
     */
    public void handleText(Text text, HTMLProcessorContext context) {
        if (textFilters != null && !textFilters.isEmpty()) {
            String original = text.getContents();
            String asString = original;
            for (Iterator<TextFilter> iterator = textFilters.iterator(); iterator.hasNext();) {
                TextFilter textFilter = (TextFilter) iterator.next();
                asString = textFilter.filter(asString);
            }
            if (!original.equals(asString)) {
                context.currentBuffer().delete(text.getPosition(), text.getLength());
                context.currentBuffer().insert(text.getPosition(), StringSitemeshBuffer.createBufferFragment(asString));
            }
        }
    }

}
