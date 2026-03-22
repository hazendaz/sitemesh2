/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package testsuite.sitemesh;

import com.meterware.httpunit.WebResponse;
import electric.xml.Document;
import testsuite.tester.WebTest;

/**
 * @author <a href="mailto:richard.hallier@freesbee.fr">Richard Hallier</a>
 */
public class FreemarkerDecoratorTest  extends WebTest {
    public void testFreemarkerDecoratedPage() throws Exception {
		WebResponse rs = wc.getResponse( baseUrl + "/freemarker/freemarker.jsp" );
		Document doc = getDocument( rs );
		assertEquals( "[:: Simple Freemarker Page ::]", rs.getTitle() );
		assertEquals( "Hello Freemarker World", doc.getElementWithId( "p1" ).getText().toString() );
		assertEquals( "footer", doc.getElementWithId( "footer" ).getText().toString() );
		assertEquals( "Simple Freemarker Page", doc.getElementWithId( "header" ).getText().toString() );
        assertEquals( "\u0126\u0118\u0139\u0139\u0150", doc.getElementWithId( "i18n" ).getText().toString() );
	}
}
