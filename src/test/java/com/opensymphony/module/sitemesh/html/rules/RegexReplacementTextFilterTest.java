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

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.HTMLProcessor;

import java.io.IOException;

import junit.framework.TestCase;

public class RegexReplacementTextFilterTest extends TestCase {

    private SitemeshBufferFragment.Builder body;

    private HTMLProcessor createProcessor(String input) {
        SitemeshBuffer buffer = new DefaultSitemeshBuffer(input.toCharArray());
        body = SitemeshBufferFragment.builder().setBuffer(buffer);
        return new HTMLProcessor(buffer, body);
    }

    public void testReplacesTextContentMatchedByRegularExpression() throws IOException {
        HTMLProcessor processor = createProcessor("<hello>Today is DATE so hi</hello>");
        processor.addTextFilter(new RegexReplacementTextFilter("DATE", "1-jan-2009"));

        processor.process();
        assertEquals("<hello>Today is 1-jan-2009 so hi</hello>", body.build().getStringContent());
    }

    public void testAllowsMatchedGroupToBeUsedInSubsitution() throws IOException {
        HTMLProcessor processor = createProcessor("<hello>I think JIRA:SIM-1234 is the way forward</hello>");
        processor.addTextFilter(new RegexReplacementTextFilter("JIRA:([A-Z]+\\-[0-9]+)",
                "<a href='http://jira.opensymhony.com/browse/$1'>$1</a>"));

        processor.process();
        assertEquals(
                "<hello>I think <a href='http://jira.opensymhony.com/browse/SIM-1234'>SIM-1234</a> is the way forward</hello>",
                body.build().getStringContent());
    }

}