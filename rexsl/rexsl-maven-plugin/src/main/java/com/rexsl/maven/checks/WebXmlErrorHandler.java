/**
 * Copyright (c) 2011, ReXSL.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the ReXSL.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rexsl.maven.checks;

import com.ymock.util.Logger;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Handles errors of web.xml schema validator.
 *
 * @author Dmitry Bashkin (dmitry.bashkin@rexsl.com)
 * @version $Id: WebXmlErrorHandler.java 204 2011-10-26 21:15:28Z guard $
 */
public final class WebXmlErrorHandler implements ErrorHandler {

    /**
     * Contains validation errors.
     */
    private final transient Collection<Exception> errors =
        new LinkedList<Exception>();

    /**
     * File is being validated.
     */
    private final transient  File file;

    /**
     * Creates new instance of <code>WebXmlErrorHandler</code> with the
     * specified validated file.
     * @param name File to be validated.
     */
    public WebXmlErrorHandler(final File name) {
        this.file = name;
    }

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
        this.add(exception);
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        this.add(exception);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws
        SAXException {
        this.add(exception);
    }

    /**
     * Returns <code>true</code> if there are no validation errors.
     * @return True if if there are no validation errors.
     */
    public boolean isEmpty() {
        return this.errors.isEmpty();
    }

    /**
     * Registers validation error (adds it to the container).
     * @param exception Exception to be added to the container.
     */
    private void add(final SAXParseException exception) {
        Logger.error(
            this,
            "File: %s contains the following error: %s.",
            this.file,
            exception.getMessage()
        );
        this.errors.add(exception);
    }
}
