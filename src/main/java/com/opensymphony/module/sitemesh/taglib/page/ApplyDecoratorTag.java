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
 * Title:        ApplyDecoratorTag
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.page;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.DefaultSitemeshBuffer;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.PageParser;
import com.opensymphony.module.sitemesh.PageParserSelector;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.module.sitemesh.SitemeshBufferWriter;
import com.opensymphony.module.sitemesh.filter.PageRequestWrapper;
import com.opensymphony.module.sitemesh.filter.PageResponseWrapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.BodyTagSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This tag inserts an external resource as a panel into the current Page.
 * <p>
 * The page attribute should point to the panel resource which should expose an entire page (e.g. another JSP file
 * producing HTML). This attribute can be relative to the page it is being called from or an absolute path from the
 * context-root.
 * <p>
 * <strong>OR</strong>
 * <p>
 * If the page attribute is not specified, the body content is parsed into the
 * {@link com.opensymphony.module.sitemesh.Page} object and has the {@link com.opensymphony.module.sitemesh.Decorator}
 * applied.
 * <p>
 * The (optional) decorator attribute is the name of the {@link com.opensymphony.module.sitemesh.Decorator} to apply to
 * the included page. Note that the implementation of {@link com.opensymphony.module.sitemesh.DecoratorMapper} can
 * overide this.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class ApplyDecoratorTag extends BodyTagSupport implements RequestConstants {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The page. */
    private String page = null;

    /** The decorator. */
    private String decorator = null;

    /** The content type. */
    private String contentType = null;

    /** The encoding. */
    private String encoding = null;

    /** The params. */
    private Map<String, String> params = new HashMap<String, String>(6);

    /** The config. */
    private Config config = null;

    /** The decorator mapper. */
    private DecoratorMapper decoratorMapper = null;

    /** The factory. */
    private Factory factory;

    /**
     * Tag attribute: URI of page to include. Can be relative to page being called from, or absolute path from
     * context-root of web-app.
     *
     * @param page
     *            the new page
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * Add a parameter to the page. This has a package level access modifier so ParamTag can also call it.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    void addParam(String name, String value) {
        params.put(name, value);
    }

    /**
     * Tag attribute: If set, this value will override the 'title' property of the page. This is a convenience utility
     * and is identical to specifing a 'page:param name=title' tag.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        addParam("title", title);
    }

    /**
     * Tag attribute: If set, this value will override the 'id' property of the page. This is a convenience utility and
     * is identical to specifing a 'page:param name=id' tag.
     */
    @Override
    public void setId(String id) {
        addParam("id", id);
    }

    /**
     * Tag attribute: Name of Decorator to apply to Page. This is passed to DecoratorMapper to retrieve appropriate
     * Decorator. DecoratorMapper may override if needed.
     *
     * @param decorator
     *            the new name
     *
     * @see com.opensymphony.module.sitemesh.DecoratorMapper
     */
    public void setName(String decorator) {
        if (decorator != null) {
            this.decorator = decorator;
        }
    }

    /**
     * Sets the decorator.
     *
     * @param decorator
     *            the new decorator
     *
     * @deprecated Use setName() instead.
     */
    @Deprecated
    public void setDecorator(String decorator) {
        setName(decorator);
    }

    /**
     * Sets the content type.
     *
     * @param contentType
     *            the new content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Sets the encoding.
     *
     * @param encoding
     *            the new encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public int doStartTag() {
        if (config == null) {
            // set context if not already set
            config = new Config(pageContext.getServletConfig());
            factory = Factory.getInstance(config);
            decoratorMapper = factory.getDecoratorMapper();
        }
        // return page == null ? EVAL_BODY_BUFFERED : SKIP_BODY;
        return EVAL_BODY_BUFFERED;
    }

    /** Ensure that external page contents are included in bodycontent. */
    @Override
    public int doAfterBody() throws JspException {
        return SKIP_BODY;
    }

    /** Standard taglib method: apply decorator to page. */
    @Override
    public int doEndTag() throws JspException {
        try {
            // if composite decorator, remember last page
            Page oldPage = (Page) pageContext.getRequest().getAttribute(PAGE);

            // parse bodycontent into Page object
            PageParser parser = getParserSelector().getPageParser(contentType != null ? contentType : "text/html");
            Page pageObj;

            if (page == null) {
                // inline content
                if (bodyContent != null) {
                    // Would be nice if we could do our own buffering...
                    SitemeshBufferWriter sitemeshWriter = new SitemeshBufferWriter();
                    bodyContent.writeOut(sitemeshWriter);
                    pageObj = parser.parse(sitemeshWriter.getSitemeshBuffer());
                } else {
                    pageObj = parser.parse(new DefaultSitemeshBuffer(new char[] {}));
                }
            } else if (page.startsWith("http://") || page.startsWith("https://")) {
                try {
                    URL url = new URL(page);
                    URLConnection urlConn = url.openConnection();
                    urlConn.setUseCaches(true);

                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConn.getInputStream(), Charset.forName(encoding)));
                            SitemeshBufferWriter sitemeshWriter = new SitemeshBufferWriter()) {
                        char[] buf = new char[1000];
                        for (;;) {
                            int moved = in.read(buf);
                            if (moved < 0) {
                                break;
                            }
                            sitemeshWriter.write(buf, 0, moved);
                        }
                        pageObj = parser.parse(sitemeshWriter.getSitemeshBuffer());
                    }
                } catch (IOException e) {
                    throw new JspException(e);
                }
            } else {
                // external content
                String fullPath = page;
                if (fullPath.length() > 0 && fullPath.charAt(0) != '/') {
                    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

                    // find absolute path if relative supplied
                    String thisPath = request.getServletPath();

                    // check if it did not return null (could occur when the servlet container
                    // does not use a servlet to serve the requested resouce)
                    if (thisPath == null) {
                        String requestURI = request.getRequestURI();
                        if (request.getPathInfo() != null) {
                            // strip the pathInfo from the requestURI
                            thisPath = requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
                        } else {
                            thisPath = requestURI;
                        }
                    }

                    fullPath = thisPath.substring(0, thisPath.lastIndexOf('/') + 1) + fullPath;
                    int dotdot;
                    while ((dotdot = fullPath.indexOf("..")) > -1) {
                        int prevSlash = fullPath.lastIndexOf('/', dotdot - 2);
                        fullPath = fullPath.substring(0, prevSlash) + fullPath.substring(dotdot + 2);
                    }
                }

                // include page using filter response
                RequestDispatcher rd = pageContext.getServletContext().getRequestDispatcher(fullPath);
                PageRequestWrapper pageRequest = new PageRequestWrapper((HttpServletRequest) pageContext.getRequest());
                PageResponseWrapper pageResponse = new PageResponseWrapper(
                        (HttpServletResponse) pageContext.getResponse(), factory);

                StringBuilder sb = new StringBuilder(contentType != null ? contentType : "text/html");
                if (encoding != null) {
                    sb.append(";charset=").append(encoding);
                }
                pageResponse.setContentType(sb.toString());

                // if rd == null, then the panel was not found, but this isn't correct, so we need to spit out
                // something appropriate. What this is, well...I don't know yet.
                if (rd == null) {
                    throw new ApplyDecoratorException(
                            "The specified resource in applyDecorator tag (" + fullPath + ") was not found.");
                }
                rd.include(pageRequest, pageResponse);
                pageObj = pageResponse.getPage();
            }

            // If pageObj == null, then the panel source had some weird error in
            // it. Stop writing bugs like this. They're ugly and they make you smell funny.
            if (pageObj == null) {
                throw new ApplyDecoratorException(page + " did not create a valid page to decorate.");
            }

            // add extra params to Page
            Iterator<String> paramKeys = params.keySet().iterator();
            while (paramKeys.hasNext()) {
                String k = (String) paramKeys.next();
                String v = (String) params.get(k);
                pageObj.addProperty(k, v);
            }

            // get decorator
            if (decorator == null) {
                decorator = "";
            }
            pageObj.setRequest((HttpServletRequest) pageContext.getRequest());
            pageContext.getRequest().setAttribute(DECORATOR, decorator);
            Decorator d = decoratorMapper.getDecorator((HttpServletRequest) pageContext.getRequest(), pageObj);
            pageContext.getRequest().removeAttribute(DECORATOR);

            // apply decorator
            if ((d == null) || (d.getPage() == null)) {
                throw new JspException("Cannot locate inline Decorator: " + decorator);
            }
            pageContext.getRequest().setAttribute(PAGE, pageObj);
            pageContext.include(d.getPage());

            // clean up
            pageContext.getRequest().setAttribute(PAGE, oldPage);
            params.clear(); // params need to be cleared between invocations - SIM-191
        } catch (IOException | ServletException e) {
            throw new JspException(e);
        } catch (ApplyDecoratorException e) {
            try {
                pageContext.getOut().println(e.getMessage());
            } catch (IOException ioe) {
                System.err.println("IOException thrown in applyDecorator tag: " + e.toString());
            }
        }
        return EVAL_PAGE;
    }

    /**
     * Gets the parser selector.
     *
     * @return the parser selector
     */
    private PageParserSelector getParserSelector() {
        return Factory.getInstance(config);
    }

    /**
     * The Class ApplyDecoratorException.
     */
    class ApplyDecoratorException extends Exception {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new apply decorator exception.
         *
         * @param s
         *            the s
         */
        public ApplyDecoratorException(String s) {
            super(s);
        }
    }
}
