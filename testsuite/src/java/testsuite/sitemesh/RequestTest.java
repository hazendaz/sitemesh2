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
package testsuite.sitemesh;

import testsuite.tester.WebTest;
import com.meterware.httpunit.WebResponse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Properties;

/**
 * @author Joe Walnes
 */
public class RequestTest extends WebTest {

    public void testCurrentRequest() throws Exception {
        WebResponse rs = wc.getResponse(baseUrl + "/request/default.jsp?x=y");
        assertEquals(200, rs.getResponseCode());
        Properties properties = getProperties(rs.getText());
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("default.jsp, request"));
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("decorator-main.jsp, request"));
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("decorator-panel.jsp, request"));
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("inline.jsp, request"));
    }

    public void testPageRequest() throws Exception {
        WebResponse rs = wc.getResponse(baseUrl + "/request/default.jsp?x=y");
        assertEquals(200, rs.getResponseCode());
        Properties properties = getProperties(rs.getText());
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("decorator-main.jsp, page.request before applyDecorator"));
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("decorator-main.jsp, page.request after applyDecorator"));
        assertStringContains(
                "/request/default.jsp|x=y",
                properties.getProperty("decorator-panel.jsp, page.request"));
    }

    private Properties getProperties(String page) {
        Properties result = new Properties();
        Matcher matcher = Pattern.compile("^(.*):(.*)$", Pattern.MULTILINE).matcher(page);
        while (matcher.find()) {
            result.setProperty(matcher.group(1).trim(), matcher.group(2).trim());
        }
        return result;
    }

}
