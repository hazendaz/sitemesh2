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

public class StateTransitionRule extends BasicRule {

    private final State newState;
    private final boolean writeEnclosingTag;

    private State lastState;

    public StateTransitionRule(String tagName, State newState) {
        this(tagName, newState, true);
    }

    public StateTransitionRule(String tagName, State newState, boolean writeEnclosingTag) {
        super(tagName);
        this.newState = newState;
        this.writeEnclosingTag = writeEnclosingTag;
    }

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
