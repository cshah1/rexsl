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
package com.rexsl.maven.checks;

import com.rexsl.maven.Environment;
import com.rexsl.maven.EnvironmentMocker;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test case for {@link CssStaticCheck}.
 * @author Dmitry Bashkin (dmitry.bashkin@rexsl.com)
 * @author Yegor Bugayenko (yegor@qulice.com)
 * @version $Id$
 */
public final class CssStaticCheckTest {

    /**
     * CssStaticCheck can validate correct CSS file.
     * @throws Exception If something goes wrong
     * @todo #105 The validation doesn't work. CSSLint, for some reason,
     *  returns incorrect report when being executed from Maven. When the same
     *  file is executed from command line it works correctly. I don't know
     *  why it's happening.
     */
    @Test
    @org.junit.Ignore
    public void validatesCorrectCssFile() throws Exception {
        final Environment env = new EnvironmentMocker()
            .withDefaultClasspath()
            .withFile("src/main/webapp/css/valid.css")
            .mock();
        MatcherAssert.assertThat(
            "valid CSS passes without problems",
            new CssStaticCheck().validate(env)
        );
    }

    /**
     * CssStaticCheck can validate incorrect CSS file.
     * @throws Exception If something goes wrong
     */
    @Test
    @org.junit.Ignore
    public void validatesIncorrectCssFile() throws Exception {
        final Environment env = new EnvironmentMocker()
            .withDefaultClasspath()
            .withFile("src/main/webapp/css/invalid.css")
            .mock();
        MatcherAssert.assertThat(
            "invalid CSS is caught",
            !new CssStaticCheck().validate(env)
        );
    }

}
