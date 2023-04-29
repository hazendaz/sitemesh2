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
package com.opensymphony.module.sitemesh.html.rules;

/**
 * Allows a TagRule to add information to a Page object. The standard HTML processing rules bundled with SiteMesh use
 * this interface instead of direct coupling to the HTMLPage class, allowing the rules to be used for HTML processing in
 * applications outside of SiteMesh.
 *
 * @author Joe Walnes
 */
public interface PageBuilder {
    void addProperty(String key, String value);
}
