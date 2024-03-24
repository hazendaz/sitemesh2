/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2024 Hazendaz.
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

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.rules.TagReplaceRule;
import com.opensymphony.module.sitemesh.html.util.StringSitemeshBuffer;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class HTMLProcessorTest.
 */
class HTMLProcessorTest {

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
        SitemeshBuffer buffer = new StringSitemeshBuffer(input);
        body = SitemeshBufferFragment.builder().setBuffer(buffer);
        return new HTMLProcessor(buffer, body);
    }

    /**
     * Test creates state transition event.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void createsStateTransitionEvent() throws IOException {
        String input = "<a></a>";
        HTMLProcessor htmlProcessor = createProcessor(input);

        State defaultState = htmlProcessor.defaultState();

        final StringBuilder stateLog = new StringBuilder();

        defaultState.addListener(() -> stateLog.append("finished"));

        htmlProcessor.process();
        Assertions.assertEquals("finished", stateLog.toString());
    }

    /**
     * Test supports conventional reader and writer.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void supportsConventionalReaderAndWriter() throws IOException {
        HTMLProcessor processor = createProcessor("<hello><b id=\"something\">world</b></hello>");
        processor.addRule(new TagReplaceRule("b", "strong"));

        processor.process();
        Assertions.assertEquals("<hello><strong id=\"something\">world</strong></hello>",
                body.build().getStringContent());
    }

    /**
     * Test allows rules to modify attributes.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void allowsRulesToModifyAttributes() throws IOException {
        HTMLProcessor processor = createProcessor("<hello><a href=\"modify-me\">world</a></hello>");
        processor.addRule(new BasicRule("a") {
            @Override
            public void process(Tag tag) {
                currentBuffer().delete(tag.getPosition(), tag.getLength());
                CustomTag customTag = new CustomTag(tag);
                String href = customTag.getAttributeValue("href", false);
                if (href != null) {
                    href = href.toUpperCase();
                    customTag.setAttributeValue("href", true, href);
                }
                customTag.writeTo(currentBuffer(), tag.getPosition());
            }
        });

        processor.process();
        Assertions.assertEquals("<hello><a href=\"MODIFY-ME\">world</a></hello>", body.build().getStringContent());
    }

    /**
     * Test supports chained filtering of text content.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void supportsChainedFilteringOfTextContent() throws IOException {
        HTMLProcessor processor = createProcessor("<hello>world</hello>");
        processor.addTextFilter(text -> text.toUpperCase());
        processor.addTextFilter(text -> text.replace('O', 'o'));

        processor.process();
        Assertions.assertEquals("<HELLo>WoRLD</HELLo>", body.build().getStringContent());
    }

    /**
     * Test supports text filters for specific states.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void supportsTextFiltersForSpecificStates() throws IOException {
        HTMLProcessor processor = createProcessor("la la<br> la la <capitalism>laaaa<br> laaaa</capitalism> la la");
        State capsState = new State();
        processor.addRule(new StateTransitionRule("capitalism", capsState, true));

        capsState.addTextFilter(text -> text.toUpperCase());

        processor.process();
        Assertions.assertEquals("la la<br> la la <capitalism>LAAAA<BR> LAAAA</capitalism> la la",
                body.build().getStringContent());
    }

    /**
     * Test can add attributes to custom tag.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    void canAddAttributesToCustomTag() throws IOException {
        String html = "<h1>Headline</h1>";
        HTMLProcessor htmlProcessor = createProcessor(html);
        htmlProcessor.addRule(new BasicRule() {
            @Override
            public boolean shouldProcess(String tag) {
                return tag.equalsIgnoreCase("h1");
            }

            @Override
            public void process(Tag tag) {
                if (tag.getType() == Tag.OPEN) {
                    currentBuffer().delete(tag.getPosition(), tag.getLength());
                    CustomTag ctag = new CustomTag(tag);
                    ctag.addAttribute("class", "y");
                    Assertions.assertEquals(1, ctag.getAttributeCount());
                    ctag.writeTo(currentBuffer(), tag.getPosition());
                }
            }
        });
        htmlProcessor.process();
        Assertions.assertEquals("<h1 class=\"y\">Headline</h1>", body.build().getStringContent());
    }
}
