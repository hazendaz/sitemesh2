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
package com.opensymphony.sitemesh;

import com.opensymphony.module.sitemesh.SitemeshBuffer;

import java.io.IOException;

/**
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public interface ContentProcessor {

    boolean handles(SiteMeshContext context);

    boolean handles(String contentType);

    Content build(SitemeshBuffer buffer, SiteMeshContext context) throws IOException;
}
