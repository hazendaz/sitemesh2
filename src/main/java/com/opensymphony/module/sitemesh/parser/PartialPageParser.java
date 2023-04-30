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
package com.opensymphony.module.sitemesh.parser;

import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Page parser that doesn't parse the full page, but rather just parses the head section of the page.
 *
 * @since v2.5
 */
public class PartialPageParser implements PageParser {
    @Override
    public Page parse(char[] buffer) throws IOException {
        return parse(new DefaultSitemeshBuffer(buffer));
    }

    @Override
    public Page parse(SitemeshBuffer buffer) throws IOException {
        char[] data = buffer.getCharArray();
        int length = buffer.getBufferLength();
        int position = 0;
        while (position < data.length) {
            if (data[position++] == '<') {
                if (position < data.length && data[position] == '!') {
                    // Ignore doctype
                    continue;
                }
                if (compareLowerCase(data, length, position, "html")) {
                    // It's an HTML page, handle HTML pages
                    return parseHtmlPage(buffer, position);
                }
                // The whole thing is the body.
                return new PartialPageParserHtmlPage(buffer, new SitemeshBufferFragment(buffer, 0, length), null);
            }
        }
        // If we're here, we mustn't have found a tag
        return new PartialPageParserHtmlPage(buffer, new SitemeshBufferFragment(buffer, 0, length), null);
    }

    /**
     * Parses the html page.
     *
     * @param buffer
     *            the buffer
     * @param position
     *            the position
     *
     * @return the page
     */
    private Page parseHtmlPage(SitemeshBuffer buffer, int position) {
        char[] data = buffer.getCharArray();
        int length = buffer.getBufferLength();
        int bodyStart = -1;
        int bodyLength = -1;
        int headStart = -1;
        int headLength = -1;
        // Find head end and start, and body start
        Map<String, String> bodyProperties = null;
        while (position < length) {
            if (data[position++] == '<') {
                if (compareLowerCase(data, length, position, "head")) {
                    position = findEndOf(data, length, position + 4, ">");
                    headStart = position;
                    // Find end of head
                    position = findStartOf(data, length, position, "</head>");
                    headLength = position - headStart;
                    position += 7;
                } else if (compareLowerCase(data, length, position, "body")) {
                    HashSimpleMap map = new HashSimpleMap();
                    bodyStart = parseProperties(data, length, position + 4, map);
                    bodyProperties = map.getMap();
                    break;
                }
            }
        }

        if (bodyStart < 0) {
            // No body found
            bodyStart = length;
            bodyLength = 0;
        } else {
            for (int i = length - 8; i > bodyStart; i--) {
                if (compareLowerCase(data, length, i, "</body>")) {
                    bodyLength = i - bodyStart;
                    break;
                }
            }
            if (bodyLength == -1) {
                bodyLength = length - bodyStart;
            }
        }

        if (headLength <= 0) {
            return new PartialPageParserHtmlPage(buffer, new SitemeshBufferFragment(buffer, bodyStart, bodyLength),
                    bodyProperties);
        }
        int idx = headStart;
        int headEnd = headStart + headLength;
        String title = null;

        TreeMap<Integer, Integer> deletions = new TreeMap<>();

        // Extract meta attributes out of head
        Map<String, String> metaAttributes = new HashMap<>();
        while (idx < headEnd) {
            if ((data[idx++] == '<') && compareLowerCase(data, headEnd, idx, "meta")) {
                MetaTagSimpleMap map = new MetaTagSimpleMap();
                idx = parseProperties(data, headEnd, idx + 4, map);
                if (map.getName() != null && map.getContent() != null) {
                    metaAttributes.put(map.getName(), map.getContent());
                }
            }
        }

        // We need a new head buffer because we have to remove the title and content tags from it
        Map<String, String> pageProperties = new HashMap<>();
        for (int i = headStart; i < headEnd; i++) {
            char c = data[i];
            if (c == '<') {
                if (compareLowerCase(data, headEnd, i + 1, "title")) {
                    int titleStart = findEndOf(data, headEnd, i + 6, ">");
                    int titleEnd = findStartOf(data, headEnd, titleStart, "<");
                    title = new String(data, titleStart, titleEnd - titleStart);
                    int titleTagEnd = titleEnd + "</title>".length();
                    deletions.put(i, titleTagEnd - i);
                    i = titleTagEnd - 1;
                } else if (compareLowerCase(data, headEnd, i + 1, "content")) {
                    ContentTagSimpleMap map = new ContentTagSimpleMap();
                    int contentStart = parseProperties(data, headEnd, i + 8, map);
                    int contentEnd = findStartOf(data, headEnd, contentStart, "</content>");
                    pageProperties.put(map.getTag(), new String(data, contentStart, contentEnd - contentStart));
                    int contentTagEnd = contentEnd + "</content>".length();
                    deletions.put(i, contentTagEnd - i);
                    i = contentTagEnd - 1;
                }
            }
        }

        return new PartialPageParserHtmlPage(buffer, new SitemeshBufferFragment(buffer, bodyStart, bodyLength),
                bodyProperties, new SitemeshBufferFragment(buffer, headStart, headEnd - headStart, deletions), title,
                metaAttributes, pageProperties);
    }

