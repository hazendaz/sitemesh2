/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2025 Hazendaz.
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
 * Title:        FileDecoratorMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import com.opensymphony.module.sitemesh.Decorator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

/**
 * The FileDecoratorMapper will treat the name of the decorator as a file-name to use (in the context of the web-app).
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mike@atlassian.com">Mike Cannon-Brookes</a>
 *
 * @see com.opensymphony.module.sitemesh.DecoratorMapper
 * @see com.opensymphony.module.sitemesh.mapper.DefaultDecorator
 */
public class FileDecoratorMapper extends AbstractDecoratorMapper {

    /** The path not available. */
    private boolean pathNotAvailable = false;

    @Override
    public Decorator getNamedDecorator(HttpServletRequest req, String name) {
        if (pathNotAvailable || name == null) {
            return super.getNamedDecorator(req, name);
        }

        URL resourcePath;

        // try to locate the resource (might be an unexpanded WAR)
        try {
            resourcePath = config.getServletContext().getResource('/' + name);
        } catch (MalformedURLException e) {
            return super.getNamedDecorator(req, name);
        }

        String filePath = config.getServletContext().getRealPath(name);

        if (filePath == null && resourcePath == null) {
            pathNotAvailable = true;
            return super.getNamedDecorator(req, name);
        }
        if (filePath != null) { // do we really need this disk file check?!
            File file = Path.of(filePath).toFile();

            if (file.exists() && file.canRead() && file.isFile()) {
                // if filename exists with name of supplied decorator, return Decorator
                return new DefaultDecorator(name, name, null);
            } else {
                // otherwise delegate to parent mapper.
                return super.getNamedDecorator(req, name);
            }
        } else {
            // file path is null and resource path is not null - can't check file on disk
            return new DefaultDecorator(name, name, null);
        }
    }
}
