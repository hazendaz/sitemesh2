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
package com.opensymphony.module.sitemesh.filter;

/**
 * Extracts the type and encoding from an HTTP Content-Type header.
 *
 * @author Scott Farquhar
 */
public class HttpContentType {

    /** The type. */
    private final String type;

    /** The encoding. */
    private final String encoding;

    /**
     * Instantiates a new http content type.
     *
     * @param fullValue
     *            the full value
     */
    public HttpContentType(String fullValue) {
        // this is the content type + charset. eg: text/html;charset=UTF-8
        int offset = fullValue.lastIndexOf("charset=");
        encoding = offset != -1 ? extractContentTypeValue(fullValue, offset + 8) : null;
        type = extractContentTypeValue(fullValue, 0);
    }

    /**
     * Extract content type value.
     *
     * @param type
     *            the type
     * @param startIndex
     *            the start index
     *
     * @return the string
     */
    private String extractContentTypeValue(String type, int startIndex) {
        if (startIndex < 0) {
            return null;
        }

        // Skip over any leading spaces
        while (startIndex < type.length() && type.charAt(startIndex) == ' ') {
            startIndex++;
        }

        if (startIndex >= type.length()) {
            return null;
        }

        int endIndex = startIndex;

        if (type.charAt(startIndex) == '"') {
            startIndex++;
            endIndex = type.indexOf('"', startIndex);
            if (endIndex == -1) {
                endIndex = type.length();
            }
        } else {
            // Scan through until we hit either the end of the string or a
            // special character (as defined in RFC-2045). Note that we ignore '/'
            // since we want to capture it as part of the value.
            char ch;
            while (endIndex < type.length() && (ch = type.charAt(endIndex)) != ' ' && ch != ';' && ch != '('
                    && ch != ')' && ch != '[' && ch != ']' && ch != '<' && ch != '>' && ch != ':' && ch != ','
                    && ch != '=' && ch != '?' && ch != '@' && ch != '"' && ch != '\\') {
                endIndex++;
            }
        }
        return type.substring(startIndex, endIndex);
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }
}
