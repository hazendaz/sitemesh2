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

import junit.framework.TestCase;

public class StateTest extends TestCase {

    class DummyRule extends BasicRule {
        public DummyRule(String acceptableTagName) {
            super(acceptableTagName);
        }

        public void process(Tag tag) {
            throw new UnsupportedOperationException();
        }
    }

    public void testMapsTagNameToRule() {
        TagRule mouseRule = new DummyRule("mouse");
        TagRule donkeyRule = new DummyRule("donkey");
        TagRule lemonRule = new DummyRule("lemon");

        State state = new State();
        state.addRule(mouseRule);
        state.addRule(donkeyRule);
        state.addRule(lemonRule);

        assertSame(donkeyRule, state.getRule("donkey"));
        assertSame(lemonRule, state.getRule("lemon"));
        assertSame(mouseRule, state.getRule("mouse"));
    }

    public void testExposesWhetherItShouldProcessATagBasedOnAvailableRules() {
        TagRule mouseRule = new DummyRule("mouse");
        TagRule donkeyRule = new DummyRule("donkey");
        TagRule lemonRule = new DummyRule("lemon");

        State state = new State();
        state.addRule(mouseRule);
        state.addRule(donkeyRule);
        state.addRule(lemonRule);

        assertTrue(state.shouldProcessTag("donkey"));
        assertTrue(state.shouldProcessTag("lemon"));
        assertFalse(state.shouldProcessTag("yeeeehaa"));
    }

    public void testReturnsMostRecentlyAddedRuleIfMultipleMatchesFound() {
        TagRule oldRule = new DummyRule("something");
        TagRule newRule = new DummyRule("something");

        State state = new State();
        state.addRule(oldRule);
        state.addRule(newRule);

        assertSame(newRule, state.getRule("something"));
    }
}