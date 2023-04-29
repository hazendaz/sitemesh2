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

/**
 * Selects an appropriate Decorator for the Content.
 * <p/>
 * Note: Since SiteMesh 3, this replaces the DecoratorMapper.
 *
 * @author Joe Walnes
 * @since SiteMesh 3
 */
public interface DecoratorSelector {

    Decorator selectDecorator(Content content, SiteMeshContext context);

}
