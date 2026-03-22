/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.sitemesh.compatability;

import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.NoDecorator;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Adapts a SiteMesh 2 {@link DecoratorMapper} to a SiteMesh 2 {@link DecoratorSelector}.
 *
 * @author Joe Walnes
 *
 * @since SiteMesh 3
 */
public class DecoratorMapper2DecoratorSelector implements DecoratorSelector {

    /** The decorator mapper. */
    private final DecoratorMapper decoratorMapper;

    /**
     * Instantiates a new decorator mapper 2 decorator selector.
     *
     * @param decoratorMapper
     *            the decorator mapper
     */
    public DecoratorMapper2DecoratorSelector(DecoratorMapper decoratorMapper) {
        this.decoratorMapper = decoratorMapper;
    }

    @Override
    public Decorator selectDecorator(Content content, SiteMeshContext context) {
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        HttpServletRequest request = webAppContext.getRequest();
        com.opensymphony.module.sitemesh.Decorator decorator = decoratorMapper.getDecorator(request,
                new Content2HTMLPage(content, request));
        if (decorator == null || decorator.getPage() == null) {
            return new NoDecorator();
        }
        return new OldDecorator2NewDecorator(decorator);
    }

}
