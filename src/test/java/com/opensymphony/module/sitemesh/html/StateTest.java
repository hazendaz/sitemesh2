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

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class StateTest.
 */
public class StateTest {

    /**
     * The Class DummyRule.
     */
    private static final class DummyRule extends BasicRule {

        /**
         * Instantiates a new dummy rule.
         *
         * @param acceptableTagName
         *            the acceptable tag name
         */
        public DummyRule(String acceptableTagName) {
            super(acceptableTagName);
        }

        @Override
        public void process(Tag tag) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test maps tag name to rule.
     */
    @Test
    public void testMapsTagNameToRule() {
        TagRule mouseRule = new DummyRule("mouse");
        TagRule donkeyRule = new DummyRule("donkey");
        TagRule lemonRule = new DummyRule("lemon");

        State state = new State();
        state.addRule(mouseRule);
        state.addRule(donkeyRule);
        state.addRule(lemonRule);

        Assert.assertSame(donkeyRule, state.getRule("donkey"));
        Assert.assertSame(lemonRule, state.getRule("lemon"));
        Assert.assertSame(mouseRule, state.getRule("mouse"));
    }

    /**
     * Test exposes whether it should process A tag based on available rules.
     */
    @Test
    public void testExposesWhetherItShouldProcessATagBasedOnAvailableRules() {
        TagRule mouseRule = new DummyRule("mouse");
        TagRule donkeyRule = new DummyRule("donkey");
        TagRule lemonRule = new DummyRule("lemon");

        State state = new State();
        state.addRule(mouseRule);
        state.addRule(donkeyRule);
        state.addRule(lemonRule);

        Assert.assertTrue(state.shouldProcessTag("donkey"));
        Assert.assertTrue(state.shouldProcessTag("lemon"));
        Assert.assertFalse(state.shouldProcessTag("yeeeehaa"));
    }

    /**
     * Test returns most recently added rule if multiple matches found.
     */
    @Test
    public void testReturnsMostRecentlyAddedRuleIfMultipleMatchesFound() {
        TagRule oldRule = new DummyRule("something");
        TagRule newRule = new DummyRule("something");

        State state = new State();
        state.addRule(oldRule);
        state.addRule(newRule);

        Assert.assertSame(newRule, state.getRule("something"));
    }
}
