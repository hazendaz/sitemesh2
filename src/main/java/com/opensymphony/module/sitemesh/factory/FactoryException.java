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
 * Title:        FactoryException
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.factory;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This RuntimeException is thrown by the Factory if it cannot initialize or perform an appropriate function.
 *
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 */
public class FactoryException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The exception. */
    protected Throwable exception;

    /**
     * Instantiates a new factory exception.
     */
    public FactoryException() {
        super();
    }

    /**
     * Instantiates a new factory exception.
     *
     * @param msg the msg
     */
    public FactoryException(String msg) {
        super(msg);
    }

    /**
     * Instantiates a new factory exception.
     *
     * @param e the e
     */
    public FactoryException(Exception e) {
        super();
        exception = e;
    }

    /**
     * Instantiates a new factory exception.
     *
     * @param msg the msg
     * @param e the e
     */
    public FactoryException(String msg, Throwable e) {
        super(msg + ": " + e);
        exception = e;
    }

    /**
     * Get the original cause of the Exception. Returns null if not known.
     *
     * @return the root cause
     */
    public Throwable getRootCause() {
        return exception;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (exception != null) {
            synchronized (System.err) {
                System.err.println("\nRoot cause:");
                exception.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        if (exception != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                exception.printStackTrace(s);
            }
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        if (exception != null) {
            synchronized (s) {
                s.println("\nRoot cause:");
                exception.printStackTrace(s);
            }
        }
    }
}