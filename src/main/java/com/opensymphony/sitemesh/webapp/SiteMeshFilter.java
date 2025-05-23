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
package com.opensymphony.sitemesh.webapp;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.compatability.DecoratorMapper2DecoratorSelector;
import com.opensymphony.sitemesh.compatability.PageParser2ContentProcessor;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Core Filter for integrating SiteMesh into a Java web application.
 *
 * @author Joe Walnes
 * @author Scott Farquhar
 *
 * @since SiteMesh 3
 */
public class SiteMeshFilter implements Filter {

    /** The filter config. */
    private FilterConfig filterConfig;

    /** The container tweaks. */
    private ContainerTweaks containerTweaks;

    /** The Constant ALREADY_APPLIED_KEY. */
    private static final String ALREADY_APPLIED_KEY = "com.opensymphony.sitemesh.APPLIED_ONCE";

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        containerTweaks = new ContainerTweaks();
    }

    @Override
    public void destroy() {
        filterConfig = null;
        containerTweaks = null;
    }

    /**
     * Main method of the Filter.
     * <p>
     * Checks if the Filter has been applied this request. If not, parses the page and applies
     * {@link com.opensymphony.module.sitemesh.Decorator} (if found).
     */
    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) rq;
        HttpServletResponse response = (HttpServletResponse) rs;
        ServletContext servletContext = filterConfig.getServletContext();

        SiteMeshWebAppContext webAppContext = new SiteMeshWebAppContext(request, response, servletContext);

        ContentProcessor contentProcessor = initContentProcessor(webAppContext);
        DecoratorSelector decoratorSelector = initDecoratorSelector(webAppContext);

        if (filterAlreadyAppliedForRequest(request) || !contentProcessor.handles(webAppContext)) {
            // Optimization: If the content doesn't need to be processed, bypass SiteMesh.
            chain.doFilter(request, response);
            return;
        }

        if (containerTweaks.shouldAutoCreateSession()) {
            // Some containers (such as Tomcat 4) will not allow sessions to be created in the decorator.
            // (i.e after the response has been committed).
            request.getSession(true);
        }

        try {

            Content content = obtainContent(contentProcessor, webAppContext, request, response, chain);

            if (content == null) {
                request.setAttribute(ALREADY_APPLIED_KEY, null);
                return;
            }

            Decorator decorator = decoratorSelector.selectDecorator(content, webAppContext);
            decorator.render(content, webAppContext);

        } catch (IllegalStateException e) {
            // Some containers (such as WebLogic) throw an IllegalStateException when an error page is served.
            // It may be ok to ignore this. However, for safety it is propegated if possible.
            if (!containerTweaks.shouldIgnoreIllegalStateExceptionOnErrorPage()) {
                throw e;
            }
        } catch (RuntimeException e) {
            if (containerTweaks.shouldLogUnhandledExceptions()) {
                // Some containers (such as Tomcat 4) swallow RuntimeExceptions in filters.
                servletContext.log("Unhandled exception occurred whilst decorating page", e);
            }
            throw e;
        } catch (ServletException e) {
            request.setAttribute(ALREADY_APPLIED_KEY, null);
            throw e;
        }

    }

    /**
     * Inits the content processor.
     *
     * @param webAppContext
     *            the web app context
     *
     * @return the content processor
     */
    protected ContentProcessor initContentProcessor(SiteMeshWebAppContext webAppContext) {
        // TODO: Remove heavy coupling on horrible SM2 Factory
        Factory factory = Factory.getInstance(new Config(filterConfig));
        factory.refresh();
        return new PageParser2ContentProcessor(factory);
    }

    /**
     * Inits the decorator selector.
     *
     * @param webAppContext
     *            the web app context
     *
     * @return the decorator selector
     */
    protected DecoratorSelector initDecoratorSelector(SiteMeshWebAppContext webAppContext) {
        // TODO: Remove heavy coupling on horrible SM2 Factory
        Factory factory = Factory.getInstance(new Config(filterConfig));
        factory.refresh();
        return new DecoratorMapper2DecoratorSelector(factory.getDecoratorMapper());
    }

    /**
     * Continue in filter-chain, writing all content to buffer and parsing into returned
     * {@link com.opensymphony.module.sitemesh.Page} object. If {@link com.opensymphony.module.sitemesh.Page} is not
     * parseable, null is returned.
     *
     * @param contentProcessor
     *            the content processor
     * @param webAppContext
     *            the web app context
     * @param request
     *            the request
     * @param response
     *            the response
     * @param chain
     *            the chain
     *
     * @return the content
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ServletException
     *             the servlet exception
     */
    private Content obtainContent(ContentProcessor contentProcessor, SiteMeshWebAppContext webAppContext,
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ContentBufferingResponse contentBufferingResponse = new ContentBufferingResponse(response, contentProcessor,
                webAppContext);
        chain.doFilter(request, contentBufferingResponse);
        // TODO: check if another servlet or filter put a page object in the request
        // Content result = request.getAttribute(PAGE);
        // if (result == null) {
        // // parse the page
        // result = pageResponse.getPage();
        // }
        webAppContext.setUsingStream(contentBufferingResponse.isUsingStream());
        return contentBufferingResponse.getContent();
    }

    /**
     * Filter already applied for request.
     *
     * @param request
     *            the request
     *
     * @return true, if successful
     */
    private boolean filterAlreadyAppliedForRequest(HttpServletRequest request) {
        if (request.getAttribute(ALREADY_APPLIED_KEY) == Boolean.TRUE) {
            return true;
        }
        request.setAttribute(ALREADY_APPLIED_KEY, Boolean.TRUE);
        return false;
    }

}
