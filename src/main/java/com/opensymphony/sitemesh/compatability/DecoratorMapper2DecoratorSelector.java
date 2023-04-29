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
package com.opensymphony.sitemesh.compatability;

import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.NoDecorator;

import javax.servlet.http.HttpServletRequest;

/**
 * Adapts a SiteMesh 2 {@link DecoratorMapper} to a SiteMesh 2 {@link DecoratorSelector}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class DecoratorMapper2DecoratorSelector implements DecoratorSelector {

    private final DecoratorMapper decoratorMapper;

    public DecoratorMapper2DecoratorSelector(DecoratorMapper decoratorMapper) {
        this.decoratorMapper = decoratorMapper;
    }

    public Decorator selectDecorator(Content content, SiteMeshContext context) {
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        HttpServletRequest request = webAppContext.getRequest();
        com.opensymphony.module.sitemesh.Decorator decorator = decoratorMapper.getDecorator(request,
                new Content2HTMLPage(content, request));
        if (decorator == null || decorator.getPage() == null) {
            return new NoDecorator();
        } else {
            return new OldDecorator2NewDecorator(decorator);
        }
    }
}
