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
/*
 * Title: FreemarkerDecoratorServlet Description:
 *
 * This software is published under the terms of the OpenSymphony Software License version 1.1, of which a copy has been included
 * with this distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.freemarker;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * Servlet that allows Freemarker templates to be used as decorators.
 *
 * @author <a href="mailto:richard.hallier@freesbee.fr">Richard HALLIER</a>
 */
public class FreemarkerDecoratorServlet extends FreemarkerServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean preTemplateProcess(HttpServletRequest request, HttpServletResponse response, Template template,
            TemplateModel templateModel) throws ServletException, IOException {
        boolean result = super.preTemplateProcess(request, response, template, templateModel);

        SimpleHash hash = (SimpleHash) templateModel;

        HTMLPage htmlPage = (HTMLPage) request.getAttribute(RequestConstants.PAGE);

        String title, body, head;

        if (htmlPage == null) {
            title = "No Title";
            body = "No Body";
            head = "<!-- No head -->";
        } else {
            title = htmlPage.getTitle();

            StringWriter buffer = new StringWriter();
            htmlPage.writeBody(buffer);
            body = buffer.toString();

            buffer = new StringWriter();
            htmlPage.writeHead(buffer);
            head = buffer.toString();

            hash.put("page", htmlPage);
        }

        hash.put("title", title);
        hash.put("body", body);
        hash.put("head", head);
        hash.put("base", request.getContextPath());

        /*
         * Factory factory = Factory.getInstance(new Config(getServletConfig())); Decorator decorator =
         * factory.getDecoratorMapper().getDecorator(request, htmlPage); -> decorator.getPage()
         */

        return result;
    }
}
