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

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.tokenizer.TagTokenizer;
import com.opensymphony.module.sitemesh.html.tokenizer.TokenHandler;

import java.io.IOException;

/**
 * The Class HTMLProcessor.
 */
public class HTMLProcessor {

    /** The sitemesh buffer. */
    private final SitemeshBuffer sitemeshBuffer;

    /** The body. */
    private final SitemeshBufferFragment.Builder body;

    /** The default state. */
    private final State defaultState = new State();

    /** The current state. */
    private State currentState = defaultState;

    /**
     * Instantiates a new HTML processor.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     * @param body
     *            the body
     */
    public HTMLProcessor(SitemeshBuffer sitemeshBuffer, SitemeshBufferFragment.Builder body) {
        this.sitemeshBuffer = sitemeshBuffer;
        this.body = body;
    }

    /**
     * Default state.
     *
     * @return the state
     */
    public State defaultState() {
        return defaultState;
    }

    /**
     * Equivalent of htmlProcessor.defaultState().addRule()
     *
     * @param rule
     *            the rule
     */
    public void addRule(TagRule rule) {
        defaultState.addRule(rule);
    }

    /**
     * Process.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void process() throws IOException {
        TagTokenizer tokenizer = new TagTokenizer(sitemeshBuffer.getCharArray(), sitemeshBuffer.getBufferLength());
        final HTMLProcessorContext context = new HTMLProcessorContext() {

            @Override
            public SitemeshBuffer getSitemeshBuffer() {
                return sitemeshBuffer;
            }

            @Override
            public State currentState() {
                return currentState;
            }

            @Override
            public void changeState(State newState) {
                currentState = newState;
            }

            private SitemeshBufferFragment.Builder[] buffers = new SitemeshBufferFragment.Builder[10];
            private int size;

            @Override
            public void pushBuffer(SitemeshBufferFragment.Builder buffer) {
                if (size == buffers.length) {
                    SitemeshBufferFragment.Builder[] newBuffers = new SitemeshBufferFragment.Builder[buffers.length
                            * 2];
                    System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
                    buffers = newBuffers;
                }
                buffers[size++] = buffer;
            }

            @Override
            public SitemeshBufferFragment.Builder currentBuffer() {
                return buffers[size - 1];
            }

            @Override
            public SitemeshBufferFragment.Builder popBuffer() {
                SitemeshBufferFragment.Builder last = buffers[size - 1];
                buffers[--size] = null;
                return last;
            }
        };
        context.pushBuffer(body);
        tokenizer.start(new TokenHandler() {

            @Override
            public boolean shouldProcessTag(String name) {
                return currentState.shouldProcessTag(name.toLowerCase());
            }

            @Override
            public void tag(Tag tag) {
                TagRule tagRule = currentState.getRule(tag.getName().toLowerCase());
                tagRule.setContext(context);
                tagRule.process(tag);
            }

            @Override
            public void text(Text text) {
                currentState.handleText(text, context);
            }

            @Override
            public void warning(String message, int line, int column) {
                // TODO Enable logging here
                // logger.info("{},{}: ", line, column, message);
            }
        });
        defaultState.endOfState();
    }

    /**
     * Adds the text filter.
     *
     * @param textFilter
     *            the text filter
     */
    public void addTextFilter(TextFilter textFilter) {
        currentState.addTextFilter(textFilter);
    }

}
