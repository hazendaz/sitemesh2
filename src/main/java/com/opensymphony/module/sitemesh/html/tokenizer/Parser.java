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
/*
 * IF YOU ARE HAVING TROUBLE COMPILING THIS CLASS, IT IS PROBABLY BECAUSE Lexer.java IS MISSING.
 *
 * Use 'ant jflex' to generate the file, which will reside in build/java
 */

package com.opensymphony.module.sitemesh.html.tokenizer;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;
import com.opensymphony.module.sitemesh.html.Tag;
import com.opensymphony.module.sitemesh.html.Text;
import com.opensymphony.module.sitemesh.html.util.CharArray;
import com.opensymphony.module.sitemesh.util.CharArrayReader;

import java.io.IOException;

/**
 * Looks for patterns of tokens in the Lexer and translates these to calls to pass to the TokenHandler.
 *
 * @author Joe Walnes
 *
 * @see TagTokenizer
 */
public class Parser extends Lexer {

    /** The attribute buffer. */
    private final CharArray attributeBuffer = new CharArray(64);

    /** The reusable token. */
    private final ReusableToken reusableToken = new ReusableToken();

    /** The pushback token. */
    private int pushbackToken = -1;

    /** The pushback text. */
    private String pushbackText;

    /** The Constant SLASH. */
    public static final short SLASH = 257;

    /** The Constant WHITESPACE. */
    public static final short WHITESPACE = 258;

    /** The Constant EQUALS. */
    public static final short EQUALS = 259;

    /** The Constant QUOTE. */
    public static final short QUOTE = 260;

    /** The Constant WORD. */
    public static final short WORD = 261;

    /** The Constant TEXT. */
    public static final short TEXT = 262;

    /** The Constant QUOTED. */
    public static final short QUOTED = 263;

    /** The Constant LT. */
    public static final short LT = 264;

    /** The Constant GT. */
    public static final short GT = 265;

    /** The Constant LT_OPEN_MAGIC_COMMENT. */
    public static final short LT_OPEN_MAGIC_COMMENT = 266;

    /** The Constant LT_CLOSE_MAGIC_COMMENT. */
    public static final short LT_CLOSE_MAGIC_COMMENT = 267;

    /** The input. */
    private final char[] input;

    /** The handler. */
    private TokenHandler handler;

    /** The position. */
    private int position;

    /** The length. */
    private int length;

    /** The name. */
    private String name;

    /** The type. */
    private int type;

    /**
     * Instantiates a new parser.
     *
     * @param input
     *            the input
     * @param length
     *            the length
     * @param handler
     *            the handler
     */
    public Parser(char[] input, int length, TokenHandler handler) {
        super(new CharArrayReader(input, 0, length));
        this.input = input;
        this.handler = handler;
    }

    /**
     * Text.
     *
     * @return the string
     */
    private String text() {
        if (pushbackToken == -1) {
            return yytext();
        }
        return pushbackText;
    }

    /**
     * Skip white space.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void skipWhiteSpace() throws IOException {
        while (true) {
            int next;
            if (pushbackToken == -1) {
                next = yylex();
            } else {
                next = pushbackToken;
                pushbackToken = -1;
            }
            if (next != Parser.WHITESPACE) {
                pushBack(next);
                break;
            }
        }
    }

    /**
     * Push back.
     *
     * @param next
     *            the next
     */
    private void pushBack(int next) {
        if (pushbackToken != -1) {
            reportError("Cannot pushback more than once", line(), column());
        }
        pushbackToken = next;
        if (next == Parser.WORD || next == Parser.QUOTED || next == Parser.SLASH || next == Parser.EQUALS) {
            pushbackText = yytext();
        } else {
            pushbackText = null;
        }
    }

