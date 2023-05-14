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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class PathMapperTest.
 */
public class PathMapperTest {

    /** The path mapper. */
    private PathMapper pathMapper;

    /**
     * The test setup. throws Exception the exception
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
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
    public void testHardening() throws Exception {
        PathMapper bad = new PathMapper();
        bad.put(null, null);
        Assert.assertNull(bad.get(null));
        Assert.assertNull(bad.get(""));
        Assert.assertNull(bad.get("/somenonexistingpath"));
    }

    /**
     * Test find exact key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFindExactKey() throws Exception {
        Assert.assertEquals("exact1", pathMapper.get("/myexactfile.html"));
        Assert.assertEquals("exact2", pathMapper.get("/mydir/myexactfile.html"));
        Assert.assertEquals("exact3", pathMapper.get("/mydir/myexactfile.jsp"));
        Assert.assertEquals("exact4", pathMapper.get("/mydir/dodo"));
    }

    /**
     * Test find complex key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFindComplexKey() throws Exception {
        Assert.assertEquals("complex1", pathMapper.get("/mydir/"));
        Assert.assertEquals("complex1", pathMapper.get("/mydir/test1.xml"));
        Assert.assertEquals("complex1", pathMapper.get("/mydir/test321.jsp"));
        Assert.assertEquals("complex1", pathMapper.get("/mydir/otherdir"));

        Assert.assertEquals("complex2", pathMapper.get("/mydir/otherdir/test321.jsp"));

        Assert.assertEquals("complex3", pathMapper.get("/otherdir/test2.jsp"));
        Assert.assertEquals("complex3", pathMapper.get("/otherdir/test2.bpp"));

        Assert.assertEquals("complex4", pathMapper.get("/somedir/one/two/some/deep/file/test.xml"));
        Assert.assertEquals("complex4", pathMapper.get("/somedir/321.jsp.xml"));

        Assert.assertEquals("complex5", pathMapper.get("/mydir/otherdir/admin/myfile.html"));
        Assert.assertEquals("complex5", pathMapper.get("/mydir/somedir/admin/text.html"));

        Assert.assertEquals("complex6", pathMapper.get("/mydir/complexx/a-some-test-b.xctml"));
        Assert.assertEquals("complex6", pathMapper.get("/mydir/complexx/a b.xhtml"));
        Assert.assertEquals("complex6", pathMapper.get("/mydir/complexx/a___b.xhtml"));
    }

    /**
     * Test find default key.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testFindDefaultKey() throws Exception {
        Assert.assertEquals("default", pathMapper.get(null));
        Assert.assertEquals("default", pathMapper.get("/"));
        Assert.assertEquals("default", pathMapper.get("/*"));
        Assert.assertEquals("default", pathMapper.get("*"));
        Assert.assertEquals("default", pathMapper.get("blah.txt"));
        Assert.assertEquals("default", pathMapper.get("somefilewithoutextension"));
        Assert.assertEquals("default", pathMapper.get("/file_with_underscores-and-dashes.test"));
        Assert.assertEquals("default", pathMapper.get("/tuuuu*/file.with.dots.test.txt"));
    }
}
