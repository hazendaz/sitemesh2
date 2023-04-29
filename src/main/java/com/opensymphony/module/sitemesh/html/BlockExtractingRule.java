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

/**
 * The Class BlockExtractingRule.
 */
public abstract class BlockExtractingRule extends BasicRule {

    /** The keep in buffer. */
    private boolean keepInBuffer;

    // we should only handle tags that have been opened previously.
    /** The seen opening tag. */
    // else the parser throws a NoSuchElementException (SIM-216)
    private boolean seenOpeningTag;

    /**
     * Instantiates a new block extracting rule.
     *
     * @param keepInBuffer
     *            the keep in buffer
     * @param acceptableTagName
     *            the acceptable tag name
     */
    protected BlockExtractingRule(boolean keepInBuffer, String acceptableTagName) {
        super(acceptableTagName);
        this.keepInBuffer = keepInBuffer;
    }

    /**
     * Instantiates a new block extracting rule.
     *
     * @param keepInBuffer
     *            the keep in buffer
     */
    protected BlockExtractingRule(boolean keepInBuffer) {
        this.keepInBuffer = keepInBuffer;
    }

    public void process(Tag tag) {
        if (tag.getType() == Tag.OPEN) {
            if (!keepInBuffer) {
                context.currentBuffer().markStartDelete(tag.getPosition());
            }
            context.pushBuffer(
                    createBuffer(context.getSitemeshBuffer()).markStart(tag.getPosition() + tag.getLength()));
            start(tag);
            seenOpeningTag = true;
        } else if (tag.getType() == Tag.CLOSE && seenOpeningTag) {
            context.currentBuffer().end(tag.getPosition());
            end(tag);
            context.popBuffer();
            if (!keepInBuffer) {
                context.currentBuffer().endDelete(tag.getPosition() + tag.getLength());
            }
            seenOpeningTag = false;
        } else if (!keepInBuffer) {
            context.currentBuffer().delete(tag.getPosition(), tag.getLength());
        }
    }

    /**
     * Start.
     *
     * @param tag
     *            the tag
     */
    protected void start(Tag tag) {
    }

    /**
     * End.
     *
     * @param tag
     *            the tag
     */
    protected void end(Tag tag) {
    }

    /**
     * Creates the buffer.
     *
     * @param sitemeshBuffer
     *            the sitemesh buffer
     *
     * @return the sitemesh buffer fragment. builder
     */
    protected SitemeshBufferFragment.Builder createBuffer(SitemeshBuffer sitemeshBuffer) {
        return SitemeshBufferFragment.builder().setBuffer(sitemeshBuffer);
    }

}
