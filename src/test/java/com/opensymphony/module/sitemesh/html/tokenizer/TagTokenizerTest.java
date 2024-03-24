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
package com.opensymphony.module.sitemesh.html.tokenizer;

import com.opensymphony.module.sitemesh.html.Tag;
import com.opensymphony.module.sitemesh.html.Text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class TagTokenizerTest.
 */
class TagTokenizerTest {

    /** The handler. */
    private MockTokenHandler handler;

    /**
     * The test setup. throws Exception the Exception
     *
     * @throws Exception
     *             the exception
     */
    @BeforeEach
    void setUp() throws Exception {
        handler = new MockTokenHandler();
    }

    /**
     * Test splits tags from text.
     */
    @Test
    void splitsTagsFromText() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello");
        handler.expectText("cruel");
        handler.expectTag(Tag.OPEN, "world");
        handler.expectTag(Tag.OPEN, "and");
        handler.expectText("some stuff");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<hello>cruel<world><and>some stuff");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test distinguishes between open close and empty tags.
     */
    @Test
    void distinguishesBetweenOpenCloseAndEmptyTags() {
        // expectations
        handler.expectTag(Tag.OPEN, "open");
        handler.expectTag(Tag.CLOSE, "close");
        handler.expectTag(Tag.EMPTY, "empty");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<open></close><empty/>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats comments as text.
     */
    @Test
    void treatsCommentsAsText() {
        // expectations
        handler.expectText("hello world ");
        handler.expectText("<!-- how are<we> \n -doing? -->");
        handler.expectText("<!-- -->");
        handler.expectText("<!---->");
        handler.expectText("good\n bye.");
        handler.expectTag(Tag.OPEN, "br");
        // execute
        TagTokenizer tokenizer = new TagTokenizer(
                "hello world <!-- how are<we> \n -doing? --><!-- --><!---->good\n bye.<br>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test extracts unquoted attributes from tag.
     */
    @Test
    void extractsUnquotedAttributesFromTag() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello", new String[] { "name", "world", "foo", "boo" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<hello name=world foo=boo>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test extracts quoted attributes from tag.
     */
    @Test
    void extractsQuotedAttributesFromTag() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello", new String[] { "name", "the world", "foo", "boo" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<hello name=\"the world\" foo=\"boo\">");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test handles mixed quote types in attributes.
     */
    @Test
    void handlesMixedQuoteTypesInAttributes() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello", new String[] { "name", "it's good", "foo", "say \"boo\"" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<hello name=\"it's good\" foo=\'say \"boo\"'>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test handles html style empty attributes.
     */
    @Test
    void handlesHtmlStyleEmptyAttributes() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello", new String[] { "isgood", null, "and", null, "stuff", null });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<hello isgood and stuff>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test supports whitespace in elements.
     */
    @Test
    void supportsWhitespaceInElements() {
        // expectations
        handler.expectTag(Tag.OPEN, "hello", new String[] { "somestuff", "good", "foo", null, "x", "long\n string" });
        handler.expectTag(Tag.EMPTY, "empty");
        handler.expectTag(Tag.OPEN, "HTML",
                new String[] { "notonnewline", "yo", "newline", "hello", "anotherline", "bye" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("" + "<hello \n somestuff = \ngood \n   foo \nx=\"long\n string\"   >"
                + "<empty      />" + "<HTML notonnewline=yo newline=\n" + "hello anotherline=\n" + "\"bye\">");
        tokenizer.start(handler);
        // verify
        handler.verify();

    }

    /**
     * Test exposes original tag to handler.
     */
    @Test
    void exposesOriginalTagToHandler() {
        // Should really use a mock library for this expectation, but I'd rather not
        // add a new dependency for the sake of a single test.
        final String originalTag = "<hello \n somestuff = \ngood \n   foo \nx=\"long\n string\"   >";
        TagTokenizer tokenizer = new TagTokenizer("some text" + originalTag + "more text");
        final boolean[] called = { false }; // has to be final array so anonymous inner class can change the value.

        tokenizer.start(new TokenHandler() {

            @Override
            public boolean shouldProcessTag(String name) {
                return true;
            }

            @Override
            public void tag(Tag tag) {
                Assertions.assertEquals(originalTag, tag.getContents());
                called[0] = true;
            }

            @Override
            public void text(Text text) {
                // ignoring text for this test
            }

            @Override
            public void warning(String message, int line, int column) {
                Assertions.fail("Encountered error " + message);
            }
        });

        Assertions.assertTrue(called[0], "tag() never called");
    }

    /**
     * Test allows slash in unquoted attribute.
     */
    @Test
    void allowsSlashInUnquotedAttribute() {
        // expectations
        handler.expectTag(Tag.OPEN, "something", new String[] { "type", "text/html" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<something type=text/html>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test allows trailing quote on attribute.
     */
    @Test
    void allowsTrailingQuoteOnAttribute() {
        // expectations
        handler.expectTag(Tag.OPEN, "something", new String[] { "type", "bl'ah\"" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<something type=bl'ah\">");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test allows awkward chars in element and attribute.
     */
    @Test
    void allowsAwkwardCharsInElementAndAttribute() {
        // expectations
        handler.expectTag(Tag.OPEN, "name:space", new String[] { "foo:bar", "x:y%" });
        handler.expectTag(Tag.EMPTY, "a_b-c$d", new String[] { "b_b-c$d", "c_b=c$d" });
        handler.expectTag(Tag.OPEN, "a", new String[] { "href",
                "/exec/obidos/flex-sign-in/ref=pd_nfy_gw_si/026-2634699-7306802?opt=a&page=misc/login/flex-sign-in-secure.html&response=tg/new-for-you/new-for-you/-/main" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("" + "<name:space foo:bar=x:y%>" + "<a_b-c$d b_b-c$d=c_b=c$d />"
                + "<a href=/exec/obidos/flex-sign-in/ref=pd_nfy_gw_si/026-2634699-7306802?opt=a&page=misc/login/flex-sign-in-secure.html&response=tg/new-for-you/new-for-you/-/main>");
        tokenizer.start(handler);
        // verify
        handler.verify();

    }

    /**
     * Test treats xmp cdata script and processing instructions as text.
     */
    @Test
    void treatsXmpCdataScriptAndProcessingInstructionsAsText() {
        // expectations
        handler.expectText("<script language=jscript> if (a < b & > c)\n alert(); </script>");
        handler.expectText("<xmp><evil \n<stuff<</xmp>");
        handler.expectText("<?some stuff ?>");
        handler.expectText("<![CDATA[ evil<>> <\n    ]]>");
        handler.expectText("<SCRIPT>stuff</SCRIPT>");
        handler.expectText("<!DOCTYPE html PUBLIC \\\"-//W3C//DTD HTML 4.01 Transitional//EN\\\">");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("" + "<script language=jscript> if (a < b & > c)\n alert(); </script>"
                + "<xmp><evil \n<stuff<</xmp>" + "<?some stuff ?>" + "<![CDATA[ evil<>> <\n    ]]>"
                + "<SCRIPT>stuff</SCRIPT>" + "<!DOCTYPE html PUBLIC \\\"-//W3C//DTD HTML 4.01 Transitional//EN\\\">");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats unterminated tag at eof as text.
     */
    @Test
    void treatsUnterminatedTagAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<world");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<world");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats lt at eof as text.
     */
    @Test
    void treatsLtAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats unterminated attribute name at eof as text.
     */
    @Test
    void treatsUnterminatedAttributeNameAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<world x");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<world x");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /*
     * TODO @Test public void testTreatsUnterminatedQuotedAttributeValueAtEofAsText() { // expectations
     * handler.expectText("hello"); handler.expectText("<world x=\"fff"); // execute TagTokenizer tokenizer = new
     * TagTokenizer("hello<world x=\"fff"); tokenizer.start(handler); // verify handler.verify(); }
     */

    /**
     * Test treats unterminated attribute at eof as text.
     */
    @Test
    void treatsUnterminatedAttributeAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<world x=");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<world x=");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats unterminated unquoted attribute value at eof as text.
     */
    @Test
    void treatsUnterminatedUnquotedAttributeValueAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<world x=fff");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<world x=fff");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test treats unterminated closing tag at eof as text.
     */
    @Test
    void treatsUnterminatedClosingTagAtEofAsText() {
        // expectations
        handler.expectText("hello");
        handler.expectText("<world /");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("hello<world /");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test ignores evil malformed pair of angle brackets.
     */
    @Test
    void ignoresEvilMalformedPairOfAngleBrackets() {
        // expectations
        handler.expectTag(Tag.OPEN, "good");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<>< ><good><>");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test does not try to parse tags unless the handler cares.
     */
    @Test
    void doesNotTryToParseTagsUnlessTheHandlerCares() {
        // setup
        handler = new MockTokenHandler() {
            @Override
            public boolean shouldProcessTag(String name) {
                return name.equals("good");
            }
        };
        // expectations
        handler.expectTag(Tag.OPEN, "good");
        handler.expectText("<bad>");
        handler.expectTag(Tag.CLOSE, "good");
        handler.expectText("<![bad]-->");
        handler.expectText("<unfinished");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<good><bad></good><![bad]--><unfinished");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }

    /**
     * Test parses magic comment blocks.
     */
    @Test
    void parsesMagicCommentBlocks() {
        // expectations
        handler.expectTag(Tag.OPEN_MAGIC_COMMENT, "if", new String[] { "gte", null, "mso", null, "9", null });
        handler.expectTag(Tag.OPEN, "stuff");
        handler.expectTag(Tag.CLOSE_MAGIC_COMMENT, "endif");
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<!--[if gte mso 9]><stuff><![endif]-->");
        tokenizer.start(handler);
        // verify
        handler.verify();

    }

    /**
     * Test tolerates extra quote closing attribute value.
     */
    @Test
    void toleratesExtraQuoteClosingAttributeValue() {
        // expectations
        handler = new MockTokenHandler() {
            @Override
            public void warning(String message, int line, int column) {
                // warning ok!
            }
        };
        handler.expectTag(Tag.OPEN, "a", new String[] { "href", "something-with-a-naughty-quote" });
        // execute
        TagTokenizer tokenizer = new TagTokenizer("<a href=\"something-with-a-naughty-quote\"\">");
        tokenizer.start(handler);
        // verify
        handler.verify();
    }
}
