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

/**
 * Exception thrown when reading config.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class ConfigException extends Exception {

	private Exception cause;

	public ConfigException( String msg, Exception cause ) {
		super( msg );
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

}
