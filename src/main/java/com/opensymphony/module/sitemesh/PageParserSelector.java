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
package com.opensymphony.module.sitemesh;

/**
 * @author Joe Walnes
 */
public interface PageParserSelector {

    /**
     * Determine whether a Page of given content-type should be parsed or not.
     */
    boolean shouldParsePage(String contentType);

    /**
     * Create a PageParser suitable for the given content-type.
     * <p>
     * For example, if the supplied parameter is <code>text/html</code> a parser shall be returned that can parse HTML
     * accordingly.
     * </p>
     * Never returns null.
     *
     * @param contentType
     *            The MIME content-type of the data to be parsed
     *
     * @return Appropriate <code>PageParser</code> for reading data
     */
    PageParser getPageParser(String contentType);

}