    /**
     * Compare lower case.
     *
     * @param data
     *            the data
     * @param dataEnd
     *            the data end
     * @param position
     *            the position
     * @param token
     *            the token
     *
     * @return true, if successful
     */
    private static boolean compareLowerCase(final char[] data, final int dataEnd, int position, String token) {
        int l = position + token.length();
        if (l > dataEnd) {
            return false;
        }
        for (int i = 0; i < token.length(); i++) {
            // | 32 converts from ASCII uppercase to ASCII lowercase
            char potential = data[position + i];
            char needed = token.charAt(i);
            if (Character.isLetter(potential) && (potential | 32) != needed || potential != needed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find end of.
     *
     * @param data
     *            the data
     * @param dataEnd
     *            the data end
     * @param position
     *            the position
     * @param token
     *            the token
     *
     * @return the int
     */
    private static int findEndOf(final char[] data, final int dataEnd, int position, String token) {
        for (int i = position; i < dataEnd - token.length(); i++) {
            if (compareLowerCase(data, dataEnd, i, token)) {
                return i + token.length();
            }
        }
        return dataEnd;
    }

    /**
     * Find start of.
     *
     * @param data
     *            the data
     * @param dataEnd
     *            the data end
     * @param position
     *            the position
     * @param token
     *            the token
     *
     * @return the int
     */
    private static int findStartOf(final char[] data, final int dataEnd, int position, String token) {
        for (int i = position; i < dataEnd - token.length(); i++) {
            if (compareLowerCase(data, dataEnd, i, token)) {
                return i;
            }
        }
        return dataEnd;
    }

    /**
     * Parse the properties of the current tag.
     *
     * @param data
     *            the data
     * @param dataEnd
     *            the end index of the data
     * @param position
     *            our position in the data, this should be the first character after the tag name
     * @param map
     *            to the map to parse the properties into
     *
     * @return The position of the first character after the tag
     */
    private static int parseProperties(char[] data, int dataEnd, int position, SimpleMap map) {
        int idx = position;

        while (idx < dataEnd) {
            // Skip forward to the next non-whitespace character
            while (idx < dataEnd && Character.isWhitespace(data[idx])) {
                idx++;
            }

            // Make sure its not the end of the data or the end of the tag
            if (idx == dataEnd || data[idx] == '>' || data[idx] == '/') {
                break;
            }

            int startAttr = idx;

            // Find the next equals
            while (idx < dataEnd && !Character.isWhitespace(data[idx]) && data[idx] != '=' && data[idx] != '>') {
                idx++;
            }

            if (idx == dataEnd || data[idx] != '=') {
                continue;
            }

            String attrName = new String(data, startAttr, idx - startAttr);

            idx++;
            if (idx == dataEnd) {
                break;
            }

            int startValue = idx;
            int endValue;
            if (data[idx] == '"') {
                idx++;
                startValue = idx;
                while (idx < dataEnd && data[idx] != '"') {
                    idx++;
                }
                if (idx == dataEnd) {
                    break;
                }
                endValue = idx;
                idx++;
            } else if (data[idx] == '\'') {
                idx++;
                startValue = idx;
                while (idx < dataEnd && data[idx] != '\'') {
                    idx++;
                }
                if (idx == dataEnd) {
                    break;
                }
                endValue = idx;
                idx++;
            } else {
                while (idx < dataEnd && !Character.isWhitespace(data[idx]) && data[idx] != '/' && data[idx] != '>') {
                    idx++;
                }
                endValue = idx;
            }
            String attrValue = new String(data, startValue, endValue - startValue);
            map.put(attrName, attrValue);
        }
        // Find the end of the tag
        while (idx < dataEnd && data[idx] != '>') {
            idx++;
        }
        if (idx == dataEnd) {
            return idx;
        }
        // Return the first character after the end of the tag
        return idx + 1;
    }

    /**
     * The Interface SimpleMap.
     */
    public interface SimpleMap {

        /**
         * Put.
         *
         * @param key
         *            the key
         * @param value
         *            the value
         */
        void put(String key, String value);
    }

    /**
     * The Class MetaTagSimpleMap.
     */
    public static class MetaTagSimpleMap implements SimpleMap {

        /** The name. */
        private String name;

        /** The content. */
        private String content;

        @Override
        public void put(String key, String value) {
            if (key.equals("name")) {
                name = value;
            } else if (key.equals("content")) {
                content = value;
            }
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the content.
         *
         * @return the content
         */
        public String getContent() {
            return content;
        }
    }

    /**
     * The Class ContentTagSimpleMap.
     */
    public static class ContentTagSimpleMap implements SimpleMap {

        /** The tag. */
        private String tag;

        @Override
        public void put(String key, String value) {
            if (key.equals("tag")) {
                tag = value;
            }
        }

        /**
         * Gets the tag.
         *
         * @return the tag
         */
        public String getTag() {
            return tag;
        }
    }

    /**
     * The Class HashSimpleMap.
     */
    public static class HashSimpleMap implements SimpleMap {

        /** The map. */
        private final Map<String, String> map = new HashMap<>();

        @Override
        public void put(String key, String value) {
            map.put(key, value);
        }

        /**
         * Gets the map.
         *
         * @return the map
         */
        public Map<String, String> getMap() {
            return map;
        }
    }
}
