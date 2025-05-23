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

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.xml.sax.SAXException;

import com.meterware.httpunit.*;
import testsuite.tester.WebTest;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Tests multipass feature of SM.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class MultipassTest extends WebTest {

	public void testExtractsSelectedDivsFromContentOnSecondPass() throws Exception {
        String expected = get(baseUrl + "/multipass/expected-result.html");
        String actual = get(baseUrl + "/multipass/content.html");
		assertEquals(expected, actual);
	}

    private String get(String path) throws Exception {
        WebResponse rs = wc.getResponse(path);
        assertEquals(200, rs.getResponseCode() );
        return trimBlankLines(rs.getText());
    }

    private String trimBlankLines(String text) {
        Pattern pattern = Pattern.compile("^[ \\t]*$", Pattern.MULTILINE);
        return pattern.matcher(text).replaceAll("");
    }
}
