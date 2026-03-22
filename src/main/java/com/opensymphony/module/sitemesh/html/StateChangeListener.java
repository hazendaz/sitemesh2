/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright 2011-2026 Hazendaz
 */
package com.opensymphony.module.sitemesh.html;

/**
 * The listener interface for receiving stateChange events. The class that is interested in processing a stateChange
 * event implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addStateChangeListener</code> method. When the stateChange event occurs, that object's appropriate
 * method is invoked.
 */
public interface StateChangeListener {

    /**
     * State finished.
     */
    void stateFinished();
}
