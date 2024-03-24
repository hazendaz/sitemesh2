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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class PathMapperTest.
 */
class PathMapperTest {

    /** The path mapper. */
    private PathMapper pathMapper;

    /**
     * The test setup. throws Exception the exception
     *
     * @throws Exception
     *             the exception
     */
    @BeforeEach
    void setUp() throws Exception {
        pathMapper = new PathMapper();

        // exact matches come first
        pathMapper.put("exact1", "/myexactfile.html");
        pathMapper.put("exact2", "/mydir/myexactfile.html");
        pathMapper.put("exact3", "/mydir/myexactfile.jsp");
        pathMapper.put("exact4", "/mydir/dodo");

        // then the complex matches
        pathMapper.put("complex1", "/mydir/*");
        pathMapper.put("complex2", "/mydir/otherdir/*.jsp");
        pathMapper.put("complex3", "/otherdir/*.??p");
        pathMapper.put("complex4", "*.xml");
        pathMapper.put("complex5", "/*/admin/*.??ml");
        pathMapper.put("complex6", "/*/complexx/a*b.x?tml");

        // if all the rest fails, use the default matches
        pathMapper.put("default", "*");
    }

    /**
     * Test hardening.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void hardening() throws Exception {
        PathMapper bad = new PathMapper();
        bad.put(null, null);
        Assertions.assertNull(bad.get(null));
        Assertions.assertNull(bad.get(""));
        Assertions.assertNull(bad.get("/somenonexistingpath"));
    }

    /**
     * Test find exact key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void findExactKey() throws Exception {
        Assertions.assertEquals("exact1", pathMapper.get("/myexactfile.html"));
        Assertions.assertEquals("exact2", pathMapper.get("/mydir/myexactfile.html"));
        Assertions.assertEquals("exact3", pathMapper.get("/mydir/myexactfile.jsp"));
        Assertions.assertEquals("exact4", pathMapper.get("/mydir/dodo"));
    }

    /**
     * Test find complex key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void findComplexKey() throws Exception {
        Assertions.assertEquals("complex1", pathMapper.get("/mydir/"));
        Assertions.assertEquals("complex1", pathMapper.get("/mydir/test1.xml"));
        Assertions.assertEquals("complex1", pathMapper.get("/mydir/test321.jsp"));
        Assertions.assertEquals("complex1", pathMapper.get("/mydir/otherdir"));

        Assertions.assertEquals("complex2", pathMapper.get("/mydir/otherdir/test321.jsp"));

        Assertions.assertEquals("complex3", pathMapper.get("/otherdir/test2.jsp"));
        Assertions.assertEquals("complex3", pathMapper.get("/otherdir/test2.bpp"));

        Assertions.assertEquals("complex4", pathMapper.get("/somedir/one/two/some/deep/file/test.xml"));
        Assertions.assertEquals("complex4", pathMapper.get("/somedir/321.jsp.xml"));

        Assertions.assertEquals("complex5", pathMapper.get("/mydir/otherdir/admin/myfile.html"));
        Assertions.assertEquals("complex5", pathMapper.get("/mydir/somedir/admin/text.html"));

        Assertions.assertEquals("complex6", pathMapper.get("/mydir/complexx/a-some-test-b.xctml"));
        Assertions.assertEquals("complex6", pathMapper.get("/mydir/complexx/a b.xhtml"));
        Assertions.assertEquals("complex6", pathMapper.get("/mydir/complexx/a___b.xhtml"));
    }

    /**
     * Test find default key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void findDefaultKey() throws Exception {
        Assertions.assertEquals("default", pathMapper.get(null));
        Assertions.assertEquals("default", pathMapper.get("/"));
        Assertions.assertEquals("default", pathMapper.get("/*"));
        Assertions.assertEquals("default", pathMapper.get("*"));
        Assertions.assertEquals("default", pathMapper.get("blah.txt"));
        Assertions.assertEquals("default", pathMapper.get("somefilewithoutextension"));
        Assertions.assertEquals("default", pathMapper.get("/file_with_underscores-and-dashes.test"));
        Assertions.assertEquals("default", pathMapper.get("/tuuuu*/file.with.dots.test.txt"));
    }
}
