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
 * Title:        DefaultFactory
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.factory;

import com.opensymphony.module.sitemesh.Config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * DefaultFactory, reads configuration from the <code>sitemesh.configfile</code> init param, or
 * <code>/WEB-INF/sitemesh.xml</code> if not specified, or uses the default configuration if <code>sitemesh.xml</code>
 * does not exist.
 * <p>
 * To use the <code>sitemesh.configfile</code> parameter, add the following to your web.xml:
 *
 * <pre>
 * &lt;context-param&gt;
 *      &lt;param-name&gt;sitemesh.configfile&lt;/param-name&gt;
 *      &lt;param-value&gt;/WEB-INF/etc/sitemesh.xml&lt;/param-value&gt;
 *  &lt;/context-param&gt;
 * </pre>
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:pathos@pandora.be">Mathias Bogaert</a>
 */
public class DefaultFactory extends BaseFactory {

    /** The config file name. */
    String configFileName;

    /** The Constant DEFAULT_CONFIG_FILENAME. */
    private static final String DEFAULT_CONFIG_FILENAME = "/WEB-INF/sitemesh.xml";

    /** The config file. */
    File configFile;

    /** The config last modified. */
    long configLastModified;

    /** The config last check. */
    private long configLastCheck = 0L;

    /** The config check millis. */
    public static long configCheckMillis = 3000L;

    /** The config props. */
    Map<Object, Object> configProps = new HashMap<>();

    /** The excludes file name. */
    String excludesFileName;

    /** The excludes file. */
    File excludesFile;

    /**
     * Instantiates a new default factory.
     *
     * @param config
     *            the config
     */
    public DefaultFactory(Config config) {
        super(config);

        configFileName = config.getServletContext().getInitParameter("sitemesh.configfile");
        if (configFileName == null) {
            configFileName = DEFAULT_CONFIG_FILENAME;
        }

        // configFilePath is null if loaded from war file
        String initParamConfigFile = config.getConfigFile();
        if (initParamConfigFile != null) {
            configFileName = initParamConfigFile;
        }

        String configFilePath = config.getServletContext().getRealPath(configFileName);

        if (configFilePath != null) { // disable config auto reloading for .war files
            configFile = Path.of(configFilePath).toFile();
        }

        loadConfig();
    }

    /** Load configuration from file. */
    private synchronized void loadConfig() {
        try {
            // Load and parse the sitemesh.xml file
            Element root = loadSitemeshXML();

            NodeList sections = root.getChildNodes();
            // Loop through child elements of root node
            for (int i = 0; i < sections.getLength(); i++) {
                if (sections.item(i) instanceof Element) {
                    Element curr = (Element) sections.item(i);
                    NodeList children = curr.getChildNodes();

                    if ("config-refresh".equalsIgnoreCase(curr.getTagName())) {
                        String seconds = curr.getAttribute("seconds");
                        configCheckMillis = Long.parseLong(seconds) * 1000L;
                    } else if ("property".equalsIgnoreCase(curr.getTagName())) {
                        String name = curr.getAttribute("name");
                        String value = curr.getAttribute("value");
                        if (!"".equals(name) && !"".equals(value)) {
                            configProps.put("${" + name + "}", value);
                        }
                    } else if ("page-parsers".equalsIgnoreCase(curr.getTagName())) {
                        // handle <page-parsers>
                        loadPageParsers(children);
                    } else if ("decorator-mappers".equalsIgnoreCase(curr.getTagName())) {
                        // handle <decorator-mappers>
                        loadDecoratorMappers(children);
                    } else if ("excludes".equalsIgnoreCase(curr.getTagName())) {
                        // handle <excludes>
                        String fileName = replaceProperties(curr.getAttribute("file"));
                        if (!"".equals(fileName)) {
                            excludesFileName = fileName;
                            loadExcludes();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new FactoryException("Could not get XML parser", e);
        } catch (IOException e) {
            throw new FactoryException("Could not read config file : " + configFileName, e);
        } catch (SAXException e) {
            throw new FactoryException("Could not parse config file : " + configFileName, e);
        }
    }

    /**
     * Load sitemesh XML.
     *
     * @return the element
     *
     * @throws ParserConfigurationException
     *             the parser configuration exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SAXException
     *             the SAX exception
     */
    private Element loadSitemeshXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = null;

        if (configFile == null) {
            is = config.getServletContext().getResourceAsStream(configFileName);
        } else if (configFile.exists() && configFile.canRead()) {
            is = configFile.toURI().toURL().openStream();
        }

        // load the default sitemesh configuration
        if (is == null) {
            is = getClass().getClassLoader()
                    .getResourceAsStream("com/opensymphony/module/sitemesh/factory/sitemesh-default.xml");
        }

        // load the default sitemesh configuration using another classloader
        if (is == null) {
            is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("com/opensymphony/module/sitemesh/factory/sitemesh-default.xml");
        }

        if (is == null) {
            throw new IllegalStateException("Cannot load default configuration from jar");
        }

        if (configFile != null) {
            configLastModified = configFile.lastModified();
        }

        Document doc = builder.parse(is);
        Element root = doc.getDocumentElement();
        // Verify root element
        if (!"sitemesh".equalsIgnoreCase(root.getTagName())) {
            throw new FactoryException("Root element of sitemesh configuration file not <sitemesh>", null);
        }
        return root;
    }

    /**
     * Load excludes.
     *
     * @throws ParserConfigurationException
     *             the parser configuration exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SAXException
     *             the SAX exception
     */
    private void loadExcludes() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        InputStream is = null;

        if (excludesFile == null) {
            is = config.getServletContext().getResourceAsStream(excludesFileName);
            if (is == null) {
                String excludesFilePath = config.getServletContext().getRealPath(excludesFileName);// getResourceAsStream
                                                                                                   // does not work on
                                                                                                   // weblogic 10.3.5
                if (excludesFilePath != null) {
                    excludesFile = Path.of(excludesFilePath).toFile();
                    is = excludesFile.toURI().toURL().openStream();
                }
            }
        } else if (excludesFile.exists() && excludesFile.canRead()) {
            is = excludesFile.toURI().toURL().openStream();
        }

        if (is == null) {
            throw new IllegalStateException("Cannot load excludes configuration file \"" + excludesFileName
                    + "\" as specified in \"sitemesh.xml\" or \"sitemesh-default.xml\"");
        }

        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        NodeList sections = root.getChildNodes();

        // Loop through child elements of root node looking for the <excludes> block
        for (int i = 0; i < sections.getLength(); i++) {
            if (sections.item(i) instanceof Element) {
                Element curr = (Element) sections.item(i);
                if ("excludes".equalsIgnoreCase(curr.getTagName())) {
                    loadExcludeUrls(curr.getChildNodes());
                }
            }
        }
    }

    /**
     * Loop through children of 'page-parsers' element and add all 'parser' mappings.
     *
     * @param nodes
     *            the nodes
     */
    private void loadPageParsers(NodeList nodes) {
        clearParserMappings();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                Element curr = (Element) nodes.item(i);

                if ("parser".equalsIgnoreCase(curr.getTagName())) {
                    String className = curr.getAttribute("class");
                    String contentType = curr.getAttribute("content-type");
                    mapParser(contentType, className);
                }
            }
        }
    }

