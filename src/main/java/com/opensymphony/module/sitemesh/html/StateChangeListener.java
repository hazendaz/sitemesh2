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
