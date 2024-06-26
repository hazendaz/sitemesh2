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
 * Title:        VelocityDecoratorServlet
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.velocity;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.module.sitemesh.util.OutputConverter;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet that allows Velocity templates to be used as decorators.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class VelocityDecoratorServlet extends VelocityViewServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Logger. */
    private static final Logger logger = LoggerFactory.getLogger(VelocityDecoratorServlet.class);

    @Override
    public Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) {
        HTMLPage htmlPage = (HTMLPage) request.getAttribute(RequestConstants.PAGE);
        String template;

        context.put("base", request.getContextPath());

        // For backwards compatability with apps that used the old VelocityDecoratorServlet
        // that extended VelocityServlet instead of VelocityViewServlet
        context.put("req", request);
        context.put("res", response);

        if (htmlPage == null) {
            context.put("title", "Title?");
            context.put("body", "<p>Body?</p>");
            context.put("head", "<!-- head -->");
            template = request.getServletPath();
        } else {
            context.put("title", OutputConverter.convert(htmlPage.getTitle()));
            {
                StringWriter buffer = new StringWriter();
                try {
                    htmlPage.writeBody(OutputConverter.getWriter(buffer));
                } catch (IOException e) {
                    logger.error("", e);
                }
                context.put("body", buffer.toString());
            }
            {
                StringWriter buffer = new StringWriter();
                try {
                    htmlPage.writeHead(OutputConverter.getWriter(buffer));
                } catch (IOException e) {
                    logger.error("", e);
                }
                context.put("head", buffer.toString());
            }
            context.put("page", htmlPage);
            DecoratorMapper decoratorMapper = getDecoratorMapper();
            Decorator decorator = decoratorMapper.getDecorator(request, htmlPage);
            template = decorator.getPage();
        }

        return getTemplate(template);
    }

    /**
     * Gets the decorator mapper.
     *
     * @return the decorator mapper
     */
    private DecoratorMapper getDecoratorMapper() {
        Factory factory = Factory.getInstance(new Config(getServletConfig()));
        return factory.getDecoratorMapper();
    }
}