    /**
     * Load decorator mappers.
     *
     * @param nodes
     *            the nodes
     */
    private void loadDecoratorMappers(NodeList nodes) {
        clearDecoratorMappers();
        Properties emptyProps = new Properties();

        pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.NullDecoratorMapper", emptyProps);

        // note, this works from the bottom node up.
        for (int i = nodes.getLength() - 1; i > 0; i--) {
            if (nodes.item(i) instanceof Element) {
                Element curr = (Element) nodes.item(i);
                if ("mapper".equalsIgnoreCase(curr.getTagName())) {
                    String className = curr.getAttribute("class");
                    Properties props = new Properties();
                    // build properties from <param> tags.
                    NodeList children = curr.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        if (children.item(j) instanceof Element) {
                            Element currC = (Element) children.item(j);
                            if ("param".equalsIgnoreCase(currC.getTagName())) {
                                String value = currC.getAttribute("value");
                                props.put(currC.getAttribute("name"), replaceProperties(value));
                            }
                        }
                    }
                    // add mapper
                    pushDecoratorMapper(className, props);
                }
            }
        }

        pushDecoratorMapper("com.opensymphony.module.sitemesh.mapper.InlineDecoratorMapper", emptyProps);
    }

    /**
     * Reads in all the url patterns to exclude from decoration.
     *
     * @param nodes
     *            the nodes
     */
    private void loadExcludeUrls(NodeList nodes) {
        clearExcludeUrls();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                Element p = (Element) nodes.item(i);
                if ("pattern".equalsIgnoreCase(p.getTagName()) || "url-pattern".equalsIgnoreCase(p.getTagName())) {
                    Text patternText = (Text) p.getFirstChild();
                    if (patternText != null) {
                        String pattern = patternText.getData().trim();
                        if (pattern != null) {
                            addExcludeUrl(pattern);
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if configuration file has been modified, and if so reload it.
     */
    @Override
    public void refresh() {
        long time = System.currentTimeMillis();
        if (time - configLastCheck < configCheckMillis) {
            return;
        }
        configLastCheck = time;

        if (configFile != null && configLastModified != configFile.lastModified()) {
            loadConfig();
        }
    }

    /**
     * Replaces any properties that appear in the supplied string with their actual values.
     *
     * @param str
     *            the string to replace the properties in
     *
     * @return the same string but with any properties expanded out to their actual values
     */
    private String replaceProperties(String str) {
        int idx;
        for (Map.Entry<Object, Object> entry : configProps.entrySet()) {
            String key = (String) entry.getKey();
            while ((idx = str.indexOf(key)) >= 0) {
                StringBuilder buf = new StringBuilder(100);
                buf.append(str.substring(0, idx));
                buf.append(entry.getValue());
                buf.append(str.substring(idx + key.length()));
                str = buf.toString();
            }
        }
        return str;
    }
}
