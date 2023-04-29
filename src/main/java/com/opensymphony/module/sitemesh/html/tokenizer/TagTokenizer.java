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
package com.opensymphony.module.sitemesh.html.tokenizer;

/**
 * Splits a chunk of HTML into 'text' and 'tag' tokens, for easy processing. Is VERY tolerant to badly formed HTML.
 * <h3>Usage</h3>
 * <p>
 * You need to supply a custom {@link TokenHandler} that will receive callbacks as text and tags are processed.
 *
 * <pre>
 * char[] input = ...;
 * HTMLTagTokenizer tokenizer = new HTMLTagTokenizer(input);
 * TokenHandler handler = new MyTokenHandler();
 * tokenizer.start(handler);
 * </pre>
 *
 * @author Joe Walnes
 *
 * @see TokenHandler
 * @see com.opensymphony.module.sitemesh.parser.HTMLPageParser
 */
public class TagTokenizer {

    private final char[] input;
    private final int length;

    public TagTokenizer(char[] input) {
        this(input, input.length);
    }

    public TagTokenizer(char[] input, int length) {
        this.input = input;
        this.length = length;
    }

    public TagTokenizer(String input) {
        this(input.toCharArray());
    }

    public void start(TokenHandler handler) {
        Parser parser = new Parser(input, length, handler);
        parser.start();
    }

}
