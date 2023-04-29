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

import com.opensymphony.module.sitemesh.SitemeshBuffer;
import com.opensymphony.module.sitemesh.SitemeshBufferFragment;

public interface HTMLProcessorContext {

    SitemeshBuffer getSitemeshBuffer();

    State currentState();

    void changeState(State newState);

    void pushBuffer(SitemeshBufferFragment.Builder fragment);

    SitemeshBufferFragment.Builder currentBuffer();

    SitemeshBufferFragment.Builder popBuffer();
}
