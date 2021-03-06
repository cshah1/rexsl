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
package com.rexsl.test;

import com.jcabi.aspects.Loggable;
import java.util.AbstractMap;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * HTTP header.
 *
 * <p>Objects of this class are immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 */
@ToString
@EqualsAndHashCode(callSuper = true)
@Loggable(Loggable.DEBUG)
final class Header extends AbstractMap.SimpleEntry<String, String> {

    /**
     * Serialization marker.
     */
    private static final long serialVersionUID = 0x7526FA78EEA2147AL;

    /**
     * Public ctor.
     * @param key The name of it
     * @param value The value
     */
    protected Header(@NotNull final String key, @NotNull final String value) {
        super(Header.normalized(key), value);
    }

    /**
     * This header has the same key?
     * @param key The key to compare with
     * @return TRUE if it's the same header
     */
    public boolean sameAs(@NotNull final String key) {
        return this.getKey().equals(Header.normalized(key));
    }

    /**
     * Normalize key.
     * @param key The key to normalize
     * @return Normalized key
     */
    @NotNull
    private static String normalized(@NotNull
        @Pattern(regexp = "[a-zA-Z0-9\\-]+") final String key) {
        final char[] chars = key.toCharArray();
        chars[0] = Header.upper(chars[0]);
        for (int pos = 1; pos < chars.length; ++pos) {
            if (chars[pos - 1] == '-') {
                chars[pos] = Header.upper(chars[pos]);
            }
        }
        return new String(chars);
    }

    /**
     * Convert char to upper case, if required.
     * @param chr The char to convert
     * @return Upper-case char
     */
    private static char upper(final char chr) {
        char upper;
        if (chr >= 'a' && chr <= 'z') {
            upper = (char) (chr - ('a' - 'A'));
        } else {
            upper = chr;
        }
        return upper;
    }

}