    /**
     * Start.
     */
    public void start() {
        try {
            while (true) {
                int token;
                if (pushbackToken == -1) {
                    token = yylex();
                } else {
                    token = pushbackToken;
                    pushbackToken = -1;
                }
                switch (token) {
                    case 0:
                        // EOF
                        return;
                    case Parser.TEXT:
                        // Got some text
                        parsedText(position(), length());
                        break;
                    case Parser.LT:
                        // Token "<" - start of tag
                        parseTag(Tag.OPEN);
                        break;
                    case Parser.LT_OPEN_MAGIC_COMMENT:
                        // Token "<!--[" - start of open magic comment
                        parseTag(Tag.OPEN_MAGIC_COMMENT);
                        break;
                    case Parser.LT_CLOSE_MAGIC_COMMENT:
                        // Token "<![" - start of close magic comment
                        parseTag(Tag.CLOSE_MAGIC_COMMENT);
                        break;
                    default:
                        reportError("Unexpected token from lexer, was expecting TEXT or LT", line(), column());
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses the tag.
     *
     * @param type
     *            the type
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void parseTag(int type) throws IOException {
        // Start parsing a TAG

        int start = position();
        skipWhiteSpace();
        int token;
        if (pushbackToken == -1) {
            token = yylex();
        } else {
            token = pushbackToken;
            pushbackToken = -1;
        }

        if (token == Parser.SLASH) {
            // Token "/" - it's a closing tag
            type = Tag.CLOSE;
            if (pushbackToken == -1) {
                token = yylex();
            } else {
                token = pushbackToken;
                pushbackToken = -1;
            }
        }

        switch (token) {
            case Parser.WORD: {
                // Token WORD - name of tag
                String name = text();
                if (handler.shouldProcessTag(name)) {
                    parseFullTag(type, name, start);
                } else {
                    resetLexerState();
                    pushBack(yylex()); // take and replace the next token, so the position is correct
                    parsedText(start, position() - start);
                }
                break;
            }
            case Parser.GT:
                break;
            case 0:
                parsedText(start, position() - start); // eof
                break;
            default:
                reportError("Could not recognise tag", line(), column());
                break;
        }
    }

    /**
     * Parses the full tag.
     *
     * @param type
     *            the type
     * @param name
     *            the name
     * @param start
     *            the start
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void parseFullTag(int type, String name, int start) throws IOException {
        int token;
        while (true) {
            skipWhiteSpace();
            if (pushbackToken == -1) {
                token = yylex();
            } else {
                token = pushbackToken;
                pushbackToken = -1;
            }
            pushBack(token);

            if (token == Parser.SLASH || token == Parser.GT) {
                break; // no more attributes here
            }
            if (token == Parser.WORD) {
                parseAttribute(); // start of an attribute
            } else if (token == 0) {
                parsedText(start, position() - start); // eof
                return;
            } else {
                reportError("Illegal tag", line(), column());
                break;
            }
        }

        if (pushbackToken == -1) {
            token = yylex();
        } else {
            token = pushbackToken;
            pushbackToken = -1;
        }
        if (token == Parser.SLASH) {
            // Token "/" - it's an empty tag
            type = Tag.EMPTY;
            if (pushbackToken == -1) {
                token = yylex();
            } else {
                token = pushbackToken;
                pushbackToken = -1;
            }
        }

        if (token == Parser.GT) {
            // Token ">" - YAY! end of tag.. process it!
            parsedTag(type, name, start, position() - start + 1);
        } else if (token == 0) {
            parsedText(start, position() - start); // eof
        } else {
            reportError("Expected end of tag", line(), column());
            parsedTag(type, name, start, position() - start + 1);
        }
    }

    /**
     * Parses the attribute.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void parseAttribute() throws IOException {
        int token;
        if (pushbackToken == -1) {
            token = yylex();
        } else {
            token = pushbackToken;
            pushbackToken = -1;
        }
        // Token WORD - start of an attribute
        String attributeName = text();
        skipWhiteSpace();
        if (pushbackToken == -1) {
            token = yylex();
        } else {
            token = pushbackToken;
            pushbackToken = -1;
        }
        switch (token) {
            case Parser.EQUALS:
                // Token "=" - the attribute has a value
                skipWhiteSpace();
                if (pushbackToken == -1) {
                    token = yylex();
                } else {
                    token = pushbackToken;
                    pushbackToken = -1;
                }
                if (token == Parser.QUOTED) {
                    // token QUOTED - a quoted literal as the attribute value
                    parsedAttribute(attributeName, text(), true);
                } else if (token == Parser.WORD || token == Parser.SLASH) {
                    // unquoted word
                    attributeBuffer.clear();
                    attributeBuffer.append(text());
                    while (true) {
                        int next;
                        if (pushbackToken == -1) {
                            next = yylex();
                        } else {
                            next = pushbackToken;
                            pushbackToken = -1;
                        }
                        if ((next != Parser.WORD) && (next != Parser.EQUALS) && (next != Parser.SLASH)) {
                            pushBack(next);
                            break;
                        }
                        attributeBuffer.append(text());
                    }
                    parsedAttribute(attributeName, attributeBuffer.toString(), false);
                } else if (token == Parser.SLASH || token == Parser.GT) {
                    // no more attributes
                    pushBack(token);
                } else if (token == 0) {
                    return;
                } else {
                    reportError("Illegal attribute value", line(), column());
                }
                break;
            case Parser.SLASH:
            case Parser.GT:
            case Parser.WORD:
                // it was a value-less HTML style attribute
                parsedAttribute(attributeName, null, false);
                pushBack(token);
                break;
            case 0:
                return;
            default:
                reportError("Illegal attribute name", line(), column());
                break;
        }
    }

    /**
     * Parsed text.
     *
     * @param position
     *            the position
     * @param length
     *            the length
     */
    protected void parsedText(int position, int length) {
        this.position = position;
        this.length = length;
        handler.text(reusableToken);
    }

    /**
     * Parsed tag.
     *
     * @param type
     *            the type
     * @param name
     *            the name
     * @param start
     *            the start
     * @param length
     *            the length
     */
    protected void parsedTag(int type, String name, int start, int length) {
        this.type = type;
        this.name = name;
        this.position = start;
        this.length = length;
        handler.tag(reusableToken);
        reusableToken.attributeCount = 0;
    }

    /**
     * Parsed attribute.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param quoted
     *            the quoted
     */
    protected void parsedAttribute(String name, String value, boolean quoted) {
        if (reusableToken.attributeCount + 2 >= reusableToken.attributes.length) {
            String[] newAttributes = new String[reusableToken.attributeCount * 2];
            System.arraycopy(reusableToken.attributes, 0, newAttributes, 0, reusableToken.attributeCount);
            reusableToken.attributes = newAttributes;
        }
        reusableToken.attributes[reusableToken.attributeCount++] = name;
        if (quoted) {
            reusableToken.attributes[reusableToken.attributeCount++] = value.substring(1, value.length() - 1);
        } else {
            reusableToken.attributes[reusableToken.attributeCount++] = value;
        }
    }

    @Override
    protected void reportError(String message, int line, int column) {
        handler.warning(message, line, column);
    }

    /**
     * The Class ReusableToken.
     */
    public class ReusableToken implements Tag, Text {

        /** The attribute count. */
        public int attributeCount = 0;

        /** The attributes. */
        public String[] attributes = new String[10]; // name1, value1, name2, value2...

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public String getContents() {
            return new String(input, position, length);
        }

        @Override
        public void writeTo(SitemeshBufferFragment.Builder buffer, int position) {
            buffer.insert(position, SitemeshBufferFragment.builder().setBuffer(new DefaultSitemeshBuffer(input))
                    .setStart(position).setLength(length).build());
        }

        @Override
        public int getAttributeCount() {
            return attributeCount / 2;
        }

        @Override
        public int getAttributeIndex(String name, boolean caseSensitive) {
            if (attributeCount == 0) {
                return -1;
            }
            final int len = attributeCount;
            for (int i = 0; i < len; i += 2) {
                final String current = attributes[i];
                if (caseSensitive ? name.equals(current) : name.equalsIgnoreCase(current)) {
                    return i / 2;
                }
            }
            return -1;
        }

        @Override
        public String getAttributeName(int index) {
            return attributes[index * 2];
        }

        @Override
        public String getAttributeValue(int index) {
            return attributes[index * 2 + 1];
        }

        @Override
        public String getAttributeValue(String name, boolean caseSensitive) {
            if (attributeCount == 0) {
                return null;
            }
            final int len = attributeCount;
            for (int i = 0; i < len; i += 2) {
                final String current = attributes[i];
                if (caseSensitive ? name.equals(current) : name.equalsIgnoreCase(current)) {
                    return attributes[i + 1];
                }
            }
            return null;
        }

        @Override
        public boolean hasAttribute(String name, boolean caseSensitive) {
            return getAttributeIndex(name, caseSensitive) > -1;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public int getLength() {
            return length;
        }

    }
}
