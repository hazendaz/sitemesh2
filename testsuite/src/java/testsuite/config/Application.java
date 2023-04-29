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
package testsuite.config;

import java.io.File;

/**
 * Representation of application to be deployed and tested.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class Application {

	private String name;
	private File warLocation, earLocation;

	public Application( String name, File warLocation, File earLocation ) {
		this.name = name;
		this.warLocation = warLocation;
		this.earLocation = earLocation;
	}

	/**
	 * Name of application (e.g. SiteMesh)
	 */
	public String getName() { return name; }

	/**
	 * Where web-app .war is located.
	 */
	public File getWarLocation() { return warLocation; }

	/**
	 * Where .ear containing web-app is located.
	 */
	public File getEarLocation() { return earLocation; }

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append( "{ Application : \n  name=" );
		result.append( name );
		result.append( "\n  warLocation=" );
		result.append( warLocation );
		result.append( "\n  earLocation=" );
		result.append( earLocation );
		result.append( "\n}" );
		return result.toString();
	}

}
