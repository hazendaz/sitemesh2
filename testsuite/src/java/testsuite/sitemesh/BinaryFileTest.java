/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package testsuite.sitemesh;

import testsuite.tester.WebTest;
import com.meterware.httpunit.WebResponse;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test binary files on sitemesh
 *
 * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
 */
public class BinaryFileTest extends WebTest {

    public void testImageIsDownloadedIntact() throws Exception {
        InputStream fis = new BufferedInputStream(Files.newInputStream(Path.of("src/webapp/binary/harry_potter.gif")));
        WebResponse rs = wc.getResponse(baseUrl + "/binary/harry_potter.gif");
        InputStream wis = rs.getInputStream();

        int f, w, count = 0;
        while (true) {
            count++;
            f = fis.read();
            w = wis.read();
            if (f == w && w == -1) {
                //end of stream, everything went well.
                return;
            } else if (f == w) {
                //matching up so far
                continue;
            } else {
                //not matching.
                fail("Binary file did not match at byte " + count + ".  Web returned '" + w + "'.  File returned '" + f + "'");
            }


        }
    }

}
