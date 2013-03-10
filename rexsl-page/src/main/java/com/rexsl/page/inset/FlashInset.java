/**
 * Copyright (c) 2011-2013, ReXSL.com
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
package com.rexsl.page.inset;

import com.rexsl.page.BasePage;
import com.rexsl.page.Inset;
import com.rexsl.page.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Page with a flash message (through cookie).
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.4.7
 * @see BasePage
 */
@ToString
@EqualsAndHashCode(of = { "resource", "name" })
public class FlashInset implements Inset {

    /**
     * The resource.
     */
    private final transient Resource resource;

    /**
     * Name of flash cookie.
     */
    private final transient String name;

    /**
     * Public ctor.
     * @param res The resource
     */
    public FlashInset(@NotNull final Resource res) {
        this(res, "X-Rexsl-Flash");
    }

    /**
     * Public ctor.
     * @param res The resource
     * @param cookie Name of cookie
     */
    public FlashInset(@NotNull final Resource res,
        @NotNull final String cookie) {
        this.resource = res;
        this.name = cookie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void render(@NotNull final BasePage<?, ?> page,
        @NotNull final Response.ResponseBuilder builder) {
        // builder.
    }

}
