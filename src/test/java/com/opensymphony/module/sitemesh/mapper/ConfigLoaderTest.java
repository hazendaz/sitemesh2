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
package com.opensymphony.module.sitemesh.mapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class ConfigLoaderTest.
 */
public class ConfigLoaderTest {

    /** The config loader. */
    private ConfigLoader configLoader;

    /** The temp config file. */
    private File tempConfigFile;

    /**
     * Setup the unit tests.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        // create temp file
        tempConfigFile = File.createTempFile("decorators-test", ".xml");
        tempConfigFile.deleteOnExit();

        // write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(tempConfigFile));
        out.write("<decorators defaultdir=\"/decorators\">");

        // new format test decorators

        out.write("   <decorator name=\"default\" page=\"default.jsp\">");
        out.write("       <pattern>/info/*</pattern>");
        out.write("       <url-pattern>   ");
        out.write("           /test/*");
        out.write("       </url-pattern>");
        out.write("       <url-pattern> </url-pattern>");
        out.write("       <url-pattern></url-pattern>");
        out.write("   </decorator>");

        out.write("   <decorator name=\"other\" page=\"/other.jsp\">");
        out.write("       <pattern>/other/*</pattern>");
        out.write("   </decorator>");

        out.write("   <decorator name=\"uri\" page=\"uri.jsp\" webapp=\"someapp\">");
        out.write("       <pattern>/uri/*</pattern>");
        out.write("   </decorator>");

        out.write("   <decorator name=\"rolebased\" page=\"rolebased.jsp\" role=\"developer\">");
        out.write("       <pattern>/rolebased/*</pattern>");
        out.write("   </decorator>");

        // old format test decorator

        out.write("   <decorator>");
        out.write("       <decorator-name>old</decorator-name>");
        out.write("       <resource>/decorators/old.jsp</resource>");
        out.write("   </decorator>");

        out.write("   <decorator-mapping>");
        out.write("       <decorator-name>old</decorator-name>");
        out.write("       <url-pattern>/old/*</url-pattern>");
        out.write("   </decorator-mapping>");

        out.write("   <decorator-mapping>");
        out.write("       <decorator-name>old2</decorator-name>");
        out.write("       <url-pattern></url-pattern>");
        out.write("   </decorator-mapping>");

        out.write("</decorators>");
        out.close();
        configLoader = new ConfigLoader(tempConfigFile);
    }

    /**
     * Cleanup the unit tests.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {
        if (tempConfigFile != null) {
            tempConfigFile.delete();
        }
        configLoader = null;
    }

    /**
     * Test mapped names.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMappedNames() throws Exception {
        Assert.assertEquals("default", configLoader.getMappedName("/info/somepage.html"));
        Assert.assertEquals("default", configLoader.getMappedName("/test/somepage.html"));

        Assert.assertEquals("other", configLoader.getMappedName("/other/someotherpage.html"));

        Assert.assertEquals("uri", configLoader.getMappedName("/uri/somepage.html"));

        Assert.assertEquals("rolebaseddeveloper", configLoader.getMappedName("/rolebased/someotherpage.html"));

        Assert.assertEquals("old", configLoader.getMappedName("/old/someoldpage.html"));
    }

    /**
     * Test decorator presence.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDecoratorPresence() throws Exception {
        Assert.assertNotNull(configLoader.getDecoratorByName("default"));
        Assert.assertNotNull(configLoader.getDecoratorByName("other"));
        Assert.assertNotNull(configLoader.getDecoratorByName("uri"));
        Assert.assertNotNull(configLoader.getDecoratorByName("rolebaseddeveloper"));
        Assert.assertNotNull(configLoader.getDecoratorByName("old"));
    }

    /**
     * Test decorators.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDecorators() throws Exception {
        Assert.assertEquals("default", configLoader.getDecoratorByName("default").getName());
        Assert.assertEquals("/decorators/default.jsp", configLoader.getDecoratorByName("default").getPage());
        Assert.assertNull(configLoader.getDecoratorByName("default").getURIPath());
        Assert.assertNull(configLoader.getDecoratorByName("default").getRole());

        Assert.assertEquals("other", configLoader.getDecoratorByName("other").getName());
        Assert.assertEquals("/other.jsp", configLoader.getDecoratorByName("other").getPage()); // absolute path
        Assert.assertNull(configLoader.getDecoratorByName("other").getURIPath());
        Assert.assertNull(configLoader.getDecoratorByName("other").getRole());

        Assert.assertEquals("uri", configLoader.getDecoratorByName("uri").getName());
        Assert.assertEquals("/decorators/uri.jsp", configLoader.getDecoratorByName("uri").getPage());
        Assert.assertEquals("/someapp", configLoader.getDecoratorByName("uri").getURIPath());
        Assert.assertNull(configLoader.getDecoratorByName("uri").getRole());

        Assert.assertEquals("rolebased", configLoader.getDecoratorByName("rolebaseddeveloper").getName());
        Assert.assertEquals("/decorators/rolebased.jsp",
                configLoader.getDecoratorByName("rolebaseddeveloper").getPage());
        Assert.assertNull(configLoader.getDecoratorByName("rolebaseddeveloper").getURIPath());
        Assert.assertEquals("developer", configLoader.getDecoratorByName("rolebaseddeveloper").getRole());

        Assert.assertEquals("old", configLoader.getDecoratorByName("old").getName());
        Assert.assertEquals("/decorators/old.jsp", configLoader.getDecoratorByName("old").getPage());
        Assert.assertNull(configLoader.getDecoratorByName("old").getURIPath());
        Assert.assertNull(configLoader.getDecoratorByName("old").getRole());
    }
}
