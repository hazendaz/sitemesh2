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
 * Title:        PathMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The PathMapper is used to map file patterns to keys, and find an approriate key for a given file path. The pattern
 * rules are consistent with those defined in the Servlet 2.3 API on the whole. Wildcard patterns are also supported,
 * using any combination of * and ?.
 * <h2>Example</h2> <blockquote><code>
 * PathMapper pm = new PathMapper();<br>
 * <br>
 * pm.put("one","/");<br>
 * pm.put("two","/mydir/*");<br>
 * pm.put("three","*.xml");<br>
 * pm.put("four","/myexactfile.html");<br>
 * pm.put("five","/*\/admin/*.??ml");<br>
 * <br>
 * String result1 = pm.get("/mydir/myfile.xml"); // returns "two";<br>
 * String result2 = pm.get("/mydir/otherdir/admin/myfile.html"); // returns "five";<br>
 * </code></blockquote>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 */
public class PathMapper {

    /** The mappings. */
    private Map<String, String> mappings = new HashMap<String, String>();

    /**
     * Add a key and appropriate matching pattern.
     *
     * @param key
     *            the key
     * @param pattern
     *            the pattern
     */
    public void put(String key, String pattern) {
        if (key != null) {
            mappings.put(pattern, key);
        }
    }

    /**
     * Retrieve appropriate key by matching patterns with supplied path.
     *
     * @param path
     *            the path
     *
     * @return the string
     */
    public String get(String path) {
        if (path == null) {
            path = "/";
        }
        String mapped = findKey(path, mappings);
        if (mapped == null) {
            return null;
        }
        return (String) mappings.get(mapped);
    }

    /**
     * Find exact key in mappings.
     *
     * @param path
     *            the path
     * @param mappings
     *            the mappings
     *
     * @return the string
     */
    private static String findKey(String path, Map<String, String> mappings) {
        String result = findExactKey(path, mappings);
        if (result == null) {
            result = findComplexKey(path, mappings);
        }
        if (result == null) {
            result = findDefaultKey(mappings);
        }
        return result;
    }

    /**
     * Check if path matches exact pattern ( /blah/blah.jsp ).
     *
     * @param path
     *            the path
     * @param mappings
     *            the mappings
     *
     * @return the string
     */
    private static String findExactKey(String path, Map<String, String> mappings) {
        if (mappings.containsKey(path)) {
            return path;
        }
        return null;
    }

    /**
     * Find complex key.
     *
     * @param path
     *            the path
     * @param mappings
     *            the mappings
     *
     * @return the string
     */
    private static String findComplexKey(String path, Map<String, String> mappings) {
        Iterator<String> i = mappings.keySet().iterator();
        String result = null, key = null;
        while (i.hasNext()) {
            key = (String) i.next();
            if (key.length() > 1 && (key.indexOf('?') != -1 || key.indexOf('*') != -1) && match(key, path, false)) {
                if (result == null || key.length() > result.length()) {
                    // longest key wins
                    result = key;
                }
            }
        }
        return result;
    }

    /**
     * Look for root pattern ( / ).
     *
     * @param mappings
     *            the mappings
     *
     * @return the string
     */
    private static String findDefaultKey(Map<String, String> mappings) {
        String[] defaultKeys = { "/", "*", "/*" };
        for (String defaultKey : defaultKeys) {
            if (mappings.containsKey(defaultKey)) {
                return defaultKey;
            }
        }
        return null;
    }

    /**
     * Match.
     *
     * @param pattern
     *            the pattern
     * @param str
     *            the str
     * @param isCaseSensitive
     *            the is case sensitive
     *
     * @return true, if successful
     */
    private static boolean match(String pattern, String str, boolean isCaseSensitive) {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        char ch;

        boolean containsStar = false;
        for (char element : patArr) {
            if (element == '*') {
                containsStar = true;
                break;
            }
        }

        if (!containsStar) {
            // No '*'s, so we make a shortcut
            if (patIdxEnd != strIdxEnd) {
                return false; // Pattern and string do not have the same size
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (ch != '?') {
                    if ((isCaseSensitive && ch != strArr[i])
                            || (!isCaseSensitive && Character.toUpperCase(ch) != Character.toUpperCase(strArr[i]))) {
                        return false; // Character mismatch
                    }
                }
            }
            return true; // String matches against pattern
        }

        if (patIdxEnd == 0) {
            return true; // Pattern contains only '*', which matches anything
        }

        // Process characters before first star
        while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if ((isCaseSensitive && ch != strArr[strIdxStart]) || (!isCaseSensitive
                        && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxStart]))) {
                    return false; // Character mismatch
                }
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // Process characters after last star
        while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if ((isCaseSensitive && ch != strArr[strIdxEnd]) || (!isCaseSensitive
                        && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxEnd]))) {
                    return false; // Character mismatch
                }
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            // All characters in the string are used. Check if only '*'s are
            // left in the pattern. If so, we succeeded. Otherwise failure.
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        // process pattern between stars. padIdxStart and patIdxEnd point
        // always to a '*'.
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                // Two stars next to each other, skip the first one.
                patIdxStart++;
                continue;
            }
            // Find the pattern between padIdxStart & padIdxTmp in str between
            // strIdxStart & strIdxEnd
            int patLength = patIdxTmp - patIdxStart - 1;
            int strLength = strIdxEnd - strIdxStart + 1;
            int foundIdx = -1;
            strLoop: for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart + j + 1];
                    if (ch != '?') {
                        if ((isCaseSensitive && ch != strArr[strIdxStart + i + j]) || (!isCaseSensitive
                                && Character.toUpperCase(ch) != Character.toUpperCase(strArr[strIdxStart + i + j]))) {
                            continue strLoop;
                        }
                    }
                }

                foundIdx = strIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        // All characters in the string are used. Check if only '*'s are left
        // in the pattern. If so, we succeeded. Otherwise failure.
        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }
}
