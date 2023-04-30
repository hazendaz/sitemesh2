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
/* This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file. */
package com.opensymphony.module.sitemesh.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * Provides a ServletOutputStream that routes through to another ServletOutputStream, however the destination can be
 * changed at any point. The destination can be passed in using a factory, so it will not be created until it's actually
 * needed.
 *
 * @author Joe Walnes
 */
public class RoutableServletOutputStream extends ServletOutputStream {

    /** The destination. */
    private ServletOutputStream destination;

    /** The factory. */
    private DestinationFactory factory;

    /**
     * Factory to lazily instantiate the destination.
     */
    public interface DestinationFactory {

        /**
         * Creates the.
         *
         * @return the servlet output stream
         *
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        ServletOutputStream create() throws IOException;
    }

    /**
     * Instantiates a new routable servlet output stream.
     *
     * @param factory
     *            the factory
     */
    public RoutableServletOutputStream(DestinationFactory factory) {
        this.factory = factory;
    }

    /**
     * Gets the destination.
     *
     * @return the destination
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private ServletOutputStream getDestination() throws IOException {
        if (destination == null) {
            destination = factory.create();
        }
        return destination;
    }

    /**
     * Update destination.
     *
     * @param factory
     *            the factory
     */
    public void updateDestination(DestinationFactory factory) {
        destination = null;
        this.factory = factory;
    }

    @Override
    public void close() throws IOException {
        getDestination().close();
    }

    @Override
    public void write(int b) throws IOException {
        getDestination().write(b);
    }

    @Override
    public void print(String s) throws IOException {
        getDestination().print(s);
    }

    @Override
    public void print(boolean b) throws IOException {
        getDestination().print(b);
    }

    @Override
    public void print(char c) throws IOException {
        getDestination().print(c);
    }

    @Override
    public void print(int i) throws IOException {
        getDestination().print(i);
    }

    @Override
    public void print(long l) throws IOException {
        getDestination().print(l);
    }

    @Override
    public void print(float v) throws IOException {
        getDestination().print(v);
    }

    @Override
    public void print(double v) throws IOException {
        getDestination().print(v);
    }

    @Override
    public void println() throws IOException {
        getDestination().println();
    }

    @Override
    public void println(String s) throws IOException {
        getDestination().println(s);
    }

    @Override
    public void println(boolean b) throws IOException {
        getDestination().println(b);
    }

    @Override
    public void println(char c) throws IOException {
        getDestination().println(c);
    }

    @Override
    public void println(int i) throws IOException {
        getDestination().println(i);
    }

    @Override
    public void println(long l) throws IOException {
        getDestination().println(l);
    }

    @Override
    public void println(float v) throws IOException {
        getDestination().println(v);
    }

    @Override
    public void println(double v) throws IOException {
        getDestination().println(v);
    }

    @Override
    public void write(byte b[]) throws IOException {
        getDestination().write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        getDestination().write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        getDestination().flush();
    }
}
