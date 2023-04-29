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
 * Title:        UseHTMLPageTEI
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.taglib.decorator;

/**
 * TagExtraInfo implementation to expose HTMLPage object as variable.
 *
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 *
 * @see UsePageTag
 * @see UsePageTEI
 */
public class UseHTMLPageTEI extends UsePageTEI {
    protected String getType() {
        return "com.opensymphony.module.sitemesh.HTMLPage";
    }
}
