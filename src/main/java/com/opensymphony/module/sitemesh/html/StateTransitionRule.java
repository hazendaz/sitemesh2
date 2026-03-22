/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

/**
 * The Class StateTransitionRule.
 */
public class StateTransitionRule extends BasicRule {

    /** The new state. */
    private final State newState;

    /** The last state. */
    private State lastState;

    /**
     * Instantiates a new state transition rule.
     *
     * @param tagName
     *            the tag name
     * @param newState
     *            the new state
     */
    public StateTransitionRule(String tagName, State newState) {
        this(tagName, newState, true);
    }

    /**
     * Instantiates a new state transition rule.
     *
     * @param tagName
     *            the tag name
     * @param newState
     *            the new state
     * @param writeEnclosingTag
     *            the write enclosing tag
     */
    public StateTransitionRule(String tagName, State newState, boolean writeEnclosingTag) {
        super(tagName);
        this.newState = newState;
    }

    @Override
    public void process(Tag tag) {
        if (tag.getType() == Tag.OPEN) {
            lastState = context.currentState();
            context.changeState(newState);
            newState.addRule(this);
        } else if (tag.getType() == Tag.CLOSE && lastState != null) {
            context.changeState(lastState);
            lastState = null;
        }
    }
}
