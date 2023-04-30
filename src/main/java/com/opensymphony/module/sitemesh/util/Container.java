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
 * Title:        Container
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility for determining the Servlet Container the application is running in. Currently supported containers: Tomcat,
 * Resin, Orion, OC4J, WebLogic, HPAS, JRun, Websphere.
 * <h2>Usage:</h2> <code>if (Container.get() == Container.TOMCAT) { .... }</code>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public final class Container {

    /** The Constant UNKNOWN. */
    public static final int UNKNOWN = 0;

    /** The Constant TOMCAT. */
    public static final int TOMCAT = 1;

    /** The Constant RESIN. */
    public static final int RESIN = 2;

    /** The Constant ORION. */
    public static final int ORION = 3; // Orion or OC4J

    /** The Constant WEBLOGIC. */
    public static final int WEBLOGIC = 4;

    /** The Constant HPAS. */
    public static final int HPAS = 5;

    /** The Constant JRUN. */
    public static final int JRUN = 6;

    /** The Constant WEBSPHERE. */
    public static final int WEBSPHERE = 7;

    /** The result. */
    private static int result = -1;

    /**
     * A map containing classes that can be searched for, and which container they are typically found in.
     */
    private static Map<String, Integer> classMappings = null;

    static {
        // initialize the classes that can be searched for
        classMappings = new HashMap<String, Integer>(6);
        classMappings.put("org.apache.jasper.runtime.JspFactoryImpl", Integer.valueOf(TOMCAT));
        classMappings.put("com.caucho.jsp.JspServlet", Integer.valueOf(RESIN));
        classMappings.put("com.evermind.server.http.JSPServlet", Integer.valueOf(ORION));
        classMappings.put("weblogic.servlet.JSPServlet", Integer.valueOf(WEBLOGIC));
        classMappings.put("com.hp.mwlabs.j2ee.containers.servlet.jsp.JspServlet", Integer.valueOf(HPAS));
        classMappings.put("jrun.servlet.WebApplicationService", Integer.valueOf(JRUN));
        classMappings.put("com.ibm.ws.webcontainer.jsp.servlet.JspServlet", Integer.valueOf(WEBSPHERE));
    }

    /**
     * Get the current container.
     *
     * @return the int
     */
    public static int get() {
        if (result == -1) {
            final String classMatch = searchForClosestClass(classMappings);

            if (classMatch == null) {
                result = UNKNOWN;
            } else {
                result = ((Integer) classMappings.get(classMatch)).intValue();
            }
        }
        return result;
    }

    /**
     * Walk up the classloader hierachy and attempt to find a class in the classMappings Map that can be loaded.
     *
     * @param classMappings
     *            the class mappings
     *
     * @return Name of the match class, or null if not found.
     */
    private static String searchForClosestClass(Map<String, Integer> classMappings) {
        // get closest classloader
        ClassLoader loader = Container.class.getClassLoader();

        // iterate up through the classloader hierachy (through parents), until no more left.
        while (loader != null) {

            for (Iterator<String> iterator = classMappings.keySet().iterator(); iterator.hasNext();) {
                String className = (String) iterator.next();

                try {
                    // attempt to load current classname with current classloader
                    loader.loadClass(className);
                    // if no exception has been thrown, we're in luck.
                    return className;
                } catch (ClassNotFoundException e) {
                    // no problem... we'll keep trying...
                }
            }
            loader = loader.getParent();
        }

        // couldn't find anything
        return null;
    }
}
