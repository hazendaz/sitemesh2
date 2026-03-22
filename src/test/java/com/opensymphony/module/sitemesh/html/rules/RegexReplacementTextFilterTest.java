/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html.rules;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.HTMLProcessor;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class RegexReplacementTextFilterTest.
 */
class RegexReplacementTextFilterTest {

    /** The body. */
    private SitemeshBufferFragment.Builder body;

    /**
     * Creates the processor.
     *
     * @param input
     *            the input
     *
     * @return the HTML processor
     */
    private HTMLProcessor createProcessor(String input) {
        SitemeshBuffer buffer = new DefaultSitemeshBuffer(input.toCharArray());
        body = SitemeshBufferFragment.builder().setBuffer(buffer);
        return new HTMLProcessor(buffer, body);
    }

    /**
     * Test replaces text content matched by regular expression.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void replacesTextContentMatchedByRegularExpression() throws IOException {
        HTMLProcessor processor = createProcessor("<hello>Today is DATE so hi</hello>");
        processor.addTextFilter(new RegexReplacementTextFilter("DATE", "1-jan-2009"));

        processor.process();
        Assertions.assertEquals("<hello>Today is 1-jan-2009 so hi</hello>", body.build().getStringContent());
    }

    /**
     * Test allows matched group to be used in subsitution.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void allowsMatchedGroupToBeUsedInSubsitution() throws IOException {
        HTMLProcessor processor = createProcessor("<hello>I think JIRA:SIM-1234 is the way forward</hello>");
        processor.addTextFilter(new RegexReplacementTextFilter("JIRA:([A-Z]+\\-[0-9]+)",
                "<a href='http://jira.opensymhony.com/browse/$1'>$1</a>"));

        processor.process();
        Assertions.assertEquals(
                "<hello>I think <a href='http://jira.opensymhony.com/browse/SIM-1234'>SIM-1234</a> is the way forward</hello>",
                body.build().getStringContent());
    }

}
