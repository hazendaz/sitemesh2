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
import electric.xml.Document;

/**
 * Test that the default welcome pages work ok (i.e. default.jsp).
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class WelcomePageTest extends WebTest {

  public void testWelcomePage() throws Exception {
    checkPage( "/welcomepage/" );
	}

  public void testWelcomePageWithoutTrailingSlash() throws Exception {
    checkPage( "/welcomepage" );
	}

  private void checkPage( String path ) throws Exception {
    WebResponse rs = wc.getResponse( baseUrl + path );
		Document doc = getDocument( rs );
		assertEquals( "[:: Welcome Page ::]", rs.getTitle() );
		assertEquals( "Welcome to the page", doc.getElementWithId( "p1" ).getText().toString() );
		assertEquals( "footer", doc.getElementWithId( "footer" ).getText().toString() );
		assertEquals( "Welcome Page", doc.getElementWithId( "header" ).getText().toString() );
  }

}
