/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2024 Hazendaz.
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class ConfigLoaderTest.
 */
class ConfigLoaderTest {

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
    @BeforeEach
    void setUp() throws Exception {
        // create temp file
        tempConfigFile = File.createTempFile("decorators-test", ".xml");
        tempConfigFile.deleteOnExit();

        // write to temp file
        BufferedWriter out = Files.newBufferedWriter(tempConfigFile.toPath(), StandardCharsets.UTF_8);
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
    @AfterEach
    void tearDown() throws Exception {
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
    void mappedNames() throws Exception {
        Assertions.assertEquals("default", configLoader.getMappedName("/info/somepage.html"));
        Assertions.assertEquals("default", configLoader.getMappedName("/test/somepage.html"));

        Assertions.assertEquals("other", configLoader.getMappedName("/other/someotherpage.html"));

        Assertions.assertEquals("uri", configLoader.getMappedName("/uri/somepage.html"));

        Assertions.assertEquals("rolebaseddeveloper", configLoader.getMappedName("/rolebased/someotherpage.html"));

        Assertions.assertEquals("old", configLoader.getMappedName("/old/someoldpage.html"));
    }

    /**
     * Test decorator presence.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void decoratorPresence() throws Exception {
        Assertions.assertNotNull(configLoader.getDecoratorByName("default"));
        Assertions.assertNotNull(configLoader.getDecoratorByName("other"));
        Assertions.assertNotNull(configLoader.getDecoratorByName("uri"));
        Assertions.assertNotNull(configLoader.getDecoratorByName("rolebaseddeveloper"));
        Assertions.assertNotNull(configLoader.getDecoratorByName("old"));
    }

    /**
     * Test decorators.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void decorators() throws Exception {
        Assertions.assertEquals("default", configLoader.getDecoratorByName("default").getName());
        Assertions.assertEquals("/decorators/default.jsp", configLoader.getDecoratorByName("default").getPage());
        Assertions.assertNull(configLoader.getDecoratorByName("default").getURIPath());
        Assertions.assertNull(configLoader.getDecoratorByName("default").getRole());

        Assertions.assertEquals("other", configLoader.getDecoratorByName("other").getName());
        Assertions.assertEquals("/other.jsp", configLoader.getDecoratorByName("other").getPage()); // absolute path
        Assertions.assertNull(configLoader.getDecoratorByName("other").getURIPath());
        Assertions.assertNull(configLoader.getDecoratorByName("other").getRole());

        Assertions.assertEquals("uri", configLoader.getDecoratorByName("uri").getName());
        Assertions.assertEquals("/decorators/uri.jsp", configLoader.getDecoratorByName("uri").getPage());
        Assertions.assertEquals("/someapp", configLoader.getDecoratorByName("uri").getURIPath());
        Assertions.assertNull(configLoader.getDecoratorByName("uri").getRole());

        Assertions.assertEquals("rolebased", configLoader.getDecoratorByName("rolebaseddeveloper").getName());
        Assertions.assertEquals("/decorators/rolebased.jsp",
                configLoader.getDecoratorByName("rolebaseddeveloper").getPage());
        Assertions.assertNull(configLoader.getDecoratorByName("rolebaseddeveloper").getURIPath());
        Assertions.assertEquals("developer", configLoader.getDecoratorByName("rolebaseddeveloper").getRole());

        Assertions.assertEquals("old", configLoader.getDecoratorByName("old").getName());
        Assertions.assertEquals("/decorators/old.jsp", configLoader.getDecoratorByName("old").getPage());
        Assertions.assertNull(configLoader.getDecoratorByName("old").getURIPath());
        Assertions.assertNull(configLoader.getDecoratorByName("old").getRole());
    }
}
